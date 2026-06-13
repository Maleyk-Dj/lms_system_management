package com.lms.lms_system_management.dto.schedule;

import com.lms.lms_system_management.dto.course.CourseResponse;
import com.lms.lms_system_management.dto.group.GroupResponse;

import java.time.LocalDateTime;

public record ScheduleResponse(

        Long id,
        GroupResponse group,
        CourseResponse course,
        LocalDateTime date
) {
}
