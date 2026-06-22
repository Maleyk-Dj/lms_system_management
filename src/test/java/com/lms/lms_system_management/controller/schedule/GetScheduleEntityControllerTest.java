package com.lms.lms_system_management.controller.schedule;

import com.lms.lms_system_management.TestcontainersConfiguration;
import com.lms.lms_system_management.dao.CourseRepository;
import com.lms.lms_system_management.dao.GroupRepository;
import com.lms.lms_system_management.dao.ScheduleRepository;
import com.lms.lms_system_management.dao.TeacherRepository;
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
        TeacherEntity teacherEntity = new TeacherEntity();
        teacherEntity.setFirstName("Malika");
        teacherEntity.setLastName("Djabrailova");
        teacherRepository.save(teacherEntity);

        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setName("Gruppa A");
        groupRepository.save(groupEntity);

        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setName("Java");
        courseEntity.setDescription("Kurs po Java");
        courseEntity.setTeacherEntity(teacherEntity);
        courseRepository.save(courseEntity);

        LocalDateTime scheduleDate = LocalDateTime.of(2026, 7, 1, 10, 0);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setGroupEntity(groupEntity);
        scheduleEntity.setCourseEntity(courseEntity);
        scheduleEntity.setDateClass(scheduleDate);
        scheduleRepository.save(scheduleEntity);
    }

    @AfterEach
    public void tearDown() {
        scheduleRepository.deleteAllInBatch();
        courseRepository.deleteAllInBatch();
        groupRepository.deleteAllInBatch();
        teacherRepository.deleteAllInBatch();
    }

    @Test
    void getScheduleByGroup_shouldReturn200AndNonEmptyBody() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/schedules?groupId={groupId}",
                String.class,
                groupId
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).contains("\"totalElements\":1");
    }

    @Test
    void getScheduleByGroup_whenUnknownGroup_shouldReturn200AndEmptyContent() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/schedules?groupId=99999",
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("\"totalElements\":0");
    }

    @Test
    void getScheduleByGroup_whenNoSchedules_shouldReturn200AndEmptyContent() {
        GroupEntity emptyGroupEntity = new GroupEntity();
        emptyGroupEntity.setName("Gruppa B");
        groupRepository.save(emptyGroupEntity);

        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/schedules?groupId={groupId}",
                String.class,
                emptyGroupEntity.getId()
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("\"totalElements\":0");
    }
}
