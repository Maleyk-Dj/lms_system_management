package com.lms.lms_system_management.service;

import com.lms.lms_system_management.dao.CourseRepository;
import com.lms.lms_system_management.dao.GroupRepository;
import com.lms.lms_system_management.dao.ScheduleRepository;
import com.lms.lms_system_management.dto.schedule.NewScheduleRequest;
import com.lms.lms_system_management.dto.schedule.UpdateScheduleRequest;
import com.lms.lms_system_management.dto.schedule.ScheduleResponse;
import com.lms.lms_system_management.exception.NotFoundException;
import com.lms.lms_system_management.mapper.ScheduleMapper;
import com.lms.lms_system_management.model.CourseEntity;
import com.lms.lms_system_management.model.GroupEntity;
import com.lms.lms_system_management.model.ScheduleEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleEntityServiceImplTest {

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
        GroupEntity groupEntity = GroupEntity.builder().id(1L).build();
        CourseEntity courseEntity = CourseEntity.builder().id(1L).build();
        NewScheduleRequest request = new NewScheduleRequest(1L, 1L, DATE);
        ScheduleEntity entity = ScheduleEntity.builder().id(1L).groupEntity(groupEntity).courseEntity(courseEntity).dateClass(DATE).build();
        ScheduleResponse expected = new ScheduleResponse(1L, null, null, DATE);

        when(groupRepository.findByIdOrThrow(1L)).thenReturn(groupEntity);
        when(courseRepository.findByIdOrThrow(1L)).thenReturn(courseEntity);
        when(scheduleMapper.toEntity(request, groupEntity, courseEntity)).thenReturn(entity);
        when(scheduleRepository.save(entity)).thenReturn(entity);
        when(scheduleMapper.toResponse(entity)).thenReturn(expected);

        ScheduleResponse result = scheduleService.assignCourseTime(request);

        assertThat(result).isEqualTo(expected);
        verify(scheduleRepository).save(entity);
    }

    @Test
    void assignCourseTime_whenGroupNotExists_shouldThrowNotFoundException() {
        NewScheduleRequest request = new NewScheduleRequest(99L, 1L, DATE);
        when(groupRepository.findByIdOrThrow(99L)).thenThrow(new NotFoundException("not found"));

        assertThatThrownBy(() -> scheduleService.assignCourseTime(request))
                .isInstanceOf(NotFoundException.class);
        verify(scheduleRepository, never()).save(any());
    }

    @Test
    void assignCourseTime_whenCourseNotExists_shouldThrowNotFoundException() {
        GroupEntity groupEntity = GroupEntity.builder().id(1L).build();
        NewScheduleRequest request = new NewScheduleRequest(1L, 99L, DATE);

        when(groupRepository.findByIdOrThrow(1L)).thenReturn(groupEntity);
        when(courseRepository.findByIdOrThrow(99L)).thenThrow(new NotFoundException("not found"));

        assertThatThrownBy(() -> scheduleService.assignCourseTime(request))
                .isInstanceOf(NotFoundException.class);
        verify(scheduleRepository, never()).save(any());
    }

    // UPDATE

    @Test
    void update_whenAllExist_shouldUpdateAndReturnResponse() {
        GroupEntity groupEntity = GroupEntity.builder().id(1L).build();
        CourseEntity courseEntity = CourseEntity.builder().id(1L).build();
        ScheduleEntity scheduleEntity = ScheduleEntity.builder().id(10L).groupEntity(groupEntity).courseEntity(courseEntity).dateClass(DATE).build();
        LocalDateTime newDate = LocalDateTime.of(2025, 10, 1, 12, 0);
        UpdateScheduleRequest request = new UpdateScheduleRequest(1L, 1L, newDate);
        ScheduleEntity saved = ScheduleEntity.builder().id(10L).groupEntity(groupEntity).courseEntity(courseEntity).dateClass(newDate).build();
        ScheduleResponse expected = new ScheduleResponse(10L, null, null, newDate);

        when(scheduleRepository.findByIdOrThrow(10L)).thenReturn(scheduleEntity);
        when(groupRepository.findByIdOrThrow(1L)).thenReturn(groupEntity);
        when(courseRepository.findByIdOrThrow(1L)).thenReturn(courseEntity);
        when(scheduleRepository.save(scheduleEntity)).thenReturn(saved);
        when(scheduleMapper.toResponse(saved)).thenReturn(expected);

        ScheduleResponse result = scheduleService.update(10L, request);

        assertThat(result).isEqualTo(expected);
        verify(scheduleMapper).updateSchedule(request, groupEntity, courseEntity, scheduleEntity);
    }

    @Test
    void update_whenScheduleNotExists_shouldThrowNotFoundException() {
        UpdateScheduleRequest request = new UpdateScheduleRequest(1L, 1L, DATE);
        when(scheduleRepository.findByIdOrThrow(99L)).thenThrow(new NotFoundException("not found"));

        assertThatThrownBy(() -> scheduleService.update(99L, request))
                .isInstanceOf(NotFoundException.class);
        verify(scheduleRepository, never()).save(any());
    }

    @Test
    void update_whenGroupNotExists_shouldThrowNotFoundException() {
        GroupEntity groupEntity = GroupEntity.builder().id(1L).build();
        CourseEntity courseEntity = CourseEntity.builder().id(1L).build();
        ScheduleEntity scheduleEntity = ScheduleEntity.builder().id(10L).groupEntity(groupEntity).courseEntity(courseEntity).dateClass(DATE).build();
        UpdateScheduleRequest request = new UpdateScheduleRequest(99L, 1L, DATE);

        when(scheduleRepository.findByIdOrThrow(10L)).thenReturn(scheduleEntity);
        when(groupRepository.findByIdOrThrow(99L)).thenThrow(new NotFoundException("not found"));

        assertThatThrownBy(() -> scheduleService.update(10L, request))
                .isInstanceOf(NotFoundException.class);
        verify(scheduleRepository, never()).save(any());
    }

    @Test
    void update_whenCourseNotExists_shouldThrowNotFoundException() {
        GroupEntity groupEntity = GroupEntity.builder().id(1L).build();
        CourseEntity courseEntity = CourseEntity.builder().id(1L).build();
        ScheduleEntity scheduleEntity = ScheduleEntity.builder().id(10L).groupEntity(groupEntity).courseEntity(courseEntity).dateClass(DATE).build();
        UpdateScheduleRequest request = new UpdateScheduleRequest(1L, 99L, DATE);

        when(scheduleRepository.findByIdOrThrow(10L)).thenReturn(scheduleEntity);
        when(groupRepository.findByIdOrThrow(1L)).thenReturn(groupEntity);
        when(courseRepository.findByIdOrThrow(99L)).thenThrow(new NotFoundException("not found"));

        assertThatThrownBy(() -> scheduleService.update(10L, request))
                .isInstanceOf(NotFoundException.class);
        verify(scheduleRepository, never()).save(any());
    }

    // DELETE

    @Test
    void delete_whenExists_shouldDelete() {
        ScheduleEntity scheduleEntity = ScheduleEntity.builder().id(10L).build();
        when(scheduleRepository.findByIdOrThrow(10L)).thenReturn(scheduleEntity);

        scheduleService.delete(10L);

        verify(scheduleRepository).delete(scheduleEntity);
    }

    @Test
    void delete_whenNotExists_shouldThrowNotFoundException() {
        when(scheduleRepository.findByIdOrThrow(99L)).thenThrow(new NotFoundException("not found"));

        assertThatThrownBy(() -> scheduleService.delete(99L))
                .isInstanceOf(NotFoundException.class);
        verify(scheduleRepository, never()).delete(any());
    }

    // GET SCHEDULE BY GROUP

    @Test
    void getScheduleByGroup_shouldReturnSchedulesForGroup() {
        GroupEntity groupEntity = GroupEntity.builder().id(1L).build();
        ScheduleEntity s1 = ScheduleEntity.builder().id(1L).groupEntity(groupEntity).dateClass(DATE).build();
        ScheduleEntity s2 = ScheduleEntity.builder().id(2L).groupEntity(groupEntity).dateClass(DATE.plusDays(1)).build();
        ScheduleResponse r1 = new ScheduleResponse(1L, null, null, DATE);
        ScheduleResponse r2 = new ScheduleResponse(2L, null, null, DATE.plusDays(1));

        when(groupRepository.findByIdOrThrow(1L)).thenReturn(groupEntity);
        when(scheduleRepository.findAllByGroupId(1L)).thenReturn(List.of(s1, s2));
        when(scheduleMapper.toResponse(s1)).thenReturn(r1);
        when(scheduleMapper.toResponse(s2)).thenReturn(r2);

        List<ScheduleResponse> result = scheduleService.getScheduleByGroup(1L);

        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void getScheduleByGroup_whenGroupNotExists_shouldThrowNotFoundException() {
        when(groupRepository.findByIdOrThrow(99L)).thenThrow(new NotFoundException("not found"));

        assertThatThrownBy(() -> scheduleService.getScheduleByGroup(99L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void getScheduleByGroup_whenNoSchedules_shouldReturnEmptyList() {
        GroupEntity groupEntity = GroupEntity.builder().id(1L).build();
        when(groupRepository.findByIdOrThrow(1L)).thenReturn(groupEntity);
        when(scheduleRepository.findAllByGroupId(1L)).thenReturn(List.of());

        List<ScheduleResponse> result = scheduleService.getScheduleByGroup(1L);

        assertThat(result.size()).isEqualTo(0);
    }
}
