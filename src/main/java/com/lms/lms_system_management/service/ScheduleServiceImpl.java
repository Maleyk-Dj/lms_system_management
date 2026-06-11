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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleServiceImpl implements ScheduleService{

    private final GroupRepository groupRepository;
    private final CourseRepository courseRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;

    @Transactional
    @Override
    public ScheduleResponse assignCourseTime (NewScheduleRequest request) {
        Group group = groupRepository.findById(request.groupId())
                .orElseThrow(() -> new NotFoundException("Группа с id "+request.groupId()+ " не найдена"));
        Course course=courseRepository.findById(request.courseId())
                .orElseThrow(() -> new NotFoundException("Курс с id "+request.courseId()+ " не найден"));
        Schedule schedule = scheduleMapper.toEntity(request, group, course);
        Schedule saved= scheduleRepository.save(schedule);
        return scheduleMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public ScheduleResponse update(Long scheduleId, UpdateScheduleRequest request) {
        Schedule schedule=scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new NotFoundException("Рсаписание с id "+scheduleId+ " не найдено"));
        Group group = groupRepository.findById(request.groupId())
                .orElseThrow(() -> new NotFoundException("Группа с id "+request.groupId()+ " не найдена"));
        Course course=courseRepository.findById(request.courseId())
                .orElseThrow(() -> new NotFoundException("Курс с id "+request.courseId()+ " не найден"));
        scheduleMapper.updateSchedule(request,group,course,schedule);
        Schedule updated=scheduleRepository.save(schedule);
        return scheduleMapper.toResponse(updated);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Schedule schedule=scheduleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Расписание с id "+id+ " не найдено"));
        scheduleRepository.delete(schedule);

    }

    @Transactional (readOnly=true)
    @Override
    public List<ScheduleResponse> getScheduleByGroup(Long groupId) {

        return scheduleRepository.findAllByGroup_Id(groupId)
                .stream()
                .map(scheduleMapper::toResponse)
                .toList();
    }
}
