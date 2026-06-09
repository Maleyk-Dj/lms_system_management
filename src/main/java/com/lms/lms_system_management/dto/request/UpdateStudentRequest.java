package com.lms.lms_system_management.dto.request;

public record UpdateStudentRequest(
        String firstName,
        String lastName,
        Long groupId
) {
}
