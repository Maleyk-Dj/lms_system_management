package com.lms.lms_system_management.controller.teacher;

import com.lms.lms_system_management.TestcontainersConfiguration;
import com.lms.lms_system_management.dao.TeacherRepository;
import com.lms.lms_system_management.dto.teacher.UpdateTeacherRequest;
import com.lms.lms_system_management.dto.teacher.TeacherResponse;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
class PatchTeacherEntityControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private TeacherRepository teacherRepository;

    private Long teacherId;

    @BeforeEach
    public void setup() {
        TeacherEntity teacherEntity = TeacherEntity.builder()
                .firstName("Malika")
                .lastName("Djabrailova")
                .build();
        teacherRepository.save(teacherEntity);
        teacherId = teacherEntity.getId();
    }

    @AfterEach
    public void tearDown() {
        teacherRepository.deleteAll();
    }

    @Test
    void updateTeacher_shouldReturn200AndUpdatedBody() {
        UpdateTeacherRequest request = new UpdateTeacherRequest("Anna", "Ivanova");

        ResponseEntity<TeacherResponse> response = testRestTemplate.exchange(
                "/api/teachers/{id}",
                HttpMethod.PATCH,
                new HttpEntity<>(request),
                TeacherResponse.class,
                teacherId
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        TeacherResponse body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.id()).isEqualTo(teacherId);
        assertThat(body.firstName()).isEqualTo("Anna");
        assertThat(body.lastName()).isEqualTo("Ivanova");
    }

    @Test
    void updateTeacher_whenFirstNameIsBlank_shouldReturn400() {
        UpdateTeacherRequest request = new UpdateTeacherRequest("", "Ivanova");

        ResponseEntity<Void> response = testRestTemplate.exchange(
                "/api/teachers/{id}",
                HttpMethod.PATCH,
                new HttpEntity<>(request),
                Void.class,
                teacherId
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void updateTeacher_whenNotExists_shouldReturn404() {
        UpdateTeacherRequest request = new UpdateTeacherRequest("Anna", "Ivanova");

        ResponseEntity<Void> response = testRestTemplate.exchange(
                "/api/teachers/{id}",
                HttpMethod.PATCH,
                new HttpEntity<>(request),
                Void.class,
                99999L
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
