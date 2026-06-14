package com.lms.lms_system_management.controller.course;

import com.lms.lms_system_management.TestcontainersConfiguration;
import com.lms.lms_system_management.dao.CourseRepository;
import com.lms.lms_system_management.dao.TeacherRepository;
import com.lms.lms_system_management.dto.course.NewCourseRequest;
import com.lms.lms_system_management.dto.course.CourseResponse;
import com.lms.lms_system_management.model.Course;
import com.lms.lms_system_management.model.Teacher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
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
class PostCourseControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private TeacherRepository teacherRepository;

    private Long teacherId;

    @BeforeEach
    public void setup() {
        Teacher teacher = Teacher.builder()
                .firstName("Li")
                .lastName("Dja").
                build();
        teacherRepository.save(teacher);
        teacherId = teacher.getId();
    }

    @AfterEach
    public void tearDown() {
        courseRepository.deleteAll();
        teacherRepository.deleteAll();
    }

    @Test
    void createCourse_shouldReturn201AndCorrectBody() {
        NewCourseRequest request = new NewCourseRequest("Spring", "Kurs po Spring", teacherId);
        ResponseEntity<CourseResponse> response = restTemplate.postForEntity(
                "/api/courses",
                request,
                CourseResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        CourseResponse body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.id()).isNotNull();
        assertThat(body.name()).isEqualTo("Spring");
        assertThat(body.description()).isEqualTo("Kurs po Spring");
        assertThat(body.teacher()).isNotNull();
        assertThat(body.teacher().id()).isNotNull();
    }

    @ParameterizedTest
    @MethodSource("invalidCourseRequests")
    void createCourse_withInvalidBody_shouldReturn400(NewCourseRequest request) {
        ResponseEntity<Void> response = restTemplate.postForEntity(
                "/api/courses",
                request,
                Void.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    static Stream<NewCourseRequest> invalidCourseRequests() {
        return Stream.of(
                new NewCourseRequest("", "Kurs po Spring", 1L),
                new NewCourseRequest("Spring", "", 1L),
                new NewCourseRequest("Spring", "Kurs po Spring", null)
        );
    }

    @Test
    void createCourse_whenTeacherNotExists_shouldReturn404() {
        NewCourseRequest request = new NewCourseRequest("Spring Boot", "Kurs po Spring", 99999L);

        ResponseEntity<Void> response = restTemplate.postForEntity(
                "/api/courses",
                request,
                Void.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
