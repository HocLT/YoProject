package com.yo.yoprj.domain.entity;

import com.yo.yoprj.domain.AuditableEntity;
import com.yo.yoprj.domain.enums.Gender;
import com.yo.yoprj.domain.enums.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "students")
public class Student extends AuditableEntity {

    @Column(name = "student_code")
    private String studentCode;
    private String fullName;
    private LocalDate dateOfBirth;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String gradeLevel;
    private String schoolName;
    private String phone;
    private int parentId;
    @Enumerated(EnumType.STRING)
    private Status status;
    private BigDecimal latestScore;
    private String note;

}
