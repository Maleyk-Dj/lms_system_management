package com.lms.lms_system_management.controller.student;

import com.lms.lms_system_management.dao.GroupRepository;
import com.lms.lms_system_management.dao.StudentRepository;
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
import org.testcontainers.utility.TestcontainersConfiguration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
public class DeleteStudentControllerTest {
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

}
