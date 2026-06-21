package com.yo.yoprj.dto.teacher;

import com.yo.yoprj.domain.enums.TeacherRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherUpsertRequest {
    @NotBlank
    @Size(max = 20)
    private String teacherCode;

    @NotBlank
    @Size(max = 100)
    private String fullName;

    @NotBlank
    @Size(max = 20)
    private String phone;

    @Size(max = 100)
    private String email;

    @NotNull
    private TeacherRole teacherRole;

    @Size(max = 255)
    private String cccdImageUrl;

    private Boolean isActive = true;
}
