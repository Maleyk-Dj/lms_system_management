package com.lms.lms_system_management.service;

import com.lms.lms_system_management.dao.CourseRepository;
import com.lms.lms_system_management.dao.TeacherRepository;
import com.lms.lms_system_management.dto.course.CourseFilter;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

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
        TeacherEntity teacherEntity = new TeacherEntity();
        teacherEntity.setId(1L);
        teacherEntity.setFirstName("Ivan");
        teacherEntity.setLastName("Petrov");

        NewCourseRequest request = new NewCourseRequest("Java", "Kurs po Java", 1L);

        CourseEntity entity = new CourseEntity();
        entity.setName("Java");
        entity.setDescription("Kurs po Java");
        entity.setTeacherEntity(teacherEntity);

        CourseEntity saved = new CourseEntity();
        saved.setId(10L);
        saved.setName("Java");
        saved.setDescription("Kurs po Java");
        saved.setTeacherEntity(teacherEntity);

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
        TeacherEntity teacherEntity = new TeacherEntity();
        teacherEntity.setId(1L);

        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setId(5L);
        courseEntity.setName("Java");
        courseEntity.setDescription("Desc");
        courseEntity.setTeacherEntity(teacherEntity);

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
    void findAll_shouldReturnPagedResponses() {
        CourseFilter filter = new CourseFilter(null, null);
        Pageable pageable = PageRequest.of(0, 10);

        TeacherEntity teacherEntity = new TeacherEntity();
        teacherEntity.setId(1L);

        CourseEntity c1 = new CourseEntity();
        c1.setId(1L);
        c1.setName("Java");
        c1.setDescription("D1");
        c1.setTeacherEntity(teacherEntity);

        CourseEntity c2 = new CourseEntity();
        c2.setId(2L);
        c2.setName("Spring");
        c2.setDescription("D2");
        c2.setTeacherEntity(teacherEntity);

        CourseResponse r1 = new CourseResponse(1L, "Java", "D1", null);
        CourseResponse r2 = new CourseResponse(2L, "Spring", "D2", null);

        when(courseRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(c1, c2)));
        when(courseMapper.toResponse(c1)).thenReturn(r1);
        when(courseMapper.toResponse(c2)).thenReturn(r2);

        Page<CourseResponse> result = courseService.findAll(filter, pageable);

        assertThat(result.getContent()).hasSize(2).containsExactly(r1, r2);
    }

    @Test
    void findAll_whenEmpty_shouldReturnEmptyPage() {
        CourseFilter filter = new CourseFilter(null, null);
        Pageable pageable = PageRequest.of(0, 10);

        when(courseRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of()));

        assertThat(courseService.findAll(filter, pageable).getContent()).isEmpty();
    }

    // UPDATE

    @Test
    void update_whenCourseAndTeacherExist_shouldUpdateAndReturnResponse() {
        TeacherEntity oldTeacherEntity = new TeacherEntity();
        oldTeacherEntity.setId(1L);

        TeacherEntity newTeacherEntity = new TeacherEntity();
        newTeacherEntity.setId(2L);
        newTeacherEntity.setFirstName("Anna");
        newTeacherEntity.setLastName("Ivanova");

        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setId(5L);
        courseEntity.setName("Java");
        courseEntity.setDescription("Desc");
        courseEntity.setTeacherEntity(oldTeacherEntity);

        UpdateCourseRequest request = new UpdateCourseRequest("Kotlin", "Kurs po Kotlin", 2L);

        CourseEntity saved = new CourseEntity();
        saved.setId(5L);
        saved.setName("Kotlin");
        saved.setDescription("Kurs po Kotlin");
        saved.setTeacherEntity(newTeacherEntity);

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
        TeacherEntity teacherEntity = new TeacherEntity();
        teacherEntity.setId(1L);

        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setId(5L);
        courseEntity.setName("Java");
        courseEntity.setDescription("Desc");
        courseEntity.setTeacherEntity(teacherEntity);

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
        when(courseRepository.softDeleteById(5L)).thenReturn(1);

        courseService.deleteById(5L);

        verify(courseRepository).softDeleteById(5L);
    }

    @Test
    void delete_ById_whenNotExists_shouldThrowNotFoundException() {
        when(courseRepository.softDeleteById(99L)).thenReturn(0);

        assertThatThrownBy(() -> courseService.deleteById(99L))
                .isInstanceOf(NotFoundException.class);
        verify(courseRepository, never()).delete(any(CourseEntity.class));
    }
}
