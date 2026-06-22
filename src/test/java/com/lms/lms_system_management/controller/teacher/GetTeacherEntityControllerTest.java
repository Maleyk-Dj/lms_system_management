package com.lms.lms_system_management.controller.teacher;

import com.lms.lms_system_management.TestcontainersConfiguration;
import com.lms.lms_system_management.dto.teacher.TeacherResponse;
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
@Sql("/sql/insert-teacher.sql")
@Sql(value = "/sql/clean.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class GetTeacherEntityControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    private Long teacherId;

    @Test
    void getTeacherById_shouldReturn200AndCorrectBody() {
        ResponseEntity<TeacherResponse> response = testRestTemplate.getForEntity(
                "/api/teachers/{id}",
                TeacherResponse.class,
                1L
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        TeacherResponse body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.id()).isEqualTo(1L);
        assertThat(body.firstName()).isEqualTo("Malika");
        assertThat(body.lastName()).isEqualTo("Djabrailova");
    }

    @Test
    void getTeacherById_whenNotExists_shouldReturn404() {
        ResponseEntity<Void> response = testRestTemplate.getForEntity(
                "/api/teachers/{id}",
                Void.class,
                99999L
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getAllTeachers_shouldReturn200AndNonEmptyContent() {
        ResponseEntity<String> response = testRestTemplate.getForEntity(
                "/api/teachers",
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).contains("Malika");
    }

    @Test
    void getScheduleByTeacher_shouldReturn200AndEmptyList() {
        ResponseEntity<Object[]> response = testRestTemplate.getForEntity(
                "/api/teachers/{id}/schedules",
                Object[].class,
                1L
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Object[] body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.length).isEqualTo(0);
    }

    @Test
    void getScheduleByTeacher_whenNotExists_shouldReturn200AndEmptyList() {
        ResponseEntity<Object[]> response = testRestTemplate.getForEntity(
                "/api/teachers/{id}/schedules",
                Object[].class,
                99999L
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isEqualTo(0);
    }
}
