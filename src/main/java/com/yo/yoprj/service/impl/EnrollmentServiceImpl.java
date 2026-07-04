package com.yo.yoprj.service.impl;

import com.yo.yoprj.common.exception.BadRequestException;
import com.yo.yoprj.common.exception.NotFoundException;
import com.yo.yoprj.domain.entity.CourseClass;
import com.yo.yoprj.domain.entity.Enrollment;
import com.yo.yoprj.domain.entity.Student;
import com.yo.yoprj.domain.enums.EnrollmentStatus;
import com.yo.yoprj.dto.enrollment.EnrollmentCreateRequest;
import com.yo.yoprj.dto.enrollment.EnrollmentResponse;
import com.yo.yoprj.dto.enrollment.EnrollmentUpdateRequest;
import com.yo.yoprj.repository.EnrollmentRepository;
import com.yo.yoprj.service.CourseClassService;
import com.yo.yoprj.service.EnrollmentService;
import com.yo.yoprj.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final StudentService studentService;
    private final CourseClassService courseClassService;
    private final ModelMapper modelMapper;

    @Transactional
    public EnrollmentResponse create(EnrollmentCreateRequest request) throws BadRequestException {
        if (enrollmentRepository.existsByStudentIdAndCourseClassId(request.getStudentId(), request.getCourseClassId())) {
            throw new BadRequestException("Student is already enrolled in this class");
        }

        CourseClass courseClass = courseClassService.getCourseClassForLock(request.getCourseClassId());
        long activeCount = enrollmentRepository.countByCourseClassIdAndStatus(request.getCourseClassId(), EnrollmentStatus.ACTIVE);
        if (activeCount >= courseClass.getMaxStudents()) {
            throw new BadRequestException("Class is full");
        }

        Student student = studentService.getStudent(request.getStudentId());
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourseClass(courseClass);
        enrollment.setEnrolledAt(request.getEnrolledAt());
        enrollment.setStatus(request.getStatus());
        enrollment.setNote(request.getNote());

        try {
            return modelMapper.map(enrollmentRepository.save(enrollment), EnrollmentResponse.class);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("Student is already enrolled in this class");
        }
    }

    @Transactional(readOnly = true)
    public Page<EnrollmentResponse> findByClassId(Integer classId, Pageable pageable) {
        return enrollmentRepository.findByCourseClassId(classId, pageable).map(enrollment -> modelMapper.map(enrollment, EnrollmentResponse.class));
    }

    public Enrollment getEnrollment(Integer studentId, Integer classId) throws BadRequestException {
        return enrollmentRepository.findByStudentIdAndCourseClassId(studentId, classId)
                .orElseThrow(() -> new BadRequestException("Enrollment not found for student and class"));
    }

    @Transactional
    public EnrollmentResponse update(Integer id, EnrollmentUpdateRequest request) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Enrollment not found: " + id));

        enrollment.setStatus(request.getStatus());
        enrollment.setNote(request.getNote());

        return modelMapper.map(enrollmentRepository.save(enrollment), EnrollmentResponse.class);
    }

    @Transactional(readOnly = true)
    public Page<EnrollmentResponse> findByStudentId(Integer studentId, Pageable pageable) {
        return enrollmentRepository.findByStudentId(studentId, pageable)
                .map(enrollment -> modelMapper.map(enrollment, EnrollmentResponse.class));
    }

}
