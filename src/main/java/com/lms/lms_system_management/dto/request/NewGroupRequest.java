package com.lms.lms_system_management.dto.request;

import jakarta.validation.constraints.NotBlank;

public record NewGroupRequest (
        @NotBlank
        String name
) {
}
