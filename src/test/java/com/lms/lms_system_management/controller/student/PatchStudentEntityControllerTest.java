package com.lms.lms_system_management.controller.student;

import com.lms.lms_system_management.TestcontainersConfiguration;
import com.lms.lms_system_management.dao.GroupRepository;
import com.lms.lms_system_management.dao.StudentRepository;
import com.lms.lms_system_management.dto.student.StudentResponse;
import com.lms.lms_system_management.model.GroupEntity;
import com.lms.lms_system_management.model.StudentEntity;
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
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
class PatchStudentEntityControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Long studentId;
    private Long groupId;
    private Long anotherGroupId;

    @BeforeEach
    public void setUp() {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setName("Gruppa A");
        groupRepository.save(groupEntity);
        groupId = groupEntity.getId();

        GroupEntity anotherGroupEntity = new GroupEntity();
        anotherGroupEntity.setName("Gruppa B");
        groupRepository.save(anotherGroupEntity);
        anotherGroupId = anotherGroupEntity.getId();

        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setFirstName("Valya");
        studentEntity.setLastName("Ivanova");
        studentEntity.setGroupEntity(groupEntity);
        studentRepository.save(studentEntity);
        studentId = studentEntity.getId();
    }

    @AfterEach
    public void tearDown() {
        jdbcTemplate.execute("DELETE FROM students");
        jdbcTemplate.execute("DELETE FROM groups");
    }

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
    void getAllStudents_shouldReturn200AndNonEmptyContent() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/students",
                String.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).contains("Valya");
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
    void addToGroup_whenStudentNotExists_shouldReturn404() {
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
