package com.yo.yoprj.dto.student;

import com.yo.yoprj.domain.enums.Gender;
import com.yo.yoprj.domain.enums.Status;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class StudentUpsertRequest {
    @NotBlank
    @Pattern(regexp = "^SV\\d{5}$")
    private String studentCode;
    @NotBlank
    private String fullName;
    private LocalDate dateOfBirth;
    private Gender gender;
    @Size(min = 1, max = 20)
    private String gradeLevel;
    private String schoolName;
    private String phone;
    @Min(value = 1)
    private int parentId;
    private Status status;
    private BigDecimal latestScore;
    private String note;
}
