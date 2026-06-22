package com.lms.lms_system_management.controller.group;

import com.lms.lms_system_management.TestcontainersConfiguration;
import com.lms.lms_system_management.dto.group.NewGroupRequest;
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
class PostGroupControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createGroup_shouldReturn201AndCorrectBody() {
        NewGroupRequest request = new NewGroupRequest("Gruppa B");

        ResponseEntity<GroupResponse> response = restTemplate.postForEntity(
                "/api/groups",
                request,
                GroupResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        GroupResponse body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.id()).isNotNull();
        assertThat(body.name()).isEqualTo("Gruppa B");
    }

    @Test
    void createGroup_whenNameIsBlank_shouldReturn400() {
        NewGroupRequest request = new NewGroupRequest("");

        ResponseEntity<Void> response = restTemplate.postForEntity(
                "/api/groups",
                request,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
