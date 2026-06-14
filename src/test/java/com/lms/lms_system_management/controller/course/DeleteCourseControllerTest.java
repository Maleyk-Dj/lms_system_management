package com.lms.lms_system_management.controller.course;

import com.lms.lms_system_management.TestcontainersConfiguration;
import com.lms.lms_system_management.dao.CourseRepository;
import com.lms.lms_system_management.dao.TeacherRepository;
import com.lms.lms_system_management.model.Course;
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
class DeleteCourseControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private TeacherRepository teacherRepository;

    private Long courseId;

    @BeforeEach
    public void setup() {

        Teacher teacher = Teacher.builder()
                .firstName("Li")
                .lastName("Dja")
                .build();
        teacherRepository.save(teacher);

        Course course = Course.builder()
                .name("Java")
                .description("Kurs po razrabotke Java")
                .teacher(teacher)
                .build();
        courseRepository.save(course);
        courseId = course.getId();
    }

    @AfterEach
    public void tearDown() {
        courseRepository.deleteAll();
        teacherRepository.deleteAll();
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
