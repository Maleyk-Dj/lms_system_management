package com.lms.lms_system_management.controller.group;

import com.lms.lms_system_management.controller.BaseIntegrationTest;
import com.lms.lms_system_management.dao.GroupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("/sql/insert-group.sql")
@Sql(value = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class DeleteGroupControllerTest extends BaseIntegrationTest {

    @Autowired
    private GroupRepository groupRepository;

    @Test
    void deleteGroup_shouldReturn204AndRemoveFromDb() throws Exception {
        mockMvc.perform(delete("/api/groups/{groupId}", 1))
                .andExpect(status().isNoContent());

        assertThat(groupRepository.findById(1L)).isEmpty();
    }

    @Test
    void deleteGroup_whenNotExists_shouldReturn404() throws Exception {
        mockMvc.perform(delete("/api/groups/{groupId}", 99999))
                .andExpect(status().isNotFound());
    }
}
