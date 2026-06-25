package com.lms.lms_system_management.controller.student;

import com.lms.lms_system_management.controller.BaseIntegrationTest;
import com.lms.lms_system_management.dao.StudentRepository;
import com.lms.lms_system_management.model.StudentEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("/sql/insert-group.sql")
@Sql(value = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class PostStudentEntityControllerTest extends BaseIntegrationTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void createStudent_shouldReturn201AndCorrectBody() throws Exception {
        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("student/create-request.json")))
                .andExpect(status().isCreated())
                .andExpect(content().json(readJson("student/create-response.json")));

        List<StudentEntity> students = studentRepository.findAll();
        assertThat(students.size()).isEqualTo(1);
        assertThat(students.get(0).getFirstName()).isEqualTo("Petr");
        assertThat(students.get(0).getLastName()).isEqualTo("Petrov");
    }

    @ParameterizedTest
    @MethodSource("invalidNewStudentRequest")
    void createStudent_whenIsBlank_shouldReturn400(String requestJson) throws Exception {
        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    static Stream<String> invalidNewStudentRequest() {
        return Stream.of(
                "{\"firstName\": \"\", \"lastName\": \"Petrov\", \"groupId\": 1}",
                "{\"firstName\": \"Petr\", \"lastName\": \"\", \"groupId\": 1}",
                "{\"firstName\": \"Petr\", \"lastName\": \"Petrov\", \"groupId\": null}"
        );
    }

    @Test
    void createStudent_whenGroupNotExists_shouldReturn404() throws Exception {
        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("student/create-not-found-request.json")))
                .andExpect(status().isNotFound());
    }
}
