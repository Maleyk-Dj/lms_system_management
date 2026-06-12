package com.lms.lms_system_management.dto.response;

public record StudentResponse(
        Long id,
        String firstName,
        String lastName,
        Long groupId
) {
}
