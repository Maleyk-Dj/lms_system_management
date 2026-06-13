package com.lms.lms_system_management.controller.schedule;

import com.lms.lms_system_management.dao.CourseRepository;
import com.lms.lms_system_management.dao.GroupRepository;
import com.lms.lms_system_management.dao.ScheduleRepository;
import com.lms.lms_system_management.dao.TeacherRepository;
import com.lms.lms_system_management.dto.schedule.ScheduleResponse;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
public class GetScheduleControllerTest {
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
