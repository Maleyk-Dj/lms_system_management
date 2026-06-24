package com.lms.lms_system_management.controller.group;

import com.lms.lms_system_management.controller.BaseIntegrationTest;
import com.lms.lms_system_management.dao.GroupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("/sql/insert-group.sql")
@Sql(value = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class PutGroupControllerTest extends BaseIntegrationTest {

    @Autowired
    private GroupRepository groupRepository;

    @Test
    void updateGroup_shouldReturn200AndUpdatedBody() throws Exception {
        mockMvc.perform(put("/api/groups/{groupId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("group/update-request.json")))
                .andExpect(status().isOk())
                .andExpect(content().json(readJson("group/update-response.json")));

        var group = groupRepository.findById(1L).orElseThrow();
        assertThat(group.getName()).isEqualTo("Gruppa C");
    }

    @Test
    void updateGroup_whenNameIsBlank_shouldReturn400() throws Exception {
        mockMvc.perform(put("/api/groups/{groupId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateGroup_whenNotExists_shouldReturn404() throws Exception {
        mockMvc.perform(put("/api/groups/{groupId}", 99999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("group/update-request.json")))
                .andExpect(status().isNotFound());
    }
}
