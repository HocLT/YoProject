package com.yo.yoprj.domain.entity;

import com.yo.yoprj.domain.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "courses")
public class Course extends AuditableEntity {
    @Column(name = "course_code", nullable = false, unique = true, length = 20)
    private String courseCode;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(name = "tuition_fee", nullable = false, precision = 12, scale = 2)
    private BigDecimal tuitionFee = BigDecimal.ZERO;

    @Column(name = "total_sessions", nullable = false)
    private Integer totalSessions = 24;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
