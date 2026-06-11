package com.lms.lms_system_management;

import com.lms.lms_system_management.dao.CourseRepository;
import com.lms.lms_system_management.dao.TeacherRepository;
import com.lms.lms_system_management.dto.request.NewCourseRequest;
import com.lms.lms_system_management.dto.request.UpdateCourseRequest;
import com.lms.lms_system_management.dto.response.CourseResponse;
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
public class CourseControlletTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private TeacherRepository teacherRepository;

    private Long courseId;
    private Long teacherId;
    private Long anotherTeacherId;

    @BeforeEach
    public void setup() {
        Teacher teacher = Teacher.builder()
                .firstName("Li")
                .lastName("Dja").
                build();
        teacherRepository.save(teacher);
        teacherId = teacher.getId();

        Teacher anotherTeacher = Teacher.builder()
                .firstName("Ira")
                .lastName("Varnava")
                .build();
        teacherRepository.save(anotherTeacher);
        anotherTeacherId = anotherTeacher.getId();

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

    @Test
    void createCourse_WhenNameIsBlank_shouldReturn400() {
        NewCourseRequest request = new NewCourseRequest("", "Kurs po Spring", teacherId);
        ResponseEntity<Void> response = restTemplate.postForEntity(
                "/api/courses",
                request,
                Void.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createCourse_WhenDescriptionIsBlank_shouldReturn400() {
        NewCourseRequest request = new NewCourseRequest("Spring", "", teacherId);
        ResponseEntity<Void> response = restTemplate.postForEntity(
                "/api/courses",
                request,
                Void.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createCourse_WhenTeacherIdIsNull_shouldReturn400() {
        NewCourseRequest request = new NewCourseRequest("Spring", "Kurs po Spring", null);
        ResponseEntity<Void> response = restTemplate.postForEntity(
                "/api/courses",
                request,
                Void.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
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

    //GET
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
        assertThat(body.name()).isEqualTo("Spring");
        assertThat(body.description()).isEqualTo("Kurs po Spring");
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

    //update

    @Test
    void updateCourse_shouldReturn200AndUpdatedBody() {
        UpdateCourseRequest request = new UpdateCourseRequest(
                "Kotlin", "Kurs po Kotlin", anotherTeacherId);

        ResponseEntity<CourseResponse> response = restTemplate.exchange(
                "/api/courses/{id}",
                HttpMethod.PUT,
                new HttpEntity<>(request),
                CourseResponse.class,
                courseId
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        CourseResponse body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.id()).isEqualTo(courseId);
        assertThat(body.name()).isEqualTo("Kotlin");
        assertThat(body.description()).isEqualTo("Kurs po Kotlin");
        assertThat(body.teacher().id()).isEqualTo(anotherTeacherId);
    }

    @Test
    void updateCourse_whenNameIsBlank_shouldReturn400() {
        UpdateCourseRequest request = new UpdateCourseRequest(
                "", "Kurs po Kotlin", anotherTeacherId);

        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/courses/{id}",
                HttpMethod.PUT,
                new HttpEntity<>(request),
                Void.class,
                courseId
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void updateCourse_whenDescriptionIsNull_shouldReturn400() {
        UpdateCourseRequest request = new UpdateCourseRequest("Kotlin", "", anotherTeacherId);

        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/courses/{id}",
                HttpMethod.PUT,
                new HttpEntity<>(request),
                Void.class,
                courseId
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void updateCourse_whenTeacherNotExists_shouldReturn404() {
        UpdateCourseRequest request = new UpdateCourseRequest(
                "Spring Boot", "Kurs po Spring", 99999L);

        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/courses/{id}",
                HttpMethod.PUT,
                new HttpEntity<>(request),
                Void.class,
                88888L
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void updateCourse_whenCourseNotExists_shouldReturn404() {
        UpdateCourseRequest request = new UpdateCourseRequest(
                "Spring Boot", "Kurs po Spring", teacherId);

        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/courses/{id}",
                HttpMethod.PUT,
                new HttpEntity<>(request),
                Void.class,
                99999L
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    //delete
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
}
