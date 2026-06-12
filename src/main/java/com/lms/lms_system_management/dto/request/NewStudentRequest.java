package com.lms.lms_system_management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NewStudentRequest(
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NotNull
        Long groupId
) {
}
