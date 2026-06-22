package com.lms.lms_system_management.dto.schedule;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record UpdateScheduleRequest(
        @NotNull
        Long groupId,

        @NotNull
        Long courseId,

        @NotNull
        LocalDateTime date
) {
}
