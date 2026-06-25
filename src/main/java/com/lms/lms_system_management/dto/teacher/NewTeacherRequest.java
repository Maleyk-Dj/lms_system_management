package com.lms.lms_system_management.dto.teacher;

import jakarta.validation.constraints.NotBlank;

public record NewTeacherRequest(

        @NotBlank
        String firstName,

        @NotBlank
        String lastName
) {
}
