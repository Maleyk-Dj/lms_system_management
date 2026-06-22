package com.lms.lms_system_management.controller.teacher;

import com.lms.lms_system_management.TestcontainersConfiguration;
import com.lms.lms_system_management.dao.CourseRepository;
import com.lms.lms_system_management.dao.ScheduleRepository;
import com.lms.lms_system_management.dao.TeacherRepository;
import com.lms.lms_system_management.model.TeacherEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
@Sql("/sql/insert-teacher.sql")
@Sql(value = "/sql/clean.sql",executionPhase =  Sql.ExecutionPhase.AFTER_TEST_METHOD)
class DeleteTeacherEntityControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private TeacherRepository teacherRepository;

    private Long teacherId;

    @Test
    void deleteTeacher_shouldReturn204AndRemoveFromDb() {
        ResponseEntity<Void> response = testRestTemplate.exchange(
                "/api/teachers/{id}",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class,
                1L
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(teacherRepository.findById(1L)).isEmpty();
    }

    @Test
    void deleteTeacher_whenNotExists_shouldReturn404() {
        ResponseEntity<Void> response = testRestTemplate.exchange(
                "/api/teachers/{id}",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class,
                99999L
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
