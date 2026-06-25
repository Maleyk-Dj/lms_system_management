package com.lms.lms_system_management.controller.student;

import com.lms.lms_system_management.controller.BaseIntegrationTest;
import com.lms.lms_system_management.dao.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("/sql/insert-student.sql")
@Sql(value = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class DeleteStudentEntityControllerTest extends BaseIntegrationTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void deleteStudent_shouldReturn204AndRemoveFromDb() throws Exception {
        mockMvc.perform(delete("/api/students/{id}", 1))
                .andExpect(status().isNoContent());

        assertThat(studentRepository.findById(1L)).isEmpty();
    }

    @Test
    void deleteStudent_whenNotExists_shouldReturn404() throws Exception {
        mockMvc.perform(delete("/api/students/{id}", 999999))
                .andExpect(status().isNotFound());
    }
}
