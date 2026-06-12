package com.lms.lms_system_management.controller.group;

import com.lms.lms_system_management.dao.GroupRepository;
import com.lms.lms_system_management.dto.request.NewGroupRequest;
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
public class PostGroupControllerTest {

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
