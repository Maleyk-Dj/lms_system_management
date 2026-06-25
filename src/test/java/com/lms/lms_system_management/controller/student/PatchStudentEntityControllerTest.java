package com.lms.lms_system_management.controller.student;

import com.lms.lms_system_management.controller.BaseIntegrationTest;
import com.lms.lms_system_management.dao.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("/sql/insert-student.sql")
@Sql(value = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class PatchStudentEntityControllerTest extends BaseIntegrationTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void addToGroup_shouldReturn200AndUpdatedGroupId() throws Exception {
        mockMvc.perform(patch("/api/students/{studentId}/groups/{groupId}", 1, 2))
                .andExpect(status().isOk())
                .andExpect(content().json(readJson("student/update-response.json")));

        var student = studentRepository.findById(1L).orElseThrow();
        assertThat(student.getGroupEntity().getId()).isEqualTo(2L);
    }

    @Test
    void addToGroup_whenAlreadyInGroup_shouldReturn409() throws Exception {
        mockMvc.perform(patch("/api/students/{studentId}/groups/{groupId}", 1, 1))
                .andExpect(status().isConflict());
    }

    @Test
    void addToGroup_whenStudentNotExists_shouldReturn404() throws Exception {
        mockMvc.perform(patch("/api/students/{studentId}/groups/{groupId}", 999999, 1))
                .andExpect(status().isNotFound());
    }
}
