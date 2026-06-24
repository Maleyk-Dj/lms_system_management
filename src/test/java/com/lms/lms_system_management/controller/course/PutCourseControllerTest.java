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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("/sql/insert-course-put.sql")
@Sql(value = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class PutCourseControllerTest extends BaseIntegrationTest {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    void updateCourse_shouldReturn200AndUpdatedBody() throws Exception {
        mockMvc.perform(put("/api/courses/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("course/update-request.json")))
                .andExpect(status().isOk())
                .andExpect(content().json(readJson("course/update-response.json")));

        var course = courseRepository.findById(1L).orElseThrow();
        assertThat(course.getName()).isEqualTo("Kotlin");
        assertThat(course.getDescription()).isEqualTo("Kurs po Kotlin");
        assertThat(course.getTeacherEntity().getId()).isEqualTo(2L);
    }

    @ParameterizedTest
    @MethodSource("invalidUpdateCourseRequests")
    void updateCourse_withInvalidBody_shouldReturn400(String requestJson) throws Exception {
        mockMvc.perform(put("/api/courses/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    static Stream<String> invalidUpdateCourseRequests() {
        return Stream.of(
                "{\"name\": \"\", \"description\": \"Kurs po Spring\", \"teacherId\": 1}",
                "{\"name\": \"Spring Boot\", \"description\": \"\", \"teacherId\": 1}",
                "{\"name\": \"Spring Boot\", \"description\": \"Kurs po Spring\", \"teacherId\": null}"
        );
    }

    @Test
    void updateCourse_whenCourseNotExists_shouldReturn404() throws Exception {
        mockMvc.perform(put("/api/courses/{id}", 99999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("course/update-request.json")))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateCourse_whenTeacherNotExists_shouldReturn404() throws Exception {
        mockMvc.perform(put("/api/courses/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Spring\", \"description\": \"Desc\", \"teacherId\": 99999}"))
                .andExpect(status().isNotFound());
    }
}
