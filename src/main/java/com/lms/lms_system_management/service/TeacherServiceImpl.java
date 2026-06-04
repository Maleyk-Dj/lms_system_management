package com.lms.lms_system_management.service;

import com.lms.lms_system_management.dao.ScheduleRepository;
import com.lms.lms_system_management.dao.TeacherRepository;
import com.lms.lms_system_management.dto.request.NewTeacherRequest;
import com.lms.lms_system_management.dto.request.UpdateTeacherRequest;
import com.lms.lms_system_management.dto.response.ScheduleResponse;
import com.lms.lms_system_management.dto.response.TeacherResponse;
import com.lms.lms_system_management.exception.NotFoundException;
import com.lms.lms_system_management.mapper.ScheduleMapper;
import com.lms.lms_system_management.mapper.TeacherMapper;
import com.lms.lms_system_management.model.Teacher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;

    @Transactional
    @Override
    public TeacherResponse create(NewTeacherRequest teacher) {

        Teacher saved = teacherRepository.save(teacherMapper.toEntity(teacher));
        return teacherMapper.toResponse(saved);
    }

    @Transactional(readOnly=true)
    @Override
    public TeacherResponse getById(Long id) {
        Teacher teacherEntity = teacherRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Преподаватель с id " + id + " не найден"));
        return teacherMapper.toResponse(teacherEntity);
    }
    @Transactional(readOnly=true)
    @Override
    public List<TeacherResponse> getAll() {
        return teacherRepository.findAll()
                .stream()
                .map(teacherMapper::toResponse)
                .toList();
    }

    @Transactional
    @Override
    public TeacherResponse update (UpdateTeacherRequest teacher, Long id) {
        Teacher updated = teacherRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Преподаватель с " + id + " не найден"));
        teacherMapper.updateEntity(teacher, updated);
        Teacher saved=teacherRepository.save(updated);
        return teacherMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        Teacher deleted = teacherRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Преподаватель с " + id + " не найден"));
        teacherRepository.delete(deleted);

    }

    @Transactional(readOnly=true)
    @Override
    public List<ScheduleResponse> getScheduleByTeacher(Long id) {
        return scheduleRepository.findByCourse_Teacher_Id(id)
                .stream()
                .map(scheduleMapper::toResponse)
                .toList();
    }
}
