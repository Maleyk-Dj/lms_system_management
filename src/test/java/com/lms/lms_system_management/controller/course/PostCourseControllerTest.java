package com.lms.lms_system_management.controller.course;

import com.lms.lms_system_management.TestcontainersConfiguration;
import com.lms.lms_system_management.dto.course.NewCourseRequest;
import com.lms.lms_system_management.dto.course.CourseResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
@Sql("/sql/insert-teacher.sql")
@Sql(value = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class PostCourseControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createCourse_shouldReturn201AndCorrectBody() {
        NewCourseRequest request = new NewCourseRequest("Spring", "Kurs po Spring", 1L);
        ResponseEntity<CourseResponse> response = restTemplate.postForEntity(
                "/api/courses",
                request,
                CourseResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        CourseResponse body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.id()).isNotNull();
        assertThat(body.name()).isEqualTo("Spring");
        assertThat(body.description()).isEqualTo("Kurs po Spring");
        assertThat(body.teacher()).isNotNull();
        assertThat(body.teacher().id()).isNotNull();
    }

    @ParameterizedTest
    @MethodSource("invalidCourseRequests")
    void createCourse_withInvalidBody_shouldReturn400(NewCourseRequest request) {
        ResponseEntity<Void> response = restTemplate.postForEntity(
                "/api/courses",
                request,
                Void.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    static Stream<NewCourseRequest> invalidCourseRequests() {
        return Stream.of(
                new NewCourseRequest("", "Kurs po Spring", 1L),
                new NewCourseRequest("Spring", "", 1L),
                new NewCourseRequest("Spring", "Kurs po Spring", null)
        );
    }

    @Test
    void createCourse_whenTeacherNotExists_shouldReturn404() {
        NewCourseRequest request = new NewCourseRequest("Spring Boot", "Kurs po Spring", 99999L);

        ResponseEntity<Void> response = restTemplate.postForEntity(
                "/api/courses",
                request,
                Void.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
