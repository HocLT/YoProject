package com.yo.yoprj.dto.attendance;

import com.yo.yoprj.domain.enums.AttendanceStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceUpdateRequest {
    @NotNull
    AttendanceStatus status;

    @Size(max = 255) String note;
}
