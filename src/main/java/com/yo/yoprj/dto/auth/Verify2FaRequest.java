package com.yo.yoprj.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record Verify2FaRequest(
        @NotBlank(message = "Temp token is required")
        String tempToken,
        
        @NotBlank(message = "Code is required")
        @Size(min = 6, max = 6, message = "Code must be 6 digits")
        String code
) {
}
