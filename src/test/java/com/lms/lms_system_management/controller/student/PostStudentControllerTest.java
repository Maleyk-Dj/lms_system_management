package com.lms.lms_system_management.controller.student;

import com.lms.lms_system_management.dao.GroupRepository;
import com.lms.lms_system_management.dao.StudentRepository;
import com.lms.lms_system_management.dto.student.NewStudentRequest;
import com.lms.lms_system_management.dto.student.StudentResponse;
import com.lms.lms_system_management.model.Group;
import com.lms.lms_system_management.model.Student;
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
public class PostStudentControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private GroupRepository groupRepository;

    private Long studentId;
    private Long groupId;
    private Long anotherGroupId;

    @BeforeEach
    public void setUp() {
        Group group = Group.builder().
                name("Gruppa A")
                .build();
        groupRepository.save(group);
        groupId = group.getId();

        Group anotherGroup = Group.builder().
                name("Gruppa B")
                .build();
        groupRepository.save(anotherGroup);
        anotherGroupId = anotherGroup.getId();

        Student student = Student.builder()
                .firstName("Valya")
                .lastName("Ivanova")
                .group(group)
                .build();
        studentRepository.save(student);
        studentId = student.getId();
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

    @Test
    void createStudent_whenFirstNameIsBlank_shouldReturn400() {
        NewStudentRequest request = new NewStudentRequest("", "Petrov", groupId);
        ResponseEntity<Void> response = restTemplate.postForEntity(
                "/api/students",
                request,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createStudent_whenLastNameIsBlank_shouldReturn400() {
        NewStudentRequest request = new NewStudentRequest("Petr", "", groupId);
        ResponseEntity<Void> response = restTemplate.postForEntity(
                "/api/students",
                request,
                Void.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createStudent_whenGroupIdIsNull_shouldReturn400() {
        NewStudentRequest request = new NewStudentRequest("Petr", "Petrov", null);
        ResponseEntity<Void> response = restTemplate.postForEntity(
                "/api/students"
                , request,
                Void.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
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
