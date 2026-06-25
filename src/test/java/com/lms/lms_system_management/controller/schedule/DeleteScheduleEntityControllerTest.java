package com.lms.lms_system_management.controller.schedule;

import com.lms.lms_system_management.controller.BaseIntegrationTest;
import com.lms.lms_system_management.dao.ScheduleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("/sql/insert-schedule.sql")
@Sql(value = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class DeleteScheduleEntityControllerTest extends BaseIntegrationTest {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Test
    void deleteSchedule_shouldReturn204AndRemoveFromDb() throws Exception {
        mockMvc.perform(delete("/api/schedules/{scheduleId}", 1))
                .andExpect(status().isNoContent());

        assertThat(scheduleRepository.findById(1L)).isEmpty();
    }

    @Test
    void deleteSchedule_whenNotExists_shouldReturn404() throws Exception {
        mockMvc.perform(delete("/api/schedules/{scheduleId}", 99999))
                .andExpect(status().isNotFound());
    }
}
