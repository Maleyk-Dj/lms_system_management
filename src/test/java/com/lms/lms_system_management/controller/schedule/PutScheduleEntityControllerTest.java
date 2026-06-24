package com.lms.lms_system_management.controller.schedule;

import com.lms.lms_system_management.controller.BaseIntegrationTest;
import com.lms.lms_system_management.dao.ScheduleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("/sql/insert-schedule.sql")
@Sql(value = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class PutScheduleEntityControllerTest extends BaseIntegrationTest {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Test
    void updateSchedule_shouldReturn200AndUpdatedBody() throws Exception {
        mockMvc.perform(put("/api/schedules/{scheduleId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("schedule/update-request.json")))
                .andExpect(status().isOk())
                .andExpect(content().json(readJson("schedule/update-response.json")));

        var schedule = scheduleRepository.findById(1L).orElseThrow();
        assertThat(schedule.getGroupEntity().getId()).isEqualTo(1L);
        assertThat(schedule.getCourseEntity().getId()).isEqualTo(1L);
        assertThat(schedule.getDateClass().toString()).isEqualTo("2026-09-15T14:00");
    }

    @Test
    void updateSchedule_whenGroupIdIsNull_shouldReturn400() throws Exception {
        mockMvc.perform(put("/api/schedules/{scheduleId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"groupId\": null, \"courseId\": 1, \"date\": \"2026-09-15T14:00:00\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateSchedule_whenNotExists_shouldReturn404() throws Exception {
        mockMvc.perform(put("/api/schedules/{scheduleId}", 99999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("schedule/update-request.json")))
                .andExpect(status().isNotFound());
    }
}
