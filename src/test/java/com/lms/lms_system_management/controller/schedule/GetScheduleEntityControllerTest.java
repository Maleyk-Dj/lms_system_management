package com.lms.lms_system_management.controller.schedule;

import com.lms.lms_system_management.TestcontainersConfiguration;
import com.lms.lms_system_management.dao.CourseRepository;
import com.lms.lms_system_management.dao.GroupRepository;
import com.lms.lms_system_management.dao.ScheduleRepository;
import com.lms.lms_system_management.dao.TeacherRepository;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
class GetScheduleEntityControllerTest {
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

    @BeforeEach
    public void setUp() {
        TeacherEntity teacherEntity = TeacherEntity.builder()
                .firstName("Ivan")
                .lastName("Petrov")
                .build();
        teacherRepository.save(teacherEntity);

        GroupEntity groupEntity = GroupEntity.builder()
                .name("Gruppa A")
                .build();
        groupRepository.save(groupEntity);
        groupId = groupEntity.getId();

        CourseEntity courseEntity = CourseEntity.builder()
                .name("Java")
                .description("Kurs po Java")
                .teacherEntity(teacherEntity)
                .build();
        courseRepository.save(courseEntity);

        LocalDateTime scheduleDate = LocalDateTime.of(2026, 7, 1, 10, 0);

        ScheduleEntity scheduleEntity = ScheduleEntity.builder()
                .groupEntity(groupEntity)
                .courseEntity(courseEntity)
                .dateClass(scheduleDate)
                .build();
        scheduleRepository.save(scheduleEntity);
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
        GroupEntity emptyGroupEntity = GroupEntity.builder().name("Gruppa B").build();
        groupRepository.save(emptyGroupEntity);

        ResponseEntity<ScheduleResponse[]> response = restTemplate.getForEntity(
                "/api/schedules/groups/{groupId}",
                ScheduleResponse[].class,
                emptyGroupEntity.getId()
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ScheduleResponse[] body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.length).isEqualTo(0);
    }
}
