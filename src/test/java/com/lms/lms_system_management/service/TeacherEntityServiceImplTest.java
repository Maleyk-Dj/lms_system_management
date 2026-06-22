package com.lms.lms_system_management.service;

import com.lms.lms_system_management.dao.ScheduleRepository;
import com.lms.lms_system_management.dao.TeacherRepository;
import com.lms.lms_system_management.dto.teacher.NewTeacherRequest;
import com.lms.lms_system_management.dto.teacher.TeacherFilter;
import com.lms.lms_system_management.dto.teacher.UpdateTeacherRequest;
import com.lms.lms_system_management.dto.teacher.TeacherResponse;
import com.lms.lms_system_management.dto.schedule.ScheduleResponse;
import com.lms.lms_system_management.exception.NotFoundException;
import com.lms.lms_system_management.mapper.ScheduleMapper;
import com.lms.lms_system_management.mapper.TeacherMapper;
import com.lms.lms_system_management.model.ScheduleEntity;
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
class TeacherEntityServiceImplTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private TeacherMapper teacherMapper;

    @Mock
    private ScheduleMapper scheduleMapper;

    @InjectMocks
    private TeacherServiceImpl teacherService;

    // CREATE

    @Test
    void create_shouldSaveAndReturnResponse() {
        NewTeacherRequest request = new NewTeacherRequest("Ivan", "Petrov");

        TeacherEntity entity = new TeacherEntity();
        entity.setFirstName("Ivan");
        entity.setLastName("Petrov");

        TeacherEntity saved = new TeacherEntity();
        saved.setId(1L);
        saved.setFirstName("Ivan");
        saved.setLastName("Petrov");

        TeacherResponse expected = new TeacherResponse(1L, "Ivan", "Petrov");

        when(teacherMapper.toEntity(request)).thenReturn(entity);
        when(teacherRepository.save(entity)).thenReturn(saved);
        when(teacherMapper.toResponse(saved)).thenReturn(expected);

        TeacherResponse result = teacherService.create(request);

        assertThat(result).isEqualTo(expected);
        verify(teacherRepository).save(entity);
    }

    // GET BY ID

    @Test
    void getById_whenExists_shouldReturnResponse() {
        TeacherEntity teacherEntity = new TeacherEntity();
        teacherEntity.setId(1L);
        teacherEntity.setFirstName("Ivan");
        teacherEntity.setLastName("Petrov");

        TeacherResponse expected = new TeacherResponse(1L, "Ivan", "Petrov");

        when(teacherRepository.findByIdOrThrow(1L)).thenReturn(teacherEntity);
        when(teacherMapper.toResponse(teacherEntity)).thenReturn(expected);

        TeacherResponse result = teacherService.getById(1L);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void getById_whenNotExists_shouldThrowNotFoundException() {
        when(teacherRepository.findByIdOrThrow(99L)).thenThrow(new NotFoundException("not found"));

        assertThatThrownBy(() -> teacherService.getById(99L))
                .isInstanceOf(NotFoundException.class);
    }

    // GET ALL

    @Test
    void getAll_shouldReturnListOfResponses() {
        TeacherFilter filter = new TeacherFilter(null, null);
        Pageable pageable = PageRequest.of(0, 10);

        TeacherEntity t1 = new TeacherEntity();
        t1.setId(1L);
        t1.setFirstName("Ivan");
        t1.setLastName("Petrov");

        TeacherEntity t2 = new TeacherEntity();
        t2.setId(2L);
        t2.setFirstName("Anna");
        t2.setLastName("Ivanova");

        TeacherResponse r1 = new TeacherResponse(1L, "Ivan", "Petrov");
        TeacherResponse r2 = new TeacherResponse(2L, "Anna", "Ivanova");

        when(teacherRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(t1, t2)));
        when(teacherMapper.toResponse(t1)).thenReturn(r1);
        when(teacherMapper.toResponse(t2)).thenReturn(r2);

        Page<TeacherResponse> result = teacherService.getAll(filter, pageable);

        assertThat(result.getContent()).hasSize(2).containsExactly(r1, r2);
    }

    @Test
    void getAll_whenEmpty_shouldReturnEmptyList() {
        TeacherFilter filter = new TeacherFilter(null, null);
        Pageable pageable = PageRequest.of(0, 10);

        when(teacherRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of()));

        assertThat(teacherService.getAll(filter, pageable).getContent()).isEmpty();
    }

    // UPDATE

    @Test
    void update_whenExists_shouldUpdateAndReturnResponse() {
        UpdateTeacherRequest request = new UpdateTeacherRequest("Anna", "Ivanova");

        TeacherEntity teacherEntity = new TeacherEntity();
        teacherEntity.setId(1L);
        teacherEntity.setFirstName("Ivan");
        teacherEntity.setLastName("Petrov");

        TeacherEntity saved = new TeacherEntity();
        saved.setId(1L);
        saved.setFirstName("Anna");
        saved.setLastName("Ivanova");

        TeacherResponse expected = new TeacherResponse(1L, "Anna", "Ivanova");

        when(teacherRepository.findByIdOrThrow(1L)).thenReturn(teacherEntity);
        when(teacherRepository.save(teacherEntity)).thenReturn(saved);
        when(teacherMapper.toResponse(saved)).thenReturn(expected);

        TeacherResponse result = teacherService.update(request, 1L);

        assertThat(result).isEqualTo(expected);
        verify(teacherMapper).updateEntity(request, teacherEntity);
    }

    @Test
    void update_whenNotExists_shouldThrowNotFoundException() {
        UpdateTeacherRequest request = new UpdateTeacherRequest("Anna", "Ivanova");
        when(teacherRepository.findByIdOrThrow(99L)).thenThrow(new NotFoundException("not found"));

        assertThatThrownBy(() -> teacherService.update(request, 99L))
                .isInstanceOf(NotFoundException.class);
        verify(teacherRepository, never()).save(any());
    }

    // DELETE

    @Test
    void deleteById_whenExists_shouldDelete() {
        when(teacherRepository.softDeletedById(1L)).thenReturn(1);

        teacherService.deleteById(1L);

        verify(teacherRepository).softDeletedById(1L);
    }

    @Test
    void deleteById_whenNotExists_shouldThrowNotFoundException() {
        when(teacherRepository.softDeletedById(99L)).thenReturn(0);

        assertThatThrownBy(() -> teacherService.deleteById(99L))
                .isInstanceOf(NotFoundException.class);
        verify(teacherRepository, never()).delete(any(TeacherEntity.class));
    }

    // GET SCHEDULE BY TEACHER

    @Test
    void getScheduleByTeacher_whenHasSchedules_shouldReturnList() {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(10L);

        ScheduleResponse scheduleResponse = new ScheduleResponse(10L, null, null, null);

        when(scheduleRepository.findByCourseEntityTeacherEntityId(1L)).thenReturn(List.of(scheduleEntity));
        when(scheduleMapper.toResponse(scheduleEntity)).thenReturn(scheduleResponse);

        List<ScheduleResponse> result = teacherService.getScheduleByTeacher(1L);

        assertThat(result).hasSize(1).containsExactly(scheduleResponse);
    }

    @Test
    void getScheduleByTeacher_whenNoSchedules_shouldReturnEmptyList() {
        when(scheduleRepository.findByCourseEntityTeacherEntityId(1L)).thenReturn(List.of());

        List<ScheduleResponse> result = teacherService.getScheduleByTeacher(1L);

        assertThat(result).isEmpty();
    }

    @Test
    void getScheduleByTeacher_whenTeacherNotExists_shouldReturnEmptyList() {
        when(scheduleRepository.findByCourseEntityTeacherEntityId(99L)).thenReturn(List.of());

        List<ScheduleResponse> result = teacherService.getScheduleByTeacher(99L);

        assertThat(result).isEmpty();
    }
}
