package com.lms.lms_system_management.dto.response;

public record CourseResponse(
        Long id,
        String name,
        String description,
        TeacherResponse teacher
) {
}
