package com.lms.lms_system_management.controller.student;

import com.lms.lms_system_management.TestcontainersConfiguration;
import com.lms.lms_system_management.dao.StudentRepository;
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
class DeleteStudentEntityControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void deleteStudent_shouldReturn204AndRemoveFromDb() {
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/students/{id}",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class,
                1L
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(studentRepository.findById(1L)).isEmpty();
    }

    @Test
    void deleteStudent_whenNotExists_shouldReturn404() {
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/students/{id}",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class,
                999999L
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
