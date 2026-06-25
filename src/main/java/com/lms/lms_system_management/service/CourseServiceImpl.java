package com.lms.lms_system_management.service;

import com.lms.lms_system_management.dao.CourseRepository;
import com.lms.lms_system_management.dao.TeacherRepository;
import com.lms.lms_system_management.dao.specification.CourseSpecification;
import com.lms.lms_system_management.dto.course.CourseFilter;
import com.lms.lms_system_management.dto.course.NewCourseRequest;
import com.lms.lms_system_management.dto.course.UpdateCourseRequest;
import com.lms.lms_system_management.dto.course.CourseResponse;
import com.lms.lms_system_management.exception.NotFoundException;
import com.lms.lms_system_management.mapper.CourseMapper;
import com.lms.lms_system_management.model.CourseEntity;
import com.lms.lms_system_management.model.TeacherEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final TeacherRepository teacherRepository;

    @Override
    public CourseResponse create(NewCourseRequest newCourseRequest) {
        Long teacherId = newCourseRequest.teacherId();
        TeacherEntity teacherEntity = teacherRepository.findByIdOrThrow(teacherId);
        CourseEntity courseEntity = courseMapper.toEntity(newCourseRequest, teacherEntity);
        CourseEntity saved = courseRepository.save(courseEntity);
        return courseMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    @Override
    public CourseResponse getById(Long id) {
        CourseEntity findable = courseRepository.findByIdOrThrow(id);
        return courseMapper.toResponse(findable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CourseResponse> findAll(CourseFilter filter, Pageable pageable) {
        return courseRepository.findAll(CourseSpecification.builder(filter), pageable)
                .map(courseMapper::toResponse);
    }

    @Transactional
    @Override
    public CourseResponse update(UpdateCourseRequest updateCourseRequest, Long id) {
        CourseEntity courseEntity = courseRepository.findByIdOrThrow(id);
        Long teacherId = updateCourseRequest.teacherId();
        TeacherEntity teacherEntity = teacherRepository.findByIdOrThrow(teacherId);
        courseMapper.updateEntity(updateCourseRequest, teacherEntity, courseEntity);
        CourseEntity updated = courseRepository.save(courseEntity);
        return courseMapper.toResponse(updated);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        int updated = courseRepository.softDeleteById(id);

        if (updated == 0) {
            throw new NotFoundException("Курс с id " + id + " не найден");
        }
    }
}
