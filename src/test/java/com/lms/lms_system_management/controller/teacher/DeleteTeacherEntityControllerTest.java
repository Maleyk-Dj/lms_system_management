package com.lms.lms_system_management.controller.teacher;

import com.lms.lms_system_management.TestcontainersConfiguration;
import com.lms.lms_system_management.dao.CourseRepository;
import com.lms.lms_system_management.dao.ScheduleRepository;
import com.lms.lms_system_management.dao.TeacherRepository;
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
class DeleteTeacherEntityControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    private Long teacherId;

    @BeforeEach
    public void setup() {
        TeacherEntity teacherEntity = new TeacherEntity();
        teacherEntity.setFirstName("Malika");
        teacherEntity.setLastName("Djabrailova");
        teacherRepository.save(teacherEntity);
        teacherId = teacherEntity.getId();
    }

    @AfterEach
    public void tearDown() {
        scheduleRepository.deleteAllInBatch();
        courseRepository.deleteAllInBatch();
        teacherRepository.deleteAllInBatch();
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
}
