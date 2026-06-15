package com.lms.lms_system_management.controller.group;

import com.lms.lms_system_management.TestcontainersConfiguration;
import com.lms.lms_system_management.dao.GroupRepository;
import com.lms.lms_system_management.dto.group.UpdateGroupRequest;
import com.lms.lms_system_management.dto.group.GroupResponse;
import com.lms.lms_system_management.model.GroupEntity;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
class PutGroupControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private GroupRepository groupRepository;

    private Long groupId;

    @BeforeEach
    public void setUp() {
        GroupEntity groupEntity = GroupEntity.builder()
                .name("Gruppa A")
                .build();
        groupRepository.save(groupEntity);
        groupId = groupEntity.getId();
    }

    @AfterEach
    public void tearDown() {
        groupRepository.deleteAll();
    }

    @Test
    void updateGroup_shouldReturn200AndUpdatedBody() {
        UpdateGroupRequest request = new UpdateGroupRequest("Gruppa C");

        ResponseEntity<GroupResponse> response = restTemplate.exchange(
                "/api/groups/{groupId}",
                HttpMethod.PUT,
                new HttpEntity<>(request),
                GroupResponse.class,
                groupId
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        GroupResponse body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.id()).isEqualTo(groupId);
        assertThat(body.name()).isEqualTo("Gruppa C");
    }

    @Test
    void updateGroup_whenNameIsBlank_shouldReturn400() {
        UpdateGroupRequest request = new UpdateGroupRequest("");

        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/groups/{groupId}",
                HttpMethod.PUT,
                new HttpEntity<>(request),
                Void.class,
                groupId
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void updateGroup_whenNotExists_shouldReturn404() {
        UpdateGroupRequest request = new UpdateGroupRequest("Gruppa D");

        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/groups/{groupId}",
                HttpMethod.PUT,
                new HttpEntity<>(request),
                Void.class,
                99999L
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}
