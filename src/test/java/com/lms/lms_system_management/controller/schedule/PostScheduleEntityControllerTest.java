package com.lms.lms_system_management.controller.schedule;

import com.lms.lms_system_management.TestcontainersConfiguration;
import com.lms.lms_system_management.dto.schedule.NewScheduleRequest;
import com.lms.lms_system_management.dto.schedule.ScheduleResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
@Sql("/sql/insert-schedule-base.sql")
@Sql(value = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class PostScheduleEntityControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void assignCourseTime_shouldReturn201AndCorrectBody() {
        LocalDateTime newDate = LocalDateTime.of(2026, 8, 1, 9, 0);
        NewScheduleRequest request = new NewScheduleRequest(1L, 1L, newDate);

        ResponseEntity<ScheduleResponse> response = restTemplate.postForEntity(
                "/api/schedules",
                request,
                ScheduleResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ScheduleResponse body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.id()).isNotNull();
        assertThat(body.group().id()).isEqualTo(1L);
        assertThat(body.course().id()).isEqualTo(1L);
        assertThat(body.date()).isEqualTo(newDate);
    }

    @ParameterizedTest
    @MethodSource("invalidScheduleRequests")
    void assignCourseTime_whenIsNull_shouldReturn400(NewScheduleRequest request) {
        ResponseEntity<Void> response = restTemplate.postForEntity(
                "/api/schedules",
                request,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    static Stream<NewScheduleRequest> invalidScheduleRequests() {
        return Stream.of(
                new NewScheduleRequest(null, 1L, LocalDateTime.now()),
                new NewScheduleRequest(1L, null, LocalDateTime.now()),
                new NewScheduleRequest(1L, 1L, null)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidSchedule")
    void assignCourseTime_whenNotExists_shouldReturn404(NewScheduleRequest request) {
        ResponseEntity<Void> response = restTemplate.postForEntity(
                "/api/schedules",
                request,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    static Stream<NewScheduleRequest> invalidSchedule() {
        return Stream.of(
                new NewScheduleRequest(99999L, 1L, LocalDateTime.now()),
                new NewScheduleRequest(1L, 99999L, LocalDateTime.now())
        );
    }
}
