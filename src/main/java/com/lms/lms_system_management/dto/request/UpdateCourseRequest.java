package com.lms.lms_system_management.dto.request;

public record UpdateCourseRequest(
        String name,
        String description,
        Long teacherId
) {
}
