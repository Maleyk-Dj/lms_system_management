package com.lms.lms_system_management.dto.schedule;

import java.time.LocalDateTime;

public record ScheduleFilter(
        Long groupId,
        LocalDateTime dateFrom,
        LocalDateTime dateTo
) {
}
