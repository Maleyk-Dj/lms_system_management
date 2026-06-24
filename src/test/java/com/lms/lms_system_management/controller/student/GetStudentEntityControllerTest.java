package com.lms.lms_system_management.controller.student;

import com.lms.lms_system_management.controller.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("/sql/insert-student.sql")
@Sql(value = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class GetStudentEntityControllerTest extends BaseIntegrationTest {

    @Test
    void getStudentById_shouldReturn200AndCorrectBody() throws Exception {
        mockMvc.perform(get("/api/students/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(readJson("student/get-response.json")));
    }

    @Test
    void getStudentById_whenNotExists_shouldReturn404() throws Exception {
        mockMvc.perform(get("/api/students/{id}", 999999))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllStudents_shouldReturn200AndNonEmptyContent() throws Exception {
        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(content().json(readJson("student/get-all-response.json")));
    }
}
