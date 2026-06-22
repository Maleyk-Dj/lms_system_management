package com.lms.lms_system_management.controller.schedule;

import com.lms.lms_system_management.TestcontainersConfiguration;
import com.lms.lms_system_management.dto.schedule.UpdateScheduleRequest;
import com.lms.lms_system_management.dto.schedule.ScheduleResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
@Sql("/sql/insert-schedule.sql")
@Sql(value = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class PutScheduleEntityControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final LocalDateTime SCHEDULE_DATE = LocalDateTime.of(2026, 7, 1, 10, 0);

    @Test
    void updateSchedule_shouldReturn200AndUpdatedBody() {
        LocalDateTime updatedDate = LocalDateTime.of(2026, 9, 15, 14, 0);
        UpdateScheduleRequest request = new UpdateScheduleRequest(1L, 1L, updatedDate);

        ResponseEntity<ScheduleResponse> response = restTemplate.exchange(
                "/api/schedules/{scheduleId}",
                HttpMethod.PUT,
                new HttpEntity<>(request),
                ScheduleResponse.class,
                1L
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ScheduleResponse body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.id()).isEqualTo(1L);
        assertThat(body.group().id()).isEqualTo(1L);
        assertThat(body.course().id()).isEqualTo(1L);
        assertThat(body.date()).isEqualTo(updatedDate);
    }

    @Test
    void updateSchedule_whenGroupIdIsNull_shouldReturn400() {
        UpdateScheduleRequest request = new UpdateScheduleRequest(null, 1L, SCHEDULE_DATE);

        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/schedules/{scheduleId}",
                HttpMethod.PUT,
                new HttpEntity<>(request),
                Void.class,
                1L
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void updateSchedule_whenNotExists_shouldReturn404() {
        UpdateScheduleRequest request = new UpdateScheduleRequest(1L, 1L, SCHEDULE_DATE);

        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/schedules/{scheduleId}",
                HttpMethod.PUT,
                new HttpEntity<>(request),
                Void.class,
                99999L
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
