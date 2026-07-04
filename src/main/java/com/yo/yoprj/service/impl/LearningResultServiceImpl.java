package com.yo.yoprj.service.impl;

import com.yo.yoprj.common.exception.BadRequestException;
import com.yo.yoprj.common.exception.ConflictException;
import com.yo.yoprj.domain.entity.LearningResult;
import com.yo.yoprj.domain.entity.User;
import com.yo.yoprj.dto.learning.LearningResultCreateRequest;
import com.yo.yoprj.dto.learning.LearningResultResponse;
import com.yo.yoprj.repository.LearningResultRepository;
import com.yo.yoprj.service.AuthService;
import com.yo.yoprj.service.CourseClassService;
import com.yo.yoprj.service.LearningResultService;
import com.yo.yoprj.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class LearningResultServiceImpl implements LearningResultService {

    private final LearningResultRepository learningResultRepository;
    private final StudentService studentService;
    private final CourseClassService courseClassService;
    private final AuthService authService;

    @Transactional
    public LearningResultResponse create(LearningResultCreateRequest request, String username) throws BadRequestException {
        if (learningResultRepository.existsByStudentIdAndCourseClassIdAndResultMonth(
                request.getStudentId(), request.getCourseClassId(), request.getResultMonth())) {
            throw new ConflictException("Learning result already exists for this student, class, and month");
        }

        User user = authService.findActiveUserByUsername(username);
        LearningResult item = new LearningResult();
        item.setStudent(studentService.getStudent(request.getStudentId()));
        item.setCourseClass(courseClassService.getCourseClass(request.getCourseClassId()));
        item.setResultMonth(request.getResultMonth());
        item.setScore(request.getScore());
        item.setTeacherComment(request.getTeacherComment());
        item.setCreatedByUser(user);
        try {
            return toResponse(learningResultRepository.saveAndFlush(item));
        } catch (DataIntegrityViolationException ex) {
            if (isDuplicateLearningResult(ex)) {
                throw new ConflictException("Learning result already exists for this student, class, and month");
            }
            throw ex;
        }
    }

    @Transactional(readOnly = true)
    public List<LearningResultResponse> findByStudentId(Integer studentId, String username) throws BadRequestException {
        User user = authService.findActiveUserByUsername(username);
        if (user.getRole().name().equals("PARENT")) {
            studentService.getStudentForParent(studentId, user.getParent().getId());
        }
        return learningResultRepository.findByStudentId(studentId).stream().map(this::toResponse).toList();
    }

    private LearningResultResponse toResponse(LearningResult item) {
        return new LearningResultResponse(
                item.getId(),
                item.getStudent().getId(),
                item.getStudent().getFullName(),
                item.getCourseClass().getId(),
                item.getCourseClass().getName(),
                item.getResultMonth(),
                item.getScore(),
                item.getTeacherComment(),
                item.getCreatedByUser() != null ? item.getCreatedByUser().getId() : null,
                item.getCreatedByUser() != null ? item.getCreatedByUser().getUsername() : null,
                item.getCreatedAt(),
                item.getUpdatedAt());
    }

    private boolean isDuplicateLearningResult(DataIntegrityViolationException ex) {
        Throwable cause = ex.getMostSpecificCause();
        String message = cause != null ? cause.getMessage() : ex.getMessage();
        return message != null && message.toLowerCase(Locale.ROOT).contains("uq_learning_result");
    }
}
