package com.lms.lms_system_management.controller.group;

import com.lms.lms_system_management.TestcontainersConfiguration;
import com.lms.lms_system_management.dao.GroupRepository;
import com.lms.lms_system_management.dao.StudentRepository;
import com.lms.lms_system_management.dto.group.GroupResponse;
import com.lms.lms_system_management.model.GroupEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
class GetGroupControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private StudentRepository studentRepository;

    private Long groupId;

    @BeforeEach
    public void setUp() {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setName("Gruppa A");
        groupRepository.save(groupEntity);
        groupId = groupEntity.getId();
    }

    @AfterEach
    public void tearDown() {
        studentRepository.deleteAllInBatch();
        groupRepository.deleteAllInBatch();
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
