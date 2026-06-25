package com.lms.lms_system_management.controller.group;

import com.lms.lms_system_management.controller.BaseIntegrationTest;
import com.lms.lms_system_management.dao.GroupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(value = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class PostGroupControllerTest extends BaseIntegrationTest {

    @Autowired
    private GroupRepository groupRepository;

    @Test
    void createGroup_shouldReturn201AndCorrectBody() throws Exception {
        mockMvc.perform(post("/api/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("group/create-request.json")))
                .andExpect(status().isCreated())
                .andExpect(content().json(readJson("group/create-response.json")));

        var groups = groupRepository.findAll();
        assertThat(groups.size()).isEqualTo(1);
        assertThat(groups.get(0).getName()).isEqualTo("Gruppa B");
    }

    @Test
    void createGroup_whenNameIsBlank_shouldReturn400() throws Exception {
        mockMvc.perform(post("/api/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"\"}"))
                .andExpect(status().isBadRequest());
    }
}
