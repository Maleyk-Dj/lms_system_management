package com.lms.lms_system_management.controller.group;

import com.lms.lms_system_management.controller.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("/sql/insert-group.sql")
@Sql(value = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class GetGroupControllerTest extends BaseIntegrationTest {

    @Test
    void getGroupById_shouldReturn200AndCorrectBody() throws Exception {
        mockMvc.perform(get("/api/groups/{groupId}", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(readJson("group/get-response.json")));
    }

    @Test
    void getGroupById_whenNotExists_shouldReturn404() throws Exception {
        mockMvc.perform(get("/api/groups/{groupId}", 99999))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllGroups_shouldReturn200AndNonEmptyContent() throws Exception {
        mockMvc.perform(get("/api/groups"))
                .andExpect(status().isOk())
                .andExpect(content().json(readJson("group/get-all-response.json")));
    }
}
