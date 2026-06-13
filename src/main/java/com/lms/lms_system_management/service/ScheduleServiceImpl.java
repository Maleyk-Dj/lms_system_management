package com.lms.lms_system_management.service;

import com.lms.lms_system_management.dao.CourseRepository;
import com.lms.lms_system_management.dao.GroupRepository;
import com.lms.lms_system_management.dao.ScheduleRepository;
import com.lms.lms_system_management.dto.schedule.NewScheduleRequest;
import com.lms.lms_system_management.dto.schedule.UpdateScheduleRequest;
import com.lms.lms_system_management.dto.schedule.ScheduleResponse;
import com.lms.lms_system_management.mapper.ScheduleMapper;
import com.lms.lms_system_management.model.Course;
import com.lms.lms_system_management.model.Group;
import com.lms.lms_system_management.model.Schedule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {

    private final GroupRepository groupRepository;
    private final CourseRepository courseRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;

    @Transactional
    @Override
    public ScheduleResponse assignCourseTime(NewScheduleRequest request) {

        Group group = groupRepository.findByIdOrThrow(request.groupId());
        Course course = courseRepository.findByIdOrThrow(request.courseId());
        Schedule schedule = scheduleMapper.toEntity(request, group, course);
        Schedule saved = scheduleRepository.save(schedule);
        return scheduleMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public ScheduleResponse update(Long scheduleId, UpdateScheduleRequest request) {

        Schedule schedule = scheduleRepository.findByIdOrThrow(scheduleId);
        Group group = groupRepository.findByIdOrThrow(request.groupId());
        Course course = courseRepository.findByIdOrThrow(request.courseId());
        scheduleMapper.updateSchedule(request, group, course, schedule);
        Schedule updated = scheduleRepository.save(schedule);
        return scheduleMapper.toResponse(updated);
    }

    @Transactional
    @Override
    public void delete(Long id) {

        Schedule schedule = scheduleRepository.findByIdOrThrow(id);
        scheduleRepository.delete(schedule);

    }

    @Transactional(readOnly = true)
    @Override
    public List<ScheduleResponse> getScheduleByGroup(Long groupId) {

        Group group = groupRepository.findByIdOrThrow(groupId);

        return scheduleRepository.findAllByGroupId(groupId)
                .stream()
                .map(scheduleMapper::toResponse)
                .toList();
    }
}
