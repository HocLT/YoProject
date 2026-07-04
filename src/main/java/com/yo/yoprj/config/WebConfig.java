package com.yo.yoprj.config;

import com.yo.yoprj.domain.entity.*;
import com.yo.yoprj.dto.attendance.AttendanceResponse;
import com.yo.yoprj.dto.courseclass.CourseClassResponse;
import com.yo.yoprj.dto.enrollment.EnrollmentResponse;
import com.yo.yoprj.dto.learning.LearningResultResponse;
import com.yo.yoprj.dto.student.StudentResponse;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Bean
    public ModelMapper modelMapper() {
        // Tạo object và cấu hình
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setAmbiguityIgnored(false);

        // Converter for CourseClass -> CourseClassResponse
        Converter<CourseClass, CourseClassResponse> courseClassConverter = ctx -> {
            CourseClass item = ctx.getSource();
            return new CourseClassResponse(
                    item.getId(),
                    item.getClassCode(),
                    item.getName(),
                    item.getCourse().getId(),
                    item.getCourse().getName(),
                    item.getRoom().getId(),
                    item.getRoom().getName(),
                    item.getScheduleSlot().getId(),
                    item.getScheduleSlot().getSlotCode() + " - T" + item.getScheduleSlot().getWeekday(),
                    item.getMainTeacher().getId(),
                    item.getMainTeacher().getFullName(),
                    item.getAssistantTeacher() != null ? item.getAssistantTeacher().getId() : null,
                    item.getAssistantTeacher() != null ? item.getAssistantTeacher().getFullName() : null,
                    item.getStartDate(),
                    item.getEndDate(),
                    item.getMaxStudents(),
                    item.getTuitionFee(),
                    item.getStatus().name(),
                    item.getCreatedAt(),
                    item.getUpdatedAt()
            );
        };
        modelMapper.createTypeMap(CourseClass.class, CourseClassResponse.class).setConverter(courseClassConverter);

        // Converter for Student -> StudentResponse
        Converter<Student, StudentResponse> studentConverter = ctx -> {
            Student item = ctx.getSource();
            return new StudentResponse(
                    item.getId(),
                    item.getStudentCode(),
                    item.getFullName(),
                    item.getDateOfBirth(),
                    item.getGender().name(),
                    item.getGradeLevel(),
                    item.getSchoolName(),
                    item.getPhone(),
                    item.getParent() != null ? item.getParent().getId() : null,
                    item.getParent() != null ? item.getParent().getFullName() : null,
                    item.getStatus().name(),
                    item.getLatestScore(),
                    item.getNote(),
                    item.getCreatedAt(),
                    item.getUpdatedAt()
            );
        };
        modelMapper.createTypeMap(Student.class, StudentResponse.class).setConverter(studentConverter);

        // Converter for Enrollment -> EnrollmentResponse
        Converter<Enrollment, EnrollmentResponse> enrollmentConverter = ctx -> {
            Enrollment item = ctx.getSource();
            return new EnrollmentResponse(
                    item.getId(),
                    item.getStudent().getId(),
                    item.getStudent().getFullName(),
                    item.getCourseClass().getId(),
                    item.getCourseClass().getName(),
                    item.getEnrolledAt(),
                    item.getStatus().name(),
                    item.getNote(),
                    item.getCreatedAt(),
                    item.getUpdatedAt()
            );
        };
        modelMapper.createTypeMap(Enrollment.class, EnrollmentResponse.class).setConverter(enrollmentConverter);

        // Converter for Attendance -> AttendanceResponse
        Converter<Attendance, AttendanceResponse> attendanceConverter = ctx -> {
            Attendance item = ctx.getSource();
            return new AttendanceResponse(
                    item.getId(),
                    item.getCourseClass().getId(),
                    item.getCourseClass().getName(),
                    item.getStudent().getId(),
                    item.getStudent().getFullName(),
                    item.getAttendanceDate(),
                    item.getStatus().name(),
                    item.getNote(),
                    item.getRecordedByUser() != null ? item.getRecordedByUser().getId() : null,
                    item.getRecordedByUser() != null ? item.getRecordedByUser().getUsername() : null,
                    item.getCreatedAt(),
                    item.getUpdatedAt()
            );
        };
        modelMapper.createTypeMap(Attendance.class, AttendanceResponse.class).setConverter(attendanceConverter);

        // Converter for LearningResult -> LearningResultResponse
        Converter<LearningResult, LearningResultResponse> learningResultConverter = ctx -> {
            LearningResult item = ctx.getSource();
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
                    item.getUpdatedAt()
            );
        };
        modelMapper.createTypeMap(LearningResult.class, LearningResultResponse.class).setConverter(learningResultConverter);

        // Converter for Attendance -> AttendanceResponse
//        Converter<TuitionInvoice, InvoiceResponse> invoiceConverter = ctx -> {
//            TuitionInvoice item = ctx.getSource();
//            return new InvoiceResponse(
//                    item.getId(),
//                    item.getInvoiceCode(),
//                    item.getStudent().getId(),
//                    item.getStudent().getFullName(),
//                    item.getCourseClass().getId(),
//                    item.getCourseClass().getName(),
//                    item.getBillingMonth(),
//                    item.getOriginalAmount(),
//                    item.getDiscountAmount(),
//                    item.getFinalAmount(),
//                    item.getAmountPaid(),
//                    item.getBalanceAmount(),
//                    item.getStatus(),
//                    item.getPromotion().getId(),
//                    item.getPromotion().getName(),
//                    item.getDueDate(),
//                    item.getnote(),
//                    item.getCreatedAt(),
//                    item.getUpdatedAt()
//            );
//        };
//        modelMapper.createTypeMap(TuitionInvoice.class, InvoiceResponse.class).setConverter(invoiceConverter);


        return modelMapper;
    }

}
