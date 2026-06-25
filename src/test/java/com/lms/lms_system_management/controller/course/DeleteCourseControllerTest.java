package com.lms.lms_system_management.controller.course;

import com.lms.lms_system_management.controller.BaseIntegrationTest;
import com.lms.lms_system_management.dao.CourseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("/sql/insert-course.sql")
@Sql(value = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class DeleteCourseControllerTest extends BaseIntegrationTest {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    void deleteCourse_shouldReturn204AndRemoveFromDb() throws Exception {
        mockMvc.perform(delete("/api/courses/{id}", 1))
                .andExpect(status().isNoContent());

        assertThat(courseRepository.findById(1L)).isEmpty();
    }

    @Test
    void deleteCourse_whenNotExists_shouldReturn404() throws Exception {
        mockMvc.perform(delete("/api/courses/{id}", 99999))
                .andExpect(status().isNotFound());
    }
}
