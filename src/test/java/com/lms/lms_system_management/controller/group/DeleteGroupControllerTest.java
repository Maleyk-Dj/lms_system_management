package com.lms.lms_system_management.controller.group;

import com.lms.lms_system_management.TestcontainersConfiguration;
import com.lms.lms_system_management.dao.GroupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
@Sql("/sql/insert-group.sql")
@Sql(value = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class DeleteGroupControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private GroupRepository groupRepository;

    @Test
    void deleteGroup_shouldReturn204AndRemoveFromDb() {
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/groups/{groupId}",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class,
                1L
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(groupRepository.findById(1L)).isEmpty();
    }

    @Test
    void deleteGroup_whenNotExists_shouldReturn404() {
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/groups/{groupId}",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class,
                99999L
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
