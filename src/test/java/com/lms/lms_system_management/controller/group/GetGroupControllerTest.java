package com.lms.lms_system_management.controller.group;

import com.lms.lms_system_management.TestcontainersConfiguration;
import com.lms.lms_system_management.dto.group.GroupResponse;
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
@Sql("/sql/insert-group.sql")
@Sql(value = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class GetGroupControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getGroupById_shouldReturn200AndCorrectBody() {
        ResponseEntity<GroupResponse> response = restTemplate.getForEntity(
                "/api/groups/{groupId}",
                GroupResponse.class,
                1L
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        GroupResponse body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.id()).isEqualTo(1L);
        assertThat(body.name()).isEqualTo("Gruppa A");
    }

    @Test
    void getGroupById_whenNotExists_shouldReturn404() {
        ResponseEntity<Void> response = restTemplate.getForEntity(
                "/api/groups/{groupId}",
                Void.class,
                99999L
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getAllGroups_shouldReturn200AndNonEmptyContent() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/groups",
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).contains("Gruppa A");
    }
}
