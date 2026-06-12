package com.lms.lms_system_management;

import com.lms.lms_system_management.dao.GroupRepository;
import com.lms.lms_system_management.dto.request.NewGroupRequest;
import com.lms.lms_system_management.dto.request.UpdateGroupRequest;
import com.lms.lms_system_management.dto.response.GroupResponse;
import com.lms.lms_system_management.model.Group;
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
public class GroupControllerTest {

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

    // CREATE

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

    // GET

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

    // UPDATE

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

    // DELETE

    @Test
    void deleteGroup_shouldReturn204AndRemoveFromDb() {
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/groups/{groupId}",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class,
                groupId
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(groupRepository.findById(groupId)).isEmpty();
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
