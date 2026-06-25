package com.lms.lms_system_management.controller.teacher;

import com.lms.lms_system_management.controller.BaseIntegrationTest;
import com.lms.lms_system_management.dao.TeacherRepository;
import com.lms.lms_system_management.model.TeacherEntity;
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

@Sql(value = "/sql/clean.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class PostTeacherEntityControllerTest extends BaseIntegrationTest {

    @Autowired
    private TeacherRepository teacherRepository;

    @Test
    void createTeacher_shouldReturn201AndCorrectBody() throws Exception {
        mockMvc.perform(post("/api/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("teacher/create-request.json")))
                .andExpect(status().isCreated())
                .andExpect(content().json(readJson("teacher/create-response.json")));

        List<TeacherEntity> teachers = teacherRepository.findAll();
        assertThat(teachers.size()).isEqualTo(1);
        assertThat(teachers.get(0).getFirstName()).isEqualTo("Oleg");
        assertThat(teachers.get(0).getLastName()).isEqualTo("Smirnov");
    }

    @ParameterizedTest
    @MethodSource("invalidNewTeacherRequest")
    void createTeacher_whenFirstNameIsInvalid_shouldReturn400(String requestJson) throws Exception {
        mockMvc.perform(post("/api/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }
    static Stream<String> invalidNewTeacherRequest() {
        return Stream.of(
                "{\"firstName\": \"\", \"lastName\": \"Smirnov\"}",
                "{\"firstName\": \" \", \"lastName\": \"Smirnov\"}"
        );
    }
}
