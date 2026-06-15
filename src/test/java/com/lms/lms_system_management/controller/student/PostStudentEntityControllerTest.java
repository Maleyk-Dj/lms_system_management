package com.lms.lms_system_management.controller.student;

import com.lms.lms_system_management.TestcontainersConfiguration;
import com.lms.lms_system_management.dao.GroupRepository;
import com.lms.lms_system_management.dao.StudentRepository;
import com.lms.lms_system_management.dto.student.NewStudentRequest;
import com.lms.lms_system_management.dto.student.StudentResponse;
import com.lms.lms_system_management.model.GroupEntity;
import com.lms.lms_system_management.model.StudentEntity;
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
class PostStudentEntityControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private GroupRepository groupRepository;

    private Long groupId;

    @BeforeEach
    public void setUp() {
        GroupEntity groupEntity = GroupEntity.builder().
                name("Gruppa A")
                .build();
        groupRepository.save(groupEntity);
        groupId = groupEntity.getId();

        GroupEntity anotherGroupEntity = GroupEntity.builder().
                name("Gruppa B")
                .build();
        groupRepository.save(anotherGroupEntity);

        StudentEntity studentEntity = StudentEntity.builder()
                .firstName("Valya")
                .lastName("Ivanova")
                .groupEntity(groupEntity)
                .build();
        studentRepository.save(studentEntity);
    }

    @AfterEach
    public void tearDown() {
        studentRepository.deleteAll();
        groupRepository.deleteAll();
    }

    @Test
    void createStudent_shouldReturn201AndCorrectBody() {
        NewStudentRequest request = new NewStudentRequest("Petr", "Petrov", groupId);
        ResponseEntity<StudentResponse> response = restTemplate.postForEntity(
                "/api/students",
                request,
                StudentResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        StudentResponse body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.id()).isNotNull();
        assertThat(body.firstName()).isEqualTo("Petr");
        assertThat(body.lastName()).isEqualTo("Petrov");
        assertThat(body.groupId()).isEqualTo(groupId);
    }

    @ParameterizedTest
    @MethodSource("invalidNewStudentRequest")
    void createStudent_whenIsBlank_shouldReturn400(NewStudentRequest request) {
        ResponseEntity<Void> response = restTemplate.postForEntity(
                "/api/students"
                , request,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    static Stream<NewStudentRequest> invalidNewStudentRequest() {
        return Stream.of(
                new NewStudentRequest("", "Petrov", 1L),
                new NewStudentRequest("Petr", "", 1L),
                new NewStudentRequest("Petr", "Petrov", null)
        );
    }

    @Test
    void createStudent_whenGroupNotExists_shouldReturn404() {
        NewStudentRequest request = new NewStudentRequest("Petr", "Petrov", 999999L);
        ResponseEntity<Void> response = restTemplate.postForEntity(
                "/api/students"
                , request,
                Void.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}
