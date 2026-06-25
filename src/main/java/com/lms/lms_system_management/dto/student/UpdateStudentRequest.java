package com.lms.lms_system_management.dto.student;

public record UpdateStudentRequest(

        String firstName,
        String lastName,
        Long groupId
) {
}
