package com.lms.lms_system_management;

import com.lms.lms_system_management.dao.GroupRepository;
import com.lms.lms_system_management.dao.StudentRepository;
import com.lms.lms_system_management.dto.request.NewStudentRequest;
import com.lms.lms_system_management.dto.request.UpdateStudentRequest;
import com.lms.lms_system_management.dto.response.StudentResponse;
import com.lms.lms_system_management.model.Group;
import com.lms.lms_system_management.model.Student;
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
public class StudentControllerTest {
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

    //GET
    @Test
    void getStudentById_shouldReturn200AndCorrectBody() {
        ResponseEntity<StudentResponse> response = restTemplate.getForEntity(
                "/api/students/{id}",
                StudentResponse.class,
                studentId
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        StudentResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.id()).isEqualTo(studentId);
        assertThat(body.firstName()).isEqualTo("Valya");
        assertThat(body.lastName()).isEqualTo("Ivanova");
        assertThat(body.groupId()).isEqualTo(groupId);
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
    void getAllStudents_shouldReturn200AndNonEmptyList() {
        ResponseEntity<StudentResponse[]> response = restTemplate.getForEntity(
                "/api/students"
                , StudentResponse[].class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        StudentResponse[] body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.length).isGreaterThan(0);
    }

    //update
    @Test
    void updateStudentById_shouldReturn200AndUpdatedBody() {
        UpdateStudentRequest request = new UpdateStudentRequest("Oleg", "Olegovich", null);
        ResponseEntity<StudentResponse> response = restTemplate.exchange(
                "/api/students/{id}",
                HttpMethod.PATCH,
                new HttpEntity<>(request),
                StudentResponse.class,
                studentId
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        StudentResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.id()).isEqualTo(studentId);
        assertThat(body.firstName()).isEqualTo("Oleg");
        assertThat(body.lastName()).isEqualTo("Olegovich");
        assertThat(body.groupId()).isEqualTo(groupId);
    }

    @Test
    void updateStudent_WithNewGroup_shouldReturn200AndUpdatedGroupId() {
        UpdateStudentRequest request = new UpdateStudentRequest(null, null, anotherGroupId);
        ResponseEntity<StudentResponse> response = restTemplate.exchange(
                "/api/students/{id}",
                HttpMethod.PATCH,
                new HttpEntity<>(request),
                StudentResponse.class,
                studentId
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        StudentResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.groupId()).isEqualTo(anotherGroupId);
        assertThat(body.firstName()).isEqualTo("Valya");
        assertThat(body.lastName()).isEqualTo("Ivanova");
    }

    @Test
    void updateStudent_whenNotExists_shouldReturn404() {
        UpdateStudentRequest request = new UpdateStudentRequest("Oleg", "Olegovich", null);
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/students/{id}",
                HttpMethod.PATCH,
                new HttpEntity<>(request),
                Void.class,
                999999L
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    //delete

    @Test
    void deleteStudent_shouldReturn204AndRemoveFromDb() {
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/students/{id}",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class,
                studentId
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(studentRepository.findById(studentId)).isEmpty();
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

    @Test
    void addToGroup_shouldReturn200AndUpdateGroupId() {
        ResponseEntity<StudentResponse> response = restTemplate.exchange(
                "/api/students/{studentId}/groups/{groupId}",
                HttpMethod.PATCH,
                HttpEntity.EMPTY,
                StudentResponse.class,
                studentId,
                anotherGroupId
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        StudentResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.groupId()).isEqualTo(anotherGroupId);
    }

    @Test
    void addToGroup_whenAlreadyInGroup_shouldReturn409() {
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/students/{studentId}/groups/{groupId}",
                HttpMethod.PATCH,
                HttpEntity.EMPTY,
                Void.class,
                studentId,
                groupId
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void addToGroup_whenStudentNotExists_shouldReturn404(){
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/students/{studentId}/groups/{groupId}",
                HttpMethod.PATCH,
                HttpEntity.EMPTY,
                Void.class,
                999999L,
                groupId
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
