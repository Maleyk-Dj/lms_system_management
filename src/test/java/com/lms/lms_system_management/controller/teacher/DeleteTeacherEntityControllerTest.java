package com.lms.lms_system_management.controller.teacher;

import com.lms.lms_system_management.controller.BaseIntegrationTest;
import com.lms.lms_system_management.dao.TeacherRepository;
import com.lms.lms_system_management.model.TeacherEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("/sql/insert-teacher.sql")
@Sql(value = "/sql/clean.sql",executionPhase =  Sql.ExecutionPhase.AFTER_TEST_METHOD)
class DeleteTeacherEntityControllerTest extends BaseIntegrationTest {

    @Autowired
    private TeacherRepository teacherRepository;
    @Test
    void deleteTeacher_shouldReturn204AndRemoveFromDb() throws Exception {
        mockMvc.perform(delete("/api/teachers/{id}", 1))
                .andExpect(status().isNoContent());

        TeacherEntity teacher = teacherRepository.findByIdIgnoringDeletedFlag(1L).orElseThrow();
        assertThat(teacher.getDeleted()).isTrue();
    }

    @Test
    void deleteTeacher_whenNotExists_shouldReturn404() throws Exception {
        mockMvc.perform(delete("/api/teachers/{id}", 99999))
                .andExpect(status().isNotFound());
    }
}
