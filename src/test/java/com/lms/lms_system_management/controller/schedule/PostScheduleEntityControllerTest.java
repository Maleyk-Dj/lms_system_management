package com.lms.lms_system_management.controller.schedule;

import com.lms.lms_system_management.controller.BaseIntegrationTest;
import com.lms.lms_system_management.dao.ScheduleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("/sql/insert-schedule-base.sql")
@Sql(value = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class PostScheduleEntityControllerTest extends BaseIntegrationTest {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Test
    void assignCourseTime_shouldReturn201AndCorrectBody() throws Exception {
        mockMvc.perform(post("/api/schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("schedule/create-request.json")))
                .andExpect(status().isCreated())
                .andExpect(content().json(readJson("schedule/create-response.json")));

        var schedules = scheduleRepository.findAll();
        assertThat(schedules.size()).isEqualTo(1);
        assertThat(schedules.get(0).getGroupEntity().getId()).isEqualTo(1L);
        assertThat(schedules.get(0).getCourseEntity().getId()).isEqualTo(1L);
    }

    @ParameterizedTest
    @MethodSource("invalidScheduleRequests")
    void assignCourseTime_whenIsNull_shouldReturn400(String requestJson) throws Exception {
        mockMvc.perform(post("/api/schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    static Stream<String> invalidScheduleRequests() {
        return Stream.of(
                "{\"groupId\": null, \"courseId\": 1, \"date\": \"2026-08-01T09:00:00\"}",
                "{\"groupId\": 1, \"courseId\": null, \"date\": \"2026-08-01T09:00:00\"}",
                "{\"groupId\": 1, \"courseId\": 1, \"date\": null}"
        );
    }

    @ParameterizedTest
    @MethodSource("notFoundScheduleRequests")
    void assignCourseTime_whenNotExists_shouldReturn404(String requestJson) throws Exception {
        mockMvc.perform(post("/api/schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound());
    }

    static Stream<String> notFoundScheduleRequests() {
        return Stream.of(
                "{\"groupId\": 99999, \"courseId\": 1, \"date\": \"2026-08-01T09:00:00\"}",
                "{\"groupId\": 1, \"courseId\": 99999, \"date\": \"2026-08-01T09:00:00\"}"
        );
    }
}
