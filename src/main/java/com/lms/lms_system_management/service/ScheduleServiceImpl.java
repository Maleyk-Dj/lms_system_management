package com.lms.lms_system_management.service;

import com.lms.lms_system_management.dao.CourseRepository;
import com.lms.lms_system_management.dao.GroupRepository;
import com.lms.lms_system_management.dao.ScheduleRepository;
import com.lms.lms_system_management.dto.schedule.NewScheduleRequest;
import com.lms.lms_system_management.dto.schedule.UpdateScheduleRequest;
import com.lms.lms_system_management.dto.schedule.ScheduleResponse;
import com.lms.lms_system_management.mapper.ScheduleMapper;
import com.lms.lms_system_management.model.CourseEntity;
import com.lms.lms_system_management.model.GroupEntity;
import com.lms.lms_system_management.model.ScheduleEntity;
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

        GroupEntity groupEntity = groupRepository.findByIdOrThrow(request.groupId());
        CourseEntity courseEntity = courseRepository.findByIdOrThrow(request.courseId());
        ScheduleEntity scheduleEntity = scheduleMapper.toEntity(request, groupEntity, courseEntity);
        ScheduleEntity saved = scheduleRepository.save(scheduleEntity);
        return scheduleMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public ScheduleResponse update(Long scheduleId, UpdateScheduleRequest request) {

        ScheduleEntity scheduleEntity = scheduleRepository.findByIdOrThrow(scheduleId);
        GroupEntity groupEntity = groupRepository.findByIdOrThrow(request.groupId());
        CourseEntity courseEntity = courseRepository.findByIdOrThrow(request.courseId());
        scheduleMapper.updateSchedule(request, groupEntity, courseEntity, scheduleEntity);
        ScheduleEntity updated = scheduleRepository.save(scheduleEntity);
        return scheduleMapper.toResponse(updated);
    }

    @Transactional
    @Override
    public void delete(Long id) {

        ScheduleEntity scheduleEntity = scheduleRepository.findByIdOrThrow(id);
        scheduleRepository.delete(scheduleEntity);

    }

    @Transactional(readOnly = true)
    @Override
    public List<ScheduleResponse> getScheduleByGroup(Long groupId) {

        return scheduleRepository.findAllByGroupId(groupId)
                .stream()
                .map(scheduleMapper::toResponse)
                .toList();
    }
}
