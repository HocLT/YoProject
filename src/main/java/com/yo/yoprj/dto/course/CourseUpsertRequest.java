package com.yo.yoprj.dto.course;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CourseUpsertRequest {

    private String courseCode;

    private String name;

    private String description;

    private BigDecimal tuitionFee = BigDecimal.ZERO;

    private Integer totalSessions = 24;

    private Boolean isActive = true;

}
