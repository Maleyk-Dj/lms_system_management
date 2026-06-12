package com.lms.lms_system_management.dto.response;



import java.time.LocalDateTime;

public record ScheduleResponse(
        Long id,
        GroupResponse group,
        CourseResponse course,
        LocalDateTime date
) {
}
