package com.lms.lms_system_management.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

/**
 * тело ошибки Rest Api
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        int status,
        String message,
        String path,
        LocalDateTime timestamp

) {
}

