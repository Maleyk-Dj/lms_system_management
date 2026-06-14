package com.lms.lms_system_management.controller.teacher;

import com.lms.lms_system_management.TestcontainersConfiguration;
import com.lms.lms_system_management.dao.TeacherRepository;
import com.lms.lms_system_management.dto.teacher.NewTeacherRequest;
import com.lms.lms_system_management.dto.teacher.TeacherResponse;
import com.lms.lms_system_management.model.Teacher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
class PostTeacherControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private TeacherRepository teacherRepository;


    @BeforeEach
    public void setup() {
        Teacher teacher = Teacher.builder()
                .firstName("Malika")
                .lastName("Djabrailova")
                .build();
        teacherRepository.save(teacher);
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

    @ParameterizedTest
    @MethodSource("invalidNewTeacherRequest")
    void createTeacher_whenFirstNameIsInvalid_shouldReturn400(NewTeacherRequest request) {
        ResponseEntity<Void> response = testRestTemplate.postForEntity(
                "/api/teachers",
                request,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    static Stream <NewTeacherRequest> invalidNewTeacherRequest(){
        return Stream.of(
                new NewTeacherRequest("", "Smirnov"),
                new NewTeacherRequest(" ", "Smirnov")
        );
    }
}
