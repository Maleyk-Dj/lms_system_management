package com.lms.lms_system_management.controller.schedule;

import com.lms.lms_system_management.TestcontainersConfiguration;
import com.lms.lms_system_management.dao.CourseRepository;
import com.lms.lms_system_management.dao.GroupRepository;
import com.lms.lms_system_management.dao.ScheduleRepository;
import com.lms.lms_system_management.dao.TeacherRepository;
import com.lms.lms_system_management.dto.schedule.UpdateScheduleRequest;
import com.lms.lms_system_management.dto.schedule.ScheduleResponse;
import com.lms.lms_system_management.model.CourseEntity;
import com.lms.lms_system_management.model.GroupEntity;
import com.lms.lms_system_management.model.ScheduleEntity;
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

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
class PutScheduleEntityControllerTest {
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
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Long groupId;
    private Long courseId;
    private Long scheduleId;
    private LocalDateTime scheduleDate;

    @BeforeEach
    public void setUp() {
        TeacherEntity teacherEntity = new TeacherEntity();
        teacherEntity.setFirstName("Malika");
        teacherEntity.setLastName("Djabrailova");
        teacherRepository.save(teacherEntity);

        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setName("Gruppa A");
        groupRepository.save(groupEntity);
        groupId = groupEntity.getId();

        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setName("Java");
        courseEntity.setDescription("Kurs po Java");
        courseEntity.setTeacherEntity(teacherEntity);
        courseRepository.save(courseEntity);
        courseId = courseEntity.getId();

        scheduleDate = LocalDateTime.of(2026, 7, 1, 10, 0);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setGroupEntity(groupEntity);
        scheduleEntity.setCourseEntity(courseEntity);
        scheduleEntity.setDateClass(scheduleDate);
        scheduleRepository.save(scheduleEntity);
        scheduleId = scheduleEntity.getId();
    }

    @AfterEach
    public void tearDown() {
        jdbcTemplate.execute("DELETE FROM schedules");
        jdbcTemplate.execute("DELETE FROM courses");
        jdbcTemplate.execute("DELETE FROM groups");
        jdbcTemplate.execute("DELETE FROM teachers");
    }
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

}
