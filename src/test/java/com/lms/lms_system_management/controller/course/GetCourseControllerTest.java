package com.lms.lms_system_management.controller.course;

import com.lms.lms_system_management.TestcontainersConfiguration;
import com.lms.lms_system_management.dto.course.CourseResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
@Sql("/sql/insert-course.sql")
@Sql(value = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class GetCourseControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getCourseById_shouldReturn200AndCorrectBody() {
        ResponseEntity<CourseResponse> response = restTemplate.getForEntity(
                "/api/courses/{id}",
                CourseResponse.class,
                1L
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        CourseResponse body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.id()).isEqualTo(1L);
        assertThat(body.name()).isEqualTo("Java");
        assertThat(body.description()).isEqualTo("Kurs po razrabotke Java");
        assertThat(body.teacher().id()).isEqualTo(1L);
    }

    @Test
    void getCourseById_whenNotExists_shouldReturn404() {
        ResponseEntity<Void> response = restTemplate.getForEntity(
                "/api/courses/{id}",
                Void.class,
                999999L
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getAllCourses_shouldReturn200AndNonEmptyContent() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/courses",
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).contains("Java");
    }
}
