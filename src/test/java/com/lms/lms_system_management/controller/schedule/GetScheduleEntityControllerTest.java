package com.lms.lms_system_management.controller.schedule;

import com.lms.lms_system_management.controller.BaseIntegrationTest;
import com.lms.lms_system_management.dao.GroupRepository;
import com.lms.lms_system_management.model.GroupEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("/sql/insert-schedule.sql")
@Sql(value = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class GetScheduleEntityControllerTest extends BaseIntegrationTest {

    @Autowired
    private GroupRepository groupRepository;

    @Test
    void getScheduleByGroup_shouldReturn200AndNonEmptyBody() throws Exception {
        mockMvc.perform(get("/api/schedules").param("groupId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(readJson("schedule/get-all-response.json")));
    }

    @Test
    void getScheduleByGroup_whenUnknownGroup_shouldReturn200AndEmptyContent() throws Exception {
        mockMvc.perform(get("/api/schedules").param("groupId", "99999"))
                .andExpect(status().isOk())
                .andExpect(content().json(readJson("schedule/get-empty-response.json")));
    }

    @Test
    void getScheduleByGroup_whenNoSchedules_shouldReturn200AndEmptyContent() throws Exception {
        GroupEntity emptyGroup = new GroupEntity();
        emptyGroup.setName("Gruppa B");
        groupRepository.save(emptyGroup);

        mockMvc.perform(get("/api/schedules").param("groupId", String.valueOf(emptyGroup.getId())))
                .andExpect(status().isOk())
                .andExpect(content().json(readJson("schedule/get-empty-response.json")));
    }
}
