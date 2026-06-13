package com.lms.lms_system_management.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateCourseRequest(

        @NotBlank
        String name,

        @NotBlank
        String description,

        @NotNull
        Long teacherId
) {
}
