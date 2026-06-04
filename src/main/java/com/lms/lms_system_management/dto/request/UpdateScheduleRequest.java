package com.lms.lms_system_management.dto.request;

import java.time.LocalDateTime;

public record UpdateScheduleRequest(
        Long groupId,
        Long courseId,
        LocalDateTime date
) {
}
