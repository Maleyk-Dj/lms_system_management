package com.lms.lms_system_management.controller.course;

import com.lms.lms_system_management.controller.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("/sql/insert-course.sql")
@Sql(value = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class GetCourseControllerTest extends BaseIntegrationTest {

    @Test
    void getCourseById_shouldReturn200AndCorrectBody() throws Exception {
        mockMvc.perform(get("/api/courses/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(readJson("course/get-response.json")));
    }

    @Test
    void getCourseById_whenNotExists_shouldReturn404() throws Exception {
        mockMvc.perform(get("/api/courses/{id}", 999999))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllCourses_shouldReturn200AndNonEmptyContent() throws Exception {
        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(content().json(readJson("course/get-all-response.json")));
    }
}
