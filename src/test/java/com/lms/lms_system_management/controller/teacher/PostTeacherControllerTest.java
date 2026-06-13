package com.lms.lms_system_management.controller.teacher;

import com.lms.lms_system_management.dao.TeacherRepository;
import com.lms.lms_system_management.dto.teacher.NewTeacherRequest;
import com.lms.lms_system_management.dto.teacher.TeacherResponse;
import com.lms.lms_system_management.model.Teacher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.utility.TestcontainersConfiguration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
public class PostTeacherControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private TeacherRepository teacherRepository;

    private Long teacherId;

    @BeforeEach
    public void setup() {
        Teacher teacher = Teacher.builder()
                .firstName("Malika")
                .lastName("Djabrailova")
                .build();
        teacherRepository.save(teacher);
        teacherId = teacher.getId();
    }

    @AfterEach
    public void tearDown() {
        teacherRepository.deleteAll();
    }
    @Test
    void createTeacher_shouldReturn201AndCorrectBody() {
        NewTeacherRequest request = new NewTeacherRequest("Oleg", "Smirnov");

        ResponseEntity<TeacherResponse> response = testRestTemplate.postForEntity(
                "/api/teachers",
                request,
                TeacherResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        TeacherResponse body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.id()).isNotNull();
        assertThat(body.firstName()).isEqualTo("Oleg");
        assertThat(body.lastName()).isEqualTo("Smirnov");
    }

    @Test
    void createTeacher_whenFirstNameIsBlanck_shouldReturn400() {
        NewTeacherRequest request = new NewTeacherRequest("", "Smirnov");

        ResponseEntity<Void> response = testRestTemplate.postForEntity(
                "/api/teachers",
                request,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createTeacher_whenLastNameIsBlanck_shouldReturn400() {
        NewTeacherRequest request = new NewTeacherRequest("Oleg", "");

        ResponseEntity<Void> response = testRestTemplate.postForEntity(
                "/api/teachers",
                request,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
