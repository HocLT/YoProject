package com.yo.yoprj.service.impl;

import com.yo.yoprj.common.exception.BadRequestException;
import com.yo.yoprj.common.exception.NotFoundException;
import com.yo.yoprj.domain.entity.*;
import com.yo.yoprj.domain.enums.AttendanceStatus;
import com.yo.yoprj.domain.enums.NotificationRecipientType;
import com.yo.yoprj.domain.enums.NotificationType;
import com.yo.yoprj.dto.attendance.*;
import com.yo.yoprj.repository.AttendanceRepository;
import com.yo.yoprj.repository.NotificationRepository;
import com.yo.yoprj.service.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final NotificationRepository notificationRepository;
    private final StudentService studentService;
    private final CourseClassService courseClassService;
    private final EnrollmentService enrollmentService;
    private final AuthService authService;
    private final ModelMapper modelMapper;

    @Transactional
    public AttendanceResponse create(AttendanceCreateRequest request, String username) throws BadRequestException {
        CourseClass courseClass = courseClassService.getCourseClass(request.getCourseClassId());
        Student student = studentService.getStudent(request.getStudentId());
        validateAttendanceDate(courseClass, request.getAttendanceDate());
        enrollmentService.getEnrollment(request.getStudentId(), request.getCourseClassId());

        if (attendanceRepository.existsByCourseClassIdAndStudentIdAndAttendanceDate(
                request.getCourseClassId(), request.getStudentId(), request.getAttendanceDate())) {
            throw new BadRequestException(duplicateAttendanceMessage(request));
        }

        Attendance attendance = new Attendance();
        attendance.setStudent(student);
        attendance.setCourseClass(courseClass);
        attendance.setAttendanceDate(request.getAttendanceDate());
        attendance.setStatus(request.getStatus());
        attendance.setNote(request.getNote());
        User recorder = authService.findActiveUserByUsername(username);
        attendance.setRecordedByUser(recorder);
        Attendance saved;
        try {
            saved = attendanceRepository.save(attendance);
        } catch (DataIntegrityViolationException ex) {
            if (attendanceRepository.existsByCourseClassIdAndStudentIdAndAttendanceDate(
                    request.getCourseClassId(), request.getStudentId(), request.getAttendanceDate())) {
                throw new BadRequestException(duplicateAttendanceMessage(request));
            }
            throw ex;
        }

        if (request.getStatus() == AttendanceStatus.ABSENT && saved.getStudent().getParent() != null) {
            Notification notification = new Notification();
            notification.setRecipientType(NotificationRecipientType.PARENT);
            notification.setRecipientRefId(saved.getStudent().getParent().getId());
            notification.setStudent(saved.getStudent());
            notification.setType(NotificationType.ABSENCE);
            notification.setTitle("Thông báo vắng học");
            notification.setContent("Học viên " + saved.getStudent().getFullName() + " vắng buổi học ngày "
                    + saved.getAttendanceDate() + ".");
            notification.setRelatedEntityType("attendance");
            notification.setRelatedEntityId(saved.getId());
            notificationRepository.save(notification);
        }

        return modelMapper.map(saved, AttendanceResponse.class);
    }

    @Transactional(readOnly = true)
    public Page<AttendanceResponse> findByClassId(Integer classId, Pageable pageable) {
        courseClassService.getCourseClass(classId);
        return attendanceRepository.findByCourseClassId(classId, pageable).map(attendance -> modelMapper.map(attendance, AttendanceResponse.class));
    }

    @Transactional(readOnly = true)
    public Page<AttendanceResponse> findByStudentId(Integer studentId, Pageable pageable) {
        studentService.getStudent(studentId);
        return attendanceRepository.findByStudentId(studentId, pageable).map(attendance -> modelMapper.map(attendance, AttendanceResponse.class));
    }

    @Transactional
    public AttendanceResponse update(Integer id, AttendanceUpdateRequest request, String username) throws BadRequestException {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Attendance not found"));
        attendance.setStatus(request.getStatus());
        attendance.setNote(request.getNote());
        User recorder = authService.findActiveUserByUsername(username);
        attendance.setRecordedByUser(recorder);
        return modelMapper.map(attendanceRepository.save(attendance), AttendanceResponse.class);
    }

    @Transactional
    public List<AttendanceResponse> bulkSave(AttendanceBulkRequest request, String username) throws BadRequestException {
        CourseClass courseClass = courseClassService.getCourseClass(request.getCourseClassId());
        validateAttendanceDate(courseClass, request.getAttendanceDate());
        User recorder = authService.findActiveUserByUsername(username);

        List<AttendanceResponse> responses = new ArrayList<>();
        for (StudentAttendanceItem item : request.getStudents()) {
            Student student = studentService.getStudent(item.getStudentId());
            enrollmentService.getEnrollment(student.getId(), courseClass.getId());

            Optional<Attendance> existingOpt = attendanceRepository.findByCourseClassIdAndStudentIdAndAttendanceDate(
                    courseClass.getId(), student.getId(), request.getAttendanceDate());

            Attendance attendance;
            if (existingOpt.isPresent()) {
                attendance = existingOpt.get();
                attendance.setStatus(item.getStatus());
                attendance.setNote(item.getNote());
                attendance.setRecordedByUser(recorder);
            } else {
                attendance = new Attendance();
                attendance.setStudent(student);
                attendance.setCourseClass(courseClass);
                attendance.setAttendanceDate(request.getAttendanceDate());
                attendance.setStatus(item.getStatus());
                attendance.setNote(item.getNote());
                attendance.setRecordedByUser(recorder);
            }
            Attendance saved = attendanceRepository.save(attendance);

            if (item.getStatus() == AttendanceStatus.ABSENT && saved.getStudent().getParent() != null) {
                Notification notification = new Notification();
                notification.setRecipientType(NotificationRecipientType.PARENT);
                notification.setRecipientRefId(saved.getStudent().getParent().getId());
                notification.setStudent(saved.getStudent());
                notification.setType(NotificationType.ABSENCE);
                notification.setTitle("Thông báo vắng học");
                notification.setContent("Học viên " + saved.getStudent().getFullName() + " vắng buổi học ngày "
                        + saved.getAttendanceDate() + ".");
                notification.setRelatedEntityType("attendance");
                notification.setRelatedEntityId(saved.getId());
                notificationRepository.save(notification);
            }
            responses.add(modelMapper.map(saved, AttendanceResponse.class));
        }
        return responses;
    }

    private void validateAttendanceDate(CourseClass courseClass, LocalDate attendanceDate) throws BadRequestException {
        if (attendanceDate.isBefore(courseClass.getStartDate())) {
            throw new BadRequestException("Attendance date must not be before class start date");
        }
        if (courseClass.getEndDate() != null && attendanceDate.isAfter(courseClass.getEndDate())) {
            throw new BadRequestException("Attendance date must not be after class end date");
        }
        if (courseClass.getScheduleSlot() != null
                && !matchesScheduledWeekday(attendanceDate, Integer.valueOf(courseClass.getScheduleSlot().getWeekday()))) {
            throw new BadRequestException("Attendance date does not match the class schedule");
        }
    }

    private boolean matchesScheduledWeekday(LocalDate attendanceDate, Integer scheduledWeekday) {
        if (scheduledWeekday == null) {
            return true;
        }

        int isoWeekday = attendanceDate.getDayOfWeek().getValue();
        // Use VN-style numbering consistently (Mon=2, Sun=8)
        int vnStyleWeekday = isoWeekday == 7 ? 8 : isoWeekday + 1;
        return scheduledWeekday == vnStyleWeekday;
    }

    private String duplicateAttendanceMessage(AttendanceCreateRequest request) {
        return "Attendance already exists for student " + request.getStudentId()
                + " in class " + request.getCourseClassId()
                + " on " + request.getAttendanceDate();
    }

}
