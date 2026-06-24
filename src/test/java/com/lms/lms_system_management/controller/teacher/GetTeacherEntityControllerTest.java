package com.lms.lms_system_management.controller.teacher;

import com.lms.lms_system_management.controller.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("/sql/insert-teacher.sql")
@Sql(value = "/sql/clean.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class GetTeacherEntityControllerTest extends BaseIntegrationTest {

    @Test
    void getTeacherById_shouldReturn200AndCorrectBody() throws Exception {
        mockMvc.perform(get("/api/teachers/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(readJson("teacher/get-response.json")));
    }

    @Test
    void getTeacherById_whenNotExists_shouldReturn404() throws Exception {
        mockMvc.perform(get("/api/teachers/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllTeachers_shouldReturn200AndNonEmptyContent() throws Exception {
        mockMvc.perform(get("/api/teachers"))
                .andExpect(status().isOk())
                .andExpect(content().json(readJson("teacher/get-all-response.json")));
    }

    @Test
    void getScheduleByTeacher_shouldReturn200AndEmptyList() throws Exception {
        mockMvc.perform(get("/api/teachers/{id}/schedules", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(readJson("teacher/get-empty-response.json")));
    }

    @Test
    void getScheduleByTeacher_whenNotExists_shouldReturn200AndEmptyList() throws Exception {
        mockMvc.perform(get("/api/teachers/{id}/schedules", 999))
                .andExpect(status().isOk())
                .andExpect(content().json(readJson("teacher/get-empty-response.json")));
    }
}
