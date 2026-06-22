package com.lms.lms_system_management.controller.schedule;

import com.lms.lms_system_management.TestcontainersConfiguration;
import com.lms.lms_system_management.dao.GroupRepository;
import com.lms.lms_system_management.model.GroupEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
@Sql("/sql/insert-schedule.sql")
@Sql(value = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class GetScheduleEntityControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private GroupRepository groupRepository;

    @Test
    void getScheduleByGroup_shouldReturn200AndNonEmptyBody() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/schedules?groupId={groupId}",
                String.class,
                1L
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
