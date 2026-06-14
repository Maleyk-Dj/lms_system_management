package com.lms.lms_system_management.service;

import com.lms.lms_system_management.dao.ScheduleRepository;
import com.lms.lms_system_management.dao.TeacherRepository;
import com.lms.lms_system_management.dto.teacher.NewTeacherRequest;
import com.lms.lms_system_management.dto.teacher.UpdateTeacherRequest;
import com.lms.lms_system_management.dto.teacher.TeacherResponse;
import com.lms.lms_system_management.dto.schedule.ScheduleResponse;
import com.lms.lms_system_management.exception.NotFoundException;
import com.lms.lms_system_management.mapper.ScheduleMapper;
import com.lms.lms_system_management.mapper.TeacherMapper;
import com.lms.lms_system_management.model.Schedule;
import com.lms.lms_system_management.model.Teacher;
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
class TeacherServiceImplTest {

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
        Teacher entity = Teacher.builder().firstName("Ivan").lastName("Petrov").build();
        Teacher saved = Teacher.builder().id(1L).firstName("Ivan").lastName("Petrov").build();
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
        Teacher teacher = Teacher.builder().id(1L).firstName("Ivan").lastName("Petrov").build();
        TeacherResponse expected = new TeacherResponse(1L, "Ivan", "Petrov");

        when(teacherRepository.findByIdOrThrow(1L)).thenReturn(teacher);
        when(teacherMapper.toResponse(teacher)).thenReturn(expected);

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
        Teacher t1 = Teacher.builder().id(1L).firstName("Ivan").lastName("Petrov").build();
        Teacher t2 = Teacher.builder().id(2L).firstName("Anna").lastName("Ivanova").build();
        TeacherResponse r1 = new TeacherResponse(1L, "Ivan", "Petrov");
        TeacherResponse r2 = new TeacherResponse(2L, "Anna", "Ivanova");

        when(teacherRepository.findAll()).thenReturn(List.of(t1, t2));
        when(teacherMapper.toResponse(t1)).thenReturn(r1);
        when(teacherMapper.toResponse(t2)).thenReturn(r2);

        List<TeacherResponse> result = teacherService.getAll();

        assertThat(result).hasSize(2).containsExactly(r1, r2);
    }

    @Test
    void getAll_whenEmpty_shouldReturnEmptyList() {
        when(teacherRepository.findAll()).thenReturn(List.of());

        assertThat(teacherService.getAll()).isEmpty();
    }

    // UPDATE

    @Test
    void update_whenExists_shouldUpdateAndReturnResponse() {
        UpdateTeacherRequest request = new UpdateTeacherRequest("Anna", "Ivanova");
        Teacher teacher = Teacher.builder().id(1L).firstName("Ivan").lastName("Petrov").build();
        Teacher saved = Teacher.builder().id(1L).firstName("Anna").lastName("Ivanova").build();
        TeacherResponse expected = new TeacherResponse(1L, "Anna", "Ivanova");

        when(teacherRepository.findByIdOrThrow(1L)).thenReturn(teacher);
        when(teacherRepository.save(teacher)).thenReturn(saved);
        when(teacherMapper.toResponse(saved)).thenReturn(expected);

        TeacherResponse result = teacherService.update(request, 1L);

        assertThat(result).isEqualTo(expected);
        verify(teacherMapper).updateEntity(request, teacher);
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
        Teacher teacher = Teacher.builder().id(1L).firstName("Ivan").lastName("Petrov").build();
        when(teacherRepository.findByIdOrThrow(1L)).thenReturn(teacher);

        teacherService.deleteById(1L);

        verify(teacherRepository).delete(teacher);
    }

    @Test
    void deleteById_whenNotExists_shouldThrowNotFoundException() {
        when(teacherRepository.findByIdOrThrow(99L)).thenThrow(new NotFoundException("not found"));

        assertThatThrownBy(() -> teacherService.deleteById(99L))
                .isInstanceOf(NotFoundException.class);
        verify(teacherRepository, never()).delete(any());
    }

    // GET SCHEDULE BY TEACHER

    @Test
    void getScheduleByTeacher_whenExists_shouldReturnSchedules() {
        Teacher teacher = Teacher.builder().id(1L).build();
        Schedule schedule = Schedule.builder().id(10L).build();
        ScheduleResponse scheduleResponse = new ScheduleResponse(10L, null, null, null);

        when(teacherRepository.findByIdOrThrow(1L)).thenReturn(teacher);
        when(scheduleRepository.findByCourseTeacherId(1L)).thenReturn(List.of(schedule));
        when(scheduleMapper.toResponse(schedule)).thenReturn(scheduleResponse);

        List<ScheduleResponse> result = teacherService.getScheduleByTeacher(1L);

        assertThat(result).hasSize(1).containsExactly(scheduleResponse);
    }

    @Test
    void getScheduleByTeacher_whenNoSchedules_shouldReturnEmptyList() {
        Teacher teacher = Teacher.builder().id(1L).build();
        when(teacherRepository.findByIdOrThrow(1L)).thenReturn(teacher);
        when(scheduleRepository.findByCourseTeacherId(1L)).thenReturn(List.of());

        List<ScheduleResponse> result = teacherService.getScheduleByTeacher(1L);

        assertThat(result).isEmpty();
    }

    @Test
    void getScheduleByTeacher_whenTeacherNotExists_shouldThrowNotFoundException() {
        when(teacherRepository.findByIdOrThrow(99L)).thenThrow(new NotFoundException("not found"));

        assertThatThrownBy(() -> teacherService.getScheduleByTeacher(99L))
                .isInstanceOf(NotFoundException.class);
    }
}
