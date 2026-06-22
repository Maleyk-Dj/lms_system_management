package com.lms.lms_system_management.controller.course;

import com.lms.lms_system_management.TestcontainersConfiguration;
import com.lms.lms_system_management.dao.CourseRepository;
import com.lms.lms_system_management.dao.ScheduleRepository;
import com.lms.lms_system_management.dao.TeacherRepository;
import com.lms.lms_system_management.model.CourseEntity;
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
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
class DeleteCourseControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Long courseId;

    @BeforeEach
    public void setup() {
        TeacherEntity teacherEntity = new TeacherEntity();
        teacherEntity.setFirstName("Li");
        teacherEntity.setLastName("Dja");
        teacherRepository.save(teacherEntity);

        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setName("Java");
        courseEntity.setDescription("Kurs po razrabotke Java");
        courseEntity.setTeacherEntity(teacherEntity);
        courseRepository.save(courseEntity);
        courseId = courseEntity.getId();
    }

    @AfterEach
    public void tearDown() {
        jdbcTemplate.execute("DELETE FROM schedules");
        jdbcTemplate.execute("DELETE FROM courses");
        jdbcTemplate.execute("DELETE FROM teachers");
    }

    @Test
    void deleteCourse_shouldReturn204AndRemoveFromBd() {
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/courses/{id}",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class,
                courseId
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(courseRepository.findById(courseId)).isEmpty();
    }

    @Test
    void deleteCourse_whenNotExists_shouldReturn404() {
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/courses/{id}",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class,
                99999L
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
