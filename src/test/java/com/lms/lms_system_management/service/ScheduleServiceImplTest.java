package com.lms.lms_system_management.service;

import com.lms.lms_system_management.dao.CourseRepository;
import com.lms.lms_system_management.dao.GroupRepository;
import com.lms.lms_system_management.dao.ScheduleRepository;
import com.lms.lms_system_management.dto.request.NewScheduleRequest;
import com.lms.lms_system_management.dto.request.UpdateScheduleRequest;
import com.lms.lms_system_management.dto.response.ScheduleResponse;
import com.lms.lms_system_management.exception.NotFoundException;
import com.lms.lms_system_management.mapper.ScheduleMapper;
import com.lms.lms_system_management.model.Course;
import com.lms.lms_system_management.model.Group;
import com.lms.lms_system_management.model.Schedule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private ScheduleMapper scheduleMapper;

    @InjectMocks
    private ScheduleServiceImpl scheduleService;

    private static final LocalDateTime DATE = LocalDateTime.of(2025, 9, 1, 10, 0);

    // ASSIGN COURSE TIME (CREATE)

    @Test
    void assignCourseTime_whenGroupAndCourseExist_shouldSaveAndReturnResponse() {
        Group group = Group.builder().id(1L).build();
        Course course = Course.builder().id(1L).build();
        NewScheduleRequest request = new NewScheduleRequest(1L, 1L, DATE);
        Schedule entity = Schedule.builder().id(1L).group(group).course(course).date(DATE).build();
        ScheduleResponse expected = new ScheduleResponse(1L, null, null, DATE);

        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(scheduleMapper.toEntity(request, group, course)).thenReturn(entity);
        when(scheduleRepository.save(entity)).thenReturn(entity);
        when(scheduleMapper.toResponse(entity)).thenReturn(expected);

        ScheduleResponse result = scheduleService.assignCourseTime(request);

        assertThat(result).isEqualTo(expected);
        verify(scheduleRepository).save(entity);
    }

    @Test
    void assignCourseTime_whenGroupNotExists_shouldThrowNotFoundException() {
        NewScheduleRequest request = new NewScheduleRequest(99L, 1L, DATE);
        when(groupRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> scheduleService.assignCourseTime(request))
                .isInstanceOf(NotFoundException.class);
        verify(scheduleRepository, never()).save(any());
    }

    @Test
    void assignCourseTime_whenCourseNotExists_shouldThrowNotFoundException() {
        Group group = Group.builder().id(1L).build();
        NewScheduleRequest request = new NewScheduleRequest(1L, 99L, DATE);

        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> scheduleService.assignCourseTime(request))
                .isInstanceOf(NotFoundException.class);
        verify(scheduleRepository, never()).save(any());
    }

    // UPDATE

    @Test
    void update_whenAllExist_shouldUpdateAndReturnResponse() {
        Group group = Group.builder().id(1L).build();
        Course course = Course.builder().id(1L).build();
        Schedule schedule = Schedule.builder().id(10L).group(group).course(course).date(DATE).build();
        LocalDateTime newDate = LocalDateTime.of(2025, 10, 1, 12, 0);
        UpdateScheduleRequest request = new UpdateScheduleRequest(1L, 1L, newDate);
        Schedule saved = Schedule.builder().id(10L).group(group).course(course).date(newDate).build();
        ScheduleResponse expected = new ScheduleResponse(10L, null, null, newDate);

        when(scheduleRepository.findById(10L)).thenReturn(Optional.of(schedule));
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(scheduleRepository.save(schedule)).thenReturn(saved);
        when(scheduleMapper.toResponse(saved)).thenReturn(expected);

        ScheduleResponse result = scheduleService.update(10L, request);

        assertThat(result).isEqualTo(expected);
        verify(scheduleMapper).updateSchedule(request, group, course, schedule);
    }

    @Test
    void update_whenScheduleNotExists_shouldThrowNotFoundException() {
        UpdateScheduleRequest request = new UpdateScheduleRequest(1L, 1L, DATE);
        when(scheduleRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> scheduleService.update(99L, request))
                .isInstanceOf(NotFoundException.class);
        verify(scheduleRepository, never()).save(any());
    }

    @Test
    void update_whenGroupNotExists_shouldThrowNotFoundException() {
        Group group = Group.builder().id(1L).build();
        Course course = Course.builder().id(1L).build();
        Schedule schedule = Schedule.builder().id(10L).group(group).course(course).date(DATE).build();
        UpdateScheduleRequest request = new UpdateScheduleRequest(99L, 1L, DATE);

        when(scheduleRepository.findById(10L)).thenReturn(Optional.of(schedule));
        when(groupRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> scheduleService.update(10L, request))
                .isInstanceOf(NotFoundException.class);
        verify(scheduleRepository, never()).save(any());
    }

    @Test
    void update_whenCourseNotExists_shouldThrowNotFoundException() {
        Group group = Group.builder().id(1L).build();
        Course course = Course.builder().id(1L).build();
        Schedule schedule = Schedule.builder().id(10L).group(group).course(course).date(DATE).build();
        UpdateScheduleRequest request = new UpdateScheduleRequest(1L, 99L, DATE);

        when(scheduleRepository.findById(10L)).thenReturn(Optional.of(schedule));
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> scheduleService.update(10L, request))
                .isInstanceOf(NotFoundException.class);
        verify(scheduleRepository, never()).save(any());
    }

    // DELETE

    @Test
    void delete_whenExists_shouldDelete() {
        Schedule schedule = Schedule.builder().id(10L).build();
        when(scheduleRepository.findById(10L)).thenReturn(Optional.of(schedule));

        scheduleService.delete(10L);

        verify(scheduleRepository).delete(schedule);
    }

    @Test
    void delete_whenNotExists_shouldThrowNotFoundException() {
        when(scheduleRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> scheduleService.delete(99L))
                .isInstanceOf(NotFoundException.class);
        verify(scheduleRepository, never()).delete(any());
    }

    // GET SCHEDULE BY GROUP

    @Test
    void getScheduleByGroup_shouldReturnSchedulesForGroup() {
        Group group = Group.builder().id(1L).build();
        Schedule s1 = Schedule.builder().id(1L).group(group).date(DATE).build();
        Schedule s2 = Schedule.builder().id(2L).group(group).date(DATE.plusDays(1)).build();
        ScheduleResponse r1 = new ScheduleResponse(1L, null, null, DATE);
        ScheduleResponse r2 = new ScheduleResponse(2L, null, null, DATE.plusDays(1));

        when(scheduleRepository.findAllByGroup_Id(1L)).thenReturn(List.of(s1, s2));
        when(scheduleMapper.toResponse(s1)).thenReturn(r1);
        when(scheduleMapper.toResponse(s2)).thenReturn(r2);

        List<ScheduleResponse> result = scheduleService.getScheduleByGroup(1L);

        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void getScheduleByGroup_whenNoSchedules_shouldReturnEmptyList() {
        when(scheduleRepository.findAllByGroup_Id(1L)).thenReturn(List.of());

        List<ScheduleResponse> result = scheduleService.getScheduleByGroup(1L);

        assertThat(result.size()).isEqualTo(0);
    }
}
