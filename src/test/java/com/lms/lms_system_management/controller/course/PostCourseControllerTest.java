package com.lms.lms_system_management.controller.course;

import com.lms.lms_system_management.controller.BaseIntegrationTest;
import com.lms.lms_system_management.dao.CourseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("/sql/insert-teacher.sql")
@Sql(value = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class PostCourseControllerTest extends BaseIntegrationTest {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    void createCourse_shouldReturn201AndCorrectBody() throws Exception {
        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("course/create-request.json")))
                .andExpect(status().isCreated())
                .andExpect(content().json(readJson("course/create-response.json")));

        var courses = courseRepository.findAll();
        assertThat(courses.size()).isEqualTo(1);
        assertThat(courses.get(0).getName()).isEqualTo("Spring");
        assertThat(courses.get(0).getDescription()).isEqualTo("Kurs po Spring");
    }

    @ParameterizedTest
    @MethodSource("invalidCourseRequests")
    void createCourse_withInvalidBody_shouldReturn400(String requestJson) throws Exception {
        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    static Stream<String> invalidCourseRequests() {
        return Stream.of(
                "{\"name\": \"\", \"description\": \"Kurs po Spring\", \"teacherId\": 1}",
                "{\"name\": \"Spring\", \"description\": \"\", \"teacherId\": 1}",
                "{\"name\": \"Spring\", \"description\": \"Kurs po Spring\", \"teacherId\": null}"
        );
    }

    @Test
    void createCourse_whenTeacherNotExists_shouldReturn404() throws Exception {
        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Spring Boot\", \"description\": \"Desc\", \"teacherId\": 99999}"))
                .andExpect(status().isNotFound());
    }
}
