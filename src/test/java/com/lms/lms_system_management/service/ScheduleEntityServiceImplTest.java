package com.lms.lms_system_management.service;

import com.lms.lms_system_management.dao.CourseRepository;
import com.lms.lms_system_management.dao.GroupRepository;
import com.lms.lms_system_management.dao.ScheduleRepository;
import com.lms.lms_system_management.dto.schedule.NewScheduleRequest;
import com.lms.lms_system_management.dto.schedule.ScheduleFilter;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
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
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setId(1L);

        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setId(1L);

        NewScheduleRequest request = new NewScheduleRequest(1L, 1L, DATE);

        ScheduleEntity entity = new ScheduleEntity();
        entity.setId(1L);
        entity.setGroupEntity(groupEntity);
        entity.setCourseEntity(courseEntity);
        entity.setDateClass(DATE);

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
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setId(1L);

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
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setId(1L);

        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setId(1L);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(10L);
        scheduleEntity.setGroupEntity(groupEntity);
        scheduleEntity.setCourseEntity(courseEntity);
        scheduleEntity.setDateClass(DATE);

        LocalDateTime newDate = LocalDateTime.of(2025, 10, 1, 12, 0);
        UpdateScheduleRequest request = new UpdateScheduleRequest(1L, 1L, newDate);

        ScheduleEntity saved = new ScheduleEntity();
        saved.setId(10L);
        saved.setGroupEntity(groupEntity);
        saved.setCourseEntity(courseEntity);
        saved.setDateClass(newDate);

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
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setId(1L);

        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setId(1L);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(10L);
        scheduleEntity.setGroupEntity(groupEntity);
        scheduleEntity.setCourseEntity(courseEntity);
        scheduleEntity.setDateClass(DATE);

        UpdateScheduleRequest request = new UpdateScheduleRequest(99L, 1L, DATE);

        when(scheduleRepository.findByIdOrThrow(10L)).thenReturn(scheduleEntity);
        when(groupRepository.findByIdOrThrow(99L)).thenThrow(new NotFoundException("not found"));

        assertThatThrownBy(() -> scheduleService.update(10L, request))
                .isInstanceOf(NotFoundException.class);
        verify(scheduleRepository, never()).save(any());
    }

    @Test
    void update_whenCourseNotExists_shouldThrowNotFoundException() {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setId(1L);

        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setId(1L);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(10L);
        scheduleEntity.setGroupEntity(groupEntity);
        scheduleEntity.setCourseEntity(courseEntity);
        scheduleEntity.setDateClass(DATE);

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
        when(scheduleRepository.softDeleteById(10L)).thenReturn(1);

        scheduleService.deleteById(10L);

        verify(scheduleRepository).softDeleteById(10L);
    }

    @Test
    void delete_whenNotExists_shouldThrowNotFoundException() {
        when(scheduleRepository.softDeleteById(99L)).thenReturn(0);

        assertThatThrownBy(() -> scheduleService.deleteById(99L))
                .isInstanceOf(NotFoundException.class);
        verify(scheduleRepository, never()).delete(any(ScheduleEntity.class));
    }

    // GET SCHEDULE BY GROUP

    @Test
    void getScheduleByGroup_shouldReturnPagedSchedules() {
        ScheduleFilter filter = new ScheduleFilter(1L, null, null);
        Pageable pageable = PageRequest.of(0, 10);

        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setId(1L);

        ScheduleEntity s1 = new ScheduleEntity();
        s1.setId(1L);
        s1.setGroupEntity(groupEntity);
        s1.setDateClass(DATE);

        ScheduleEntity s2 = new ScheduleEntity();
        s2.setId(2L);
        s2.setGroupEntity(groupEntity);
        s2.setDateClass(DATE.plusDays(1));

        ScheduleResponse r1 = new ScheduleResponse(1L, null, null, DATE);
        ScheduleResponse r2 = new ScheduleResponse(2L, null, null, DATE.plusDays(1));

        when(scheduleRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(s1, s2)));
        when(scheduleMapper.toResponse(s1)).thenReturn(r1);
        when(scheduleMapper.toResponse(s2)).thenReturn(r2);

        Page<ScheduleResponse> result = scheduleService.getScheduleByGroup(filter, pageable);

        assertThat(result.getContent()).hasSize(2).containsExactly(r1, r2);
    }

    @Test
    void getScheduleByGroup_whenNoSchedules_shouldReturnEmptyPage() {
        ScheduleFilter filter = new ScheduleFilter(1L, null, null);
        Pageable pageable = PageRequest.of(0, 10);

        when(scheduleRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of()));

        assertThat(scheduleService.getScheduleByGroup(filter, pageable).getContent()).isEmpty();
    }
}
