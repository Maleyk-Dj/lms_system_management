package com.lms.lms_system_management.service;

import com.lms.lms_system_management.dao.CourseRepository;
import com.lms.lms_system_management.dao.TeacherRepository;
import com.lms.lms_system_management.dto.course.NewCourseRequest;
import com.lms.lms_system_management.dto.course.UpdateCourseRequest;
import com.lms.lms_system_management.dto.course.CourseResponse;
import com.lms.lms_system_management.dto.teacher.TeacherResponse;
import com.lms.lms_system_management.exception.NotFoundException;
import com.lms.lms_system_management.mapper.CourseMapper;
import com.lms.lms_system_management.model.CourseEntity;
import com.lms.lms_system_management.model.TeacherEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CourseMapper courseMapper;

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    // CREATE

    @Test
    void create_whenTeacherExists_shouldSaveAndReturnResponse() {
        TeacherEntity teacherEntity = TeacherEntity.builder().id(1L).firstName("Ivan").lastName("Petrov").build();
        NewCourseRequest request = new NewCourseRequest("Java", "Kurs po Java", 1L);
        CourseEntity entity = CourseEntity.builder().name("Java").description("Kurs po Java").teacherEntity(teacherEntity).build();
        CourseEntity saved = CourseEntity.builder().id(10L).name("Java").description("Kurs po Java").teacherEntity(teacherEntity).build();
        CourseResponse expected = new CourseResponse(10L, "Java", "Kurs po Java",
                new TeacherResponse(1L, "Ivan", "Petrov"));

        when(teacherRepository.findByIdOrThrow(1L)).thenReturn(teacherEntity);
        when(courseMapper.toEntity(request, teacherEntity)).thenReturn(entity);
        when(courseRepository.save(entity)).thenReturn(saved);
        when(courseMapper.toResponse(saved)).thenReturn(expected);

        CourseResponse result = courseService.create(request);

        assertThat(result).isEqualTo(expected);
        verify(courseRepository).save(entity);
    }

    @Test
    void create_whenTeacherNotExists_shouldThrowNotFoundException() {
        NewCourseRequest request = new NewCourseRequest("Java", "Kurs po Java", 99L);
        when(teacherRepository.findByIdOrThrow(99L)).thenThrow(new NotFoundException("not found"));

        assertThatThrownBy(() -> courseService.create(request))
                .isInstanceOf(NotFoundException.class);
        verify(courseRepository, never()).save(any());
    }

    // FIND BY ID

    @Test
    void getById_whenExists_shouldReturnResponse() {
        TeacherEntity teacherEntity = TeacherEntity.builder().id(1L).build();
        CourseEntity courseEntity = CourseEntity.builder().id(5L).name("Java").description("Desc").teacherEntity(teacherEntity).build();
        CourseResponse expected = new CourseResponse(5L, "Java", "Desc",
                new TeacherResponse(1L, null, null));

        when(courseRepository.findByIdOrThrow(5L)).thenReturn(courseEntity);
        when(courseMapper.toResponse(courseEntity)).thenReturn(expected);

        CourseResponse result = courseService.getById(5L);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void getById_whenNotExists_shouldThrowNotFoundException() {
        when(courseRepository.findByIdOrThrow(99L)).thenThrow(new NotFoundException("not found"));

        assertThatThrownBy(() -> courseService.getById(99L))
                .isInstanceOf(NotFoundException.class);
    }

    // FIND ALL

    @Test
    void findAll_shouldReturnListOfResponses() {
        TeacherEntity teacherEntity = TeacherEntity.builder().id(1L).build();
        CourseEntity c1 = CourseEntity.builder().id(1L).name("Java").description("D1").teacherEntity(teacherEntity).build();
        CourseEntity c2 = CourseEntity.builder().id(2L).name("Spring").description("D2").teacherEntity(teacherEntity).build();
        CourseResponse r1 = new CourseResponse(1L, "Java", "D1", null);
        CourseResponse r2 = new CourseResponse(2L, "Spring", "D2", null);

        when(courseRepository.findAll()).thenReturn(List.of(c1, c2));
        when(courseMapper.toResponse(c1)).thenReturn(r1);
        when(courseMapper.toResponse(c2)).thenReturn(r2);

        List<CourseResponse> result = courseService.findAll();

        assertThat(result).hasSize(2).containsExactly(r1, r2);
    }

    @Test
    void findAll_whenEmpty_shouldReturnEmptyList() {
        when(courseRepository.findAll()).thenReturn(List.of());

        assertThat(courseService.findAll()).isEmpty();
    }

    // UPDATE

    @Test
    void update_whenCourseAndTeacherExist_shouldUpdateAndReturnResponse() {
        TeacherEntity oldTeacherEntity = TeacherEntity.builder().id(1L).build();
        TeacherEntity newTeacherEntity = TeacherEntity.builder().id(2L).firstName("Anna").lastName("Ivanova").build();
        CourseEntity courseEntity = CourseEntity.builder().id(5L).name("Java").description("Desc").teacherEntity(oldTeacherEntity).build();
        UpdateCourseRequest request = new UpdateCourseRequest("Kotlin", "Kurs po Kotlin", 2L);
        CourseEntity saved = CourseEntity.builder().id(5L).name("Kotlin").description("Kurs po Kotlin").teacherEntity(newTeacherEntity).build();
        CourseResponse expected = new CourseResponse(5L, "Kotlin", "Kurs po Kotlin",
                new TeacherResponse(2L, "Anna", "Ivanova"));

        when(courseRepository.findByIdOrThrow(5L)).thenReturn(courseEntity);
        when(teacherRepository.findByIdOrThrow(2L)).thenReturn(newTeacherEntity);
        when(courseRepository.save(courseEntity)).thenReturn(saved);
        when(courseMapper.toResponse(saved)).thenReturn(expected);

        CourseResponse result = courseService.update(request, 5L);

        assertThat(result).isEqualTo(expected);
        verify(courseMapper).updateEntity(request, newTeacherEntity, courseEntity);
    }

    @Test
    void update_whenCourseNotExists_shouldThrowNotFoundException() {
        UpdateCourseRequest request = new UpdateCourseRequest("Kotlin", "Desc", 1L);
        when(courseRepository.findByIdOrThrow(99L)).thenThrow(new NotFoundException("not found"));

        assertThatThrownBy(() -> courseService.update(request, 99L))
                .isInstanceOf(NotFoundException.class);
        verify(courseRepository, never()).save(any());
    }

    @Test
    void update_whenTeacherNotExists_shouldThrowNotFoundException() {
        TeacherEntity teacherEntity = TeacherEntity.builder().id(1L).build();
        CourseEntity courseEntity = CourseEntity.builder().id(5L).name("Java").description("Desc").teacherEntity(teacherEntity).build();
        UpdateCourseRequest request = new UpdateCourseRequest("Kotlin", "Desc", 99L);

        when(courseRepository.findByIdOrThrow(5L)).thenReturn(courseEntity);
        when(teacherRepository.findByIdOrThrow(99L)).thenThrow(new NotFoundException("not found"));

        assertThatThrownBy(() -> courseService.update(request, 5L))
                .isInstanceOf(NotFoundException.class);
        verify(courseRepository, never()).save(any());
    }

    // DELETE

    @Test
    void delete_whenExists_shouldDeleteById() {
        TeacherEntity teacherEntity = TeacherEntity.builder().id(1L).build();
        CourseEntity courseEntity = CourseEntity.builder().id(5L).teacherEntity(teacherEntity).build();
        when(courseRepository.findByIdOrThrow(5L)).thenReturn(courseEntity);

        courseService.deleteById(5L);

        verify(courseRepository).delete(courseEntity);
    }

    @Test
    void delete_ById_whenNotExists_shouldThrowNotFoundException() {
        when(courseRepository.findByIdOrThrow(99L)).thenThrow(new NotFoundException("not found"));

        assertThatThrownBy(() -> courseService.deleteById(99L))
                .isInstanceOf(NotFoundException.class);
        verify(courseRepository, never()).delete(any());
    }
}
