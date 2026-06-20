package com.yo.yoprj.dto.course;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CourseResponse {

    private int id;

    private String courseCode;

    private String name;

    private String description;

    private BigDecimal tuitionFee = BigDecimal.ZERO;

    private Integer totalSessions = 24;

    private Boolean isActive = true;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
