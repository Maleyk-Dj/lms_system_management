package com.lms.lms_system_management.controller.teacher;

import com.lms.lms_system_management.dao.TeacherRepository;
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
public class PatchTeacherControllerTest {
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
    void getTeacherById_shouldReturn200AndCorrectBody() {
        ResponseEntity<TeacherResponse> response = testRestTemplate.getForEntity(
                "/api/teachers/{id}",
                TeacherResponse.class,
                teacherId
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        TeacherResponse body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.id()).isEqualTo(teacherId);
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
    void getAllTeachers_shouldReturn200AndNonEmptyList() {
        ResponseEntity<TeacherResponse[]> response = testRestTemplate.getForEntity(
                "/api/teachers",
                TeacherResponse[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        TeacherResponse[] body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.length).isGreaterThan(0);
    }
}
