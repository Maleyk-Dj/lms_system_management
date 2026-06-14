package com.lms.lms_system_management.controller.schedule;

import com.lms.lms_system_management.TestcontainersConfiguration;
import com.lms.lms_system_management.dao.CourseRepository;
import com.lms.lms_system_management.dao.GroupRepository;
import com.lms.lms_system_management.dao.ScheduleRepository;
import com.lms.lms_system_management.dao.TeacherRepository;
import com.lms.lms_system_management.dto.schedule.NewScheduleRequest;
import com.lms.lms_system_management.dto.schedule.ScheduleResponse;
import com.lms.lms_system_management.model.Course;
import com.lms.lms_system_management.model.Group;
import com.lms.lms_system_management.model.Schedule;
import com.lms.lms_system_management.model.Teacher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
class PostScheduleControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private TeacherRepository teacherRepository;

    private Long groupId;
    private Long courseId;

    @BeforeEach
    public void setUp() {
        Teacher teacher = Teacher.builder()
                .firstName("Ivan")
                .lastName("Petrov")
                .build();
        teacherRepository.save(teacher);

        Group group = Group.builder()
                .name("Gruppa A")
                .build();
        groupRepository.save(group);
        groupId = group.getId();

        Course course = Course.builder()
                .name("Java")
                .description("Kurs po Java")
                .teacher(teacher)
                .build();
        courseRepository.save(course);
        courseId = course.getId();

        LocalDateTime scheduleDate = LocalDateTime.of(2026, 7, 1, 10, 0);

        Schedule schedule = Schedule.builder()
                .group(group)
                .course(course)
                .dateClass(scheduleDate)
                .build();
        scheduleRepository.save(schedule);
    }

    @AfterEach
    public void tearDown() {
        scheduleRepository.deleteAll();
        courseRepository.deleteAll();
        groupRepository.deleteAll();
        teacherRepository.deleteAll();
    }

    @Test
    void assignCourseTime_shouldReturn201AndCorrectBody() {
        LocalDateTime newDate = LocalDateTime.of(2026, 8, 1, 9, 0);
        NewScheduleRequest request = new NewScheduleRequest(groupId, courseId, newDate);

        ResponseEntity<ScheduleResponse> response = restTemplate.postForEntity(
                "/api/schedules",
                request,
                ScheduleResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ScheduleResponse body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.id()).isNotNull();
        assertThat(body.group().id()).isEqualTo(groupId);
        assertThat(body.course().id()).isEqualTo(courseId);
        assertThat(body.date()).isEqualTo(newDate);
    }

    @ParameterizedTest
    @MethodSource("invalidScheduleRequests")
    void assignCourseTime_whenIsNull_shouldReturn400(NewScheduleRequest request) {
        ResponseEntity<Void> response = restTemplate.postForEntity(
                "/api/schedules",
                request,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    static Stream<NewScheduleRequest> invalidScheduleRequests() {
        return Stream.of(
                new NewScheduleRequest(null, 1L, LocalDateTime.now()),   // null groupId
                new NewScheduleRequest(1L, null, LocalDateTime.now()),   // null courseId
                new NewScheduleRequest(1L, 1L, null)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidSchedule")
    void assignCourseTime_whenNotExists_shouldReturn404(NewScheduleRequest request) {
        ResponseEntity<Void> response = restTemplate.postForEntity(
                "/api/schedules",
                request,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    static Stream<NewScheduleRequest> invalidSchedule() {
        return Stream.of(
                new NewScheduleRequest(99999L, 1L, LocalDateTime.now()),
                new NewScheduleRequest(1L, 99999L, LocalDateTime.now())
        );
    }
}
