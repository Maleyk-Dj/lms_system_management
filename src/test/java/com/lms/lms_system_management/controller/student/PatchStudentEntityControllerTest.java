package com.lms.lms_system_management.controller.student;

import com.lms.lms_system_management.TestcontainersConfiguration;
import com.lms.lms_system_management.dto.student.StudentResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
@Sql("/sql/insert-student.sql")
@Sql(value = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class PatchStudentEntityControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getStudentById_shouldReturn200AndCorrectBody() {
        ResponseEntity<StudentResponse> response = restTemplate.getForEntity(
                "/api/students/{id}",
                StudentResponse.class,
                1L
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        StudentResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.id()).isEqualTo(1L);
        assertThat(body.firstName()).isEqualTo("Valya");
        assertThat(body.lastName()).isEqualTo("Ivanova");
        assertThat(body.groupId()).isEqualTo(1L);
    }

    @Test
    void getStudentById_whenNotExists_shouldReturn404() {
        ResponseEntity<StudentResponse> response = restTemplate.getForEntity(
                "/api/students/{id}",
                StudentResponse.class,
                999999L
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getAllStudents_shouldReturn200AndNonEmptyContent() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/students",
                String.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).contains("Valya");
    }

    @Test
    void addToGroup_shouldReturn200AndUpdateGroupId() {
        ResponseEntity<StudentResponse> response = restTemplate.exchange(
                "/api/students/{studentId}/groups/{groupId}",
                HttpMethod.PATCH,
                HttpEntity.EMPTY,
                StudentResponse.class,
                1L,
                2L
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        StudentResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.groupId()).isEqualTo(2L);
    }

    @Test
    void addToGroup_whenAlreadyInGroup_shouldReturn409() {
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/students/{studentId}/groups/{groupId}",
                HttpMethod.PATCH,
                HttpEntity.EMPTY,
                Void.class,
                1L,
                1L
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void addToGroup_whenStudentNotExists_shouldReturn404() {
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/students/{studentId}/groups/{groupId}",
                HttpMethod.PATCH,
                HttpEntity.EMPTY,
                Void.class,
                999999L,
                1L
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
