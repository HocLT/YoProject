package com.yo.yoprj.config;

import com.yo.yoprj.domain.entity.CourseClass;
import com.yo.yoprj.domain.entity.Student;
import com.yo.yoprj.dto.courseclass.CourseClassResponse;
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

        return modelMapper;
    }

}
