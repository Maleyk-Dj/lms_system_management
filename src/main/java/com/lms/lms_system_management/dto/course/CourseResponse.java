package com.lms.lms_system_management.dto.course;

import com.lms.lms_system_management.dto.teacher.TeacherResponse;

public record CourseResponse(

        Long id,
        String name,
        String description,
        TeacherResponse teacher
) {
}
