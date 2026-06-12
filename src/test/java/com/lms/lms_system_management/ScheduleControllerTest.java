package com.lms.lms_system_management;

import com.lms.lms_system_management.dao.CourseRepository;
import com.lms.lms_system_management.dao.GroupRepository;
import com.lms.lms_system_management.dao.ScheduleRepository;
import com.lms.lms_system_management.dao.TeacherRepository;
import com.lms.lms_system_management.dto.request.NewScheduleRequest;
import com.lms.lms_system_management.dto.request.UpdateScheduleRequest;
import com.lms.lms_system_management.dto.response.ScheduleResponse;
import com.lms.lms_system_management.model.Course;
import com.lms.lms_system_management.model.Group;
import com.lms.lms_system_management.model.Schedule;
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

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
public class ScheduleControllerTest {

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
    private Long scheduleId;
    private LocalDateTime scheduleDate;

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

        scheduleDate = LocalDateTime.of(2026, 7, 1, 10, 0);

        Schedule schedule = Schedule.builder()
                .group(group)
                .course(course)
                .date(scheduleDate)
                .build();
        scheduleRepository.save(schedule);
        scheduleId = schedule.getId();
    }

    @AfterEach
    public void tearDown() {
        scheduleRepository.deleteAll();
        courseRepository.deleteAll();
        groupRepository.deleteAll();
        teacherRepository.deleteAll();
    }

    // CREATE

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

    @Test
    void assignCourseTime_whenGroupIdIsNull_shouldReturn400() {
        NewScheduleRequest request = new NewScheduleRequest(null, courseId, scheduleDate);

        ResponseEntity<Void> response = restTemplate.postForEntity(
                "/api/schedules",
                request,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void assignCourseTime_whenCourseIdIsNull_shouldReturn400() {
        NewScheduleRequest request = new NewScheduleRequest(groupId, null, scheduleDate);

        ResponseEntity<Void> response = restTemplate.postForEntity(
                "/api/schedules",
                request,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void assignCourseTime_whenDateIsNull_shouldReturn400() {
        NewScheduleRequest request = new NewScheduleRequest(groupId, courseId, null);

        ResponseEntity<Void> response = restTemplate.postForEntity(
                "/api/schedules",
                request,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void assignCourseTime_whenGroupNotExists_shouldReturn404() {
        NewScheduleRequest request = new NewScheduleRequest(99999L, courseId, scheduleDate);

        ResponseEntity<Void> response = restTemplate.postForEntity(
                "/api/schedules",
                request,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void assignCourseTime_whenCourseNotExists_shouldReturn404() {
        NewScheduleRequest request = new NewScheduleRequest(groupId, 99999L, scheduleDate);

        ResponseEntity<Void> response = restTemplate.postForEntity(
                "/api/schedules",
                request,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // UPDATE

    @Test
    void updateSchedule_shouldReturn200AndUpdatedBody() {
        LocalDateTime updatedDate = LocalDateTime.of(2026, 9, 15, 14, 0);
        UpdateScheduleRequest request = new UpdateScheduleRequest(groupId, courseId, updatedDate);

        ResponseEntity<ScheduleResponse> response = restTemplate.exchange(
                "/api/schedules/{scheduleId}",
                HttpMethod.PUT,
                new HttpEntity<>(request),
                ScheduleResponse.class,
                scheduleId
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ScheduleResponse body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.id()).isEqualTo(scheduleId);
        assertThat(body.group().id()).isEqualTo(groupId);
        assertThat(body.course().id()).isEqualTo(courseId);
        assertThat(body.date()).isEqualTo(updatedDate);
    }

    @Test
    void updateSchedule_whenGroupIdIsNull_shouldReturn400() {
        UpdateScheduleRequest request = new UpdateScheduleRequest(null, courseId, scheduleDate);

        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/schedules/{scheduleId}",
                HttpMethod.PUT,
                new HttpEntity<>(request),
                Void.class,
                scheduleId
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void updateSchedule_whenNotExists_shouldReturn404() {
        UpdateScheduleRequest request = new UpdateScheduleRequest(groupId, courseId, scheduleDate);

        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/schedules/{scheduleId}",
                HttpMethod.PUT,
                new HttpEntity<>(request),
                Void.class,
                99999L
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // DELETE

    @Test
    void deleteSchedule_shouldReturn204AndRemoveFromDb() {
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/schedules/{scheduleId}",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class,
                scheduleId
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(scheduleRepository.findById(scheduleId)).isEmpty();
    }

    @Test
    void deleteSchedule_whenNotExists_shouldReturn404() {
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/schedules/{scheduleId}",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class,
                99999L
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // GET by group

    @Test
    void getScheduleByGroup_shouldReturn200AndNonEmptyList() {
        ResponseEntity<ScheduleResponse[]> response = restTemplate.getForEntity(
                "/api/schedules/groups/{groupId}",
                ScheduleResponse[].class,
                groupId
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ScheduleResponse[] body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.length).isGreaterThan(0);
        assertThat(body[0].group().id()).isEqualTo(groupId);
    }

    @Test
    void getScheduleByGroup_whenGroupNotExists_shouldReturn404() {
        ResponseEntity<Void> response = restTemplate.getForEntity(
                "/api/schedules/groups/{groupId}",
                Void.class,
                99999L
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getScheduleByGroup_whenNoSchedules_shouldReturn200AndEmptyList() {
        Group emptyGroup = Group.builder().name("Gruppa B").build();
        groupRepository.save(emptyGroup);

        ResponseEntity<ScheduleResponse[]> response = restTemplate.getForEntity(
                "/api/schedules/groups/{groupId}",
                ScheduleResponse[].class,
                emptyGroup.getId()
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ScheduleResponse[] body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.length).isEqualTo(0);
    }
}
