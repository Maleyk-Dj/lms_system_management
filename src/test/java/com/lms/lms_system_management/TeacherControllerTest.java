package com.lms.lms_system_management;

import com.lms.lms_system_management.dao.TeacherRepository;
import com.lms.lms_system_management.dto.request.NewTeacherRequest;
import com.lms.lms_system_management.dto.request.UpdateTeacherRequest;
import com.lms.lms_system_management.dto.response.TeacherResponse;
import com.lms.lms_system_management.model.Teacher;
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
public class TeacherControllerTest {

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

//CREATE

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

    //GET

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

    //UPDATE

    @Test
    void updateTeacher_shouldReturn200AndUpdatedBody() {
        UpdateTeacherRequest request = new UpdateTeacherRequest("Anna", "Ivanova");

        ResponseEntity<TeacherResponse> response =
                testRestTemplate.exchange(
                        "/api/teachers/{id}",
                        HttpMethod.PATCH,
                        new HttpEntity<>(request),
                        TeacherResponse.class,
                        teacherId
                );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        TeacherResponse body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.firstName()).isEqualTo("Anna");
        assertThat(body.lastName()).isEqualTo("Ivanova");
        assertThat(body.id()).isEqualTo(teacherId);
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

    @Test
    void deleteTeacher_shouldReturn204AndRemoveFromDb() {
        ResponseEntity<Void> response = testRestTemplate.exchange(
                "/api/teachers/{id}",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class,
                teacherId
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(teacherRepository.findById(teacherId)).isEmpty();
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

    @Test
    void getScheduleByTeacher_shouldReturn200AndEmptyList() {
        ResponseEntity<Object[]> response = testRestTemplate.getForEntity(
                "/api/teachers/{id}/schedules",
                Object[].class,
                teacherId
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Object[] body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.length).isEqualTo(0);
    }

    @Test
    void getScheduleByTeacher_whenNotExists_shouldReturn404() {
        ResponseEntity<Void>response = testRestTemplate.getForEntity(
                "/api/teachers/{id}/schedules",
                Void.class,
                99999L
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
