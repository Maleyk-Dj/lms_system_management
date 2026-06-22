package com.lms.lms_system_management.controller.course;

import com.lms.lms_system_management.TestcontainersConfiguration;
import com.lms.lms_system_management.dao.CourseRepository;
import com.lms.lms_system_management.dao.ScheduleRepository;
import com.lms.lms_system_management.dao.TeacherRepository;
import com.lms.lms_system_management.dto.course.UpdateCourseRequest;
import com.lms.lms_system_management.dto.course.CourseResponse;
import com.lms.lms_system_management.model.CourseEntity;
import com.lms.lms_system_management.model.TeacherEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
public class PutCourseControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;

    private Long courseId;
    private Long anotherTeacherId;

    @BeforeEach
    public void setup() {

        TeacherEntity teacherEntity = new TeacherEntity();
        teacherEntity.setFirstName("Li");
        teacherEntity.setLastName("Dja");
        teacherRepository.save(teacherEntity);

        TeacherEntity anotherTeacherEntity = new TeacherEntity();
        anotherTeacherEntity.setFirstName("Ira");
        anotherTeacherEntity.setLastName("Varnava");
        teacherRepository.save(anotherTeacherEntity);
        anotherTeacherId = anotherTeacherEntity.getId();

        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setName("Java");
        courseEntity.setDescription("Kurs po razrabotke Java");
        courseEntity.setTeacherEntity(teacherEntity);
        courseRepository.save(courseEntity);
        courseId = courseEntity.getId();
    }

    @AfterEach
    public void tearDown() {
        scheduleRepository.deleteAllInBatch();
        courseRepository.deleteAllInBatch();
        teacherRepository.deleteAllInBatch();
    }
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

    @ParameterizedTest
    @MethodSource("invalidUpdateCourseRequests")
    void updateCourse_withInvalidBody_shouldReturn400(UpdateCourseRequest request) {

        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/courses/{id}",
                HttpMethod.PUT,
                new HttpEntity<>(request),
                Void.class,
                88888L
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    static Stream<UpdateCourseRequest> invalidUpdateCourseRequests() {
        return Stream.of(
                new UpdateCourseRequest("", "Kurs po Spring", 99999L),
                new UpdateCourseRequest("Spring Boot", "", 1L),
                new UpdateCourseRequest("Spring Boot", "Kurs po Spring", null)
        );
    }

    @ParameterizedTest
    @MethodSource ("notFoundUpdateCourseArgs")
    void updateCourse_whenNotExists_shouldReturn404(UpdateCourseRequest request, Long courseId) {

        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/courses/{id}",
                HttpMethod.PUT,
                new HttpEntity<>(request),
                Void.class,
                courseId
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    static Stream<Arguments> notFoundUpdateCourseArgs() {
        return Stream.of(
                Arguments.of(new UpdateCourseRequest("Spring", "Desc", 99999L), 88888L),
                Arguments.of(new UpdateCourseRequest("Spring", "Desc", 1L), 99999L
                ));
    }
}
