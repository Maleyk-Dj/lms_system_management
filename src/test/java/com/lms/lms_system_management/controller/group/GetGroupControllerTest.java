package com.lms.lms_system_management.controller.group;

import com.lms.lms_system_management.dao.GroupRepository;
import com.lms.lms_system_management.dto.response.GroupResponse;
import com.lms.lms_system_management.model.Group;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
public class GetGroupControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private GroupRepository groupRepository;

    private Long groupId;

    @BeforeEach
    public void setUp() {
        Group group = Group.builder()
                .name("Gruppa A")
                .build();
        groupRepository.save(group);
        groupId = group.getId();
    }

    @AfterEach
    public void tearDown() {
        groupRepository.deleteAll();
    }
    @Test
    void getGroupById_shouldReturn200AndCorrectBody() {
        ResponseEntity<GroupResponse> response = restTemplate.getForEntity(
                "/api/groups/{groupId}",
                GroupResponse.class,
                groupId
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        GroupResponse body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.id()).isEqualTo(groupId);
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
    void getAllGroups_shouldReturn200AndNonEmptyList() {
        ResponseEntity<GroupResponse[]> response = restTemplate.getForEntity(
                "/api/groups",
                GroupResponse[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        GroupResponse[] body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.length).isGreaterThan(0);
    }
}
