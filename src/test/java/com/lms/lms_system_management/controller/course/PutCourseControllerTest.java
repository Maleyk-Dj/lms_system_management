package com.lms.lms_system_management.controller.course;

import com.lms.lms_system_management.TestcontainersConfiguration;
import com.lms.lms_system_management.dto.course.UpdateCourseRequest;
import com.lms.lms_system_management.dto.course.CourseResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
@Sql("/sql/insert-course-put.sql")
@Sql(value = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PutCourseControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void updateCourse_shouldReturn200AndUpdatedBody() {
        UpdateCourseRequest request = new UpdateCourseRequest(
                "Kotlin", "Kurs po Kotlin", 2L);

        ResponseEntity<CourseResponse> response = restTemplate.exchange(
                "/api/courses/{id}",
                HttpMethod.PUT,
                new HttpEntity<>(request),
                CourseResponse.class,
                1L
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        CourseResponse body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.id()).isEqualTo(1L);
        assertThat(body.name()).isEqualTo("Kotlin");
        assertThat(body.description()).isEqualTo("Kurs po Kotlin");
        assertThat(body.teacher().id()).isEqualTo(2L);
    }

    @ParameterizedTest
    @MethodSource("invalidUpdateCourseRequests")
    void updateCourse_withInvalidBody_shouldReturn400(UpdateCourseRequest request) {
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/courses/{id}",
                HttpMethod.PUT,
                new HttpEntity<>(request),
                Void.class,
                88888L
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    static Stream<UpdateCourseRequest> invalidUpdateCourseRequests() {
        return Stream.of(
                new UpdateCourseRequest("", "Kurs po Spring", 99999L),
                new UpdateCourseRequest("Spring Boot", "", 1L),
                new UpdateCourseRequest("Spring Boot", "Kurs po Spring", null)
        );
    }

    @ParameterizedTest
    @MethodSource("notFoundUpdateCourseArgs")
    void updateCourse_whenNotExists_shouldReturn404(UpdateCourseRequest request, Long courseId) {
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/courses/{id}",
                HttpMethod.PUT,
                new HttpEntity<>(request),
                Void.class,
                courseId
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    static Stream<Arguments> notFoundUpdateCourseArgs() {
        return Stream.of(
                Arguments.of(new UpdateCourseRequest("Spring", "Desc", 99999L), 88888L),
                Arguments.of(new UpdateCourseRequest("Spring", "Desc", 1L), 99999L)
        );
    }
}
