package com.lms.lms_system_management.controller.course;

import com.lms.lms_system_management.TestcontainersConfiguration;
import com.lms.lms_system_management.dao.CourseRepository;
import com.lms.lms_system_management.dao.TeacherRepository;
import com.lms.lms_system_management.dto.course.CourseResponse;
import com.lms.lms_system_management.model.CourseEntity;
import com.lms.lms_system_management.model.TeacherEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
class GetCourseControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private TeacherRepository teacherRepository;

    private Long courseId;
    private Long teacherId;

    @BeforeEach
    public void setup() {
        TeacherEntity teacherEntity = TeacherEntity.builder()
                .firstName("Li")
                .lastName("Dja").
                build();
        teacherRepository.save(teacherEntity);
        teacherId = teacherEntity.getId();

        CourseEntity courseEntity = CourseEntity.builder()
                .name("Java")
                .description("Kurs po razrabotke Java")
                .teacherEntity(teacherEntity)
                .build();
        courseRepository.save(courseEntity);
        courseId = courseEntity.getId();
    }

    @AfterEach
    public void tearDown() {
        courseRepository.deleteAll();
        teacherRepository.deleteAll();
    }
    @Test
    void getCourseById_shouldReturn200AndCorrectBody() {
        ResponseEntity<CourseResponse> response = restTemplate.getForEntity(
                "/api/courses/{id}",
                CourseResponse.class,
                courseId
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        CourseResponse body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.id()).isEqualTo(courseId);
        assertThat(body.name()).isEqualTo("Java");
        assertThat(body.description()).isEqualTo("Kurs po razrabotke Java");
        assertThat(body.teacher().id()).isEqualTo(teacherId);
    }

    @Test
    void getCourseById_whenNotExists_shouldReturn404() {
        ResponseEntity<Void> response = restTemplate.getForEntity(
                "/api/courses/{id}",
                Void.class,
                999999L
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getAllCourses_shouldReturn200AndNonEmptyList() {
        ResponseEntity<CourseResponse[]> response = restTemplate.getForEntity(
                "/api/courses",
                CourseResponse[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        CourseResponse[] body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.length).isGreaterThan(0);
    }
}
