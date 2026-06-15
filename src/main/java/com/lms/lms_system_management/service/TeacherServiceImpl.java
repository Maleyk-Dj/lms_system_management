package com.lms.lms_system_management.service;

import com.lms.lms_system_management.dao.ScheduleRepository;
import com.lms.lms_system_management.dao.TeacherRepository;
import com.lms.lms_system_management.dto.teacher.NewTeacherRequest;
import com.lms.lms_system_management.dto.teacher.TeacherFilter;
import com.lms.lms_system_management.dto.teacher.UpdateTeacherRequest;
import com.lms.lms_system_management.dto.schedule.ScheduleResponse;
import com.lms.lms_system_management.dto.teacher.TeacherResponse;
import com.lms.lms_system_management.mapper.ScheduleMapper;
import com.lms.lms_system_management.mapper.TeacherMapper;
import com.lms.lms_system_management.model.TeacherEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.lms.lms_system_management.dao.specification.TeacherSpecification.hasFirstName;
import static com.lms.lms_system_management.dao.specification.TeacherSpecification.hasLastName;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;

    @Override
    public TeacherResponse create(NewTeacherRequest teacher) {

        TeacherEntity saved = teacherRepository.save(teacherMapper.toEntity(teacher));
        return teacherMapper.toResponse(saved);
    }

    @Override
    public TeacherResponse getById(Long id) {

        TeacherEntity teacherEntity = teacherRepository.findByIdOrThrow(id);
        return teacherMapper.toResponse(teacherEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<TeacherResponse> getAll(TeacherFilter filter, Pageable pageable) {

        Specification<TeacherEntity> spec = Specification
                .allOf(
                        hasFirstName(filter.firstName()),
                        hasLastName(filter.lastName())
                );

        return teacherRepository.findAll(spec, pageable)
                .map(teacherMapper::toResponse);
    }

    @Transactional
    @Override
    public TeacherResponse update(UpdateTeacherRequest teacher, Long id) {

        TeacherEntity updated = teacherRepository.findByIdOrThrow(id);
        teacherMapper.updateEntity(teacher, updated);
        TeacherEntity saved = teacherRepository.save(updated);
        return teacherMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {

        TeacherEntity deleted = teacherRepository.findByIdOrThrow(id);
        teacherRepository.delete(deleted);

    }

    @Transactional(readOnly = true)
    @Override
    public List<ScheduleResponse> getScheduleByTeacher(Long id) {

        return scheduleRepository.findByCourseTeacherId(id)
                .stream()
                .map(scheduleMapper::toResponse)
                .toList();
    }
}
