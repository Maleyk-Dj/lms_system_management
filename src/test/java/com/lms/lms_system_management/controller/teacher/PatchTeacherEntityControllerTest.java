package com.lms.lms_system_management.controller.teacher;

import com.lms.lms_system_management.controller.BaseIntegrationTest;
import com.lms.lms_system_management.dao.TeacherRepository;
import com.lms.lms_system_management.model.TeacherEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("/sql/insert-teacher.sql")
@Sql(value = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class PatchTeacherEntityControllerTest extends BaseIntegrationTest {

    @Autowired
    private TeacherRepository teacherRepository;

    @Test
    void updateTeacher_shouldReturn200AndUpdatedBody() throws Exception {
        mockMvc.perform(patch("/api/teachers/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("teacher/update-request.json")))
                .andExpect(status().isOk())
                .andExpect(content().json(readJson("teacher/update-response.json")));

        List<TeacherEntity> teachers = teacherRepository.findAll();
        assertThat(teachers.size()).isEqualTo(1);
        assertThat(teachers.get(0).getFirstName()).isEqualTo("Anna");
        assertThat(teachers.get(0).getLastName()).isEqualTo("Ivanova");
    }

    @Test
    void updateTeacher_whenFirstNameIsBlank_shouldReturn400() throws Exception {
        mockMvc.perform(patch("/api/teachers/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"\", \"lastName\": \"Ivanova\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateTeacher_whenNotExists_shouldReturn404() throws Exception {
        mockMvc.perform(patch("/api/teachers/{id}", 99999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("teacher/update-request.json")))
                .andExpect(status().isNotFound());
    }
}
