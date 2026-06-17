package com.lms.lms_system_management.service;

import com.lms.lms_system_management.dao.CourseRepository;
import com.lms.lms_system_management.dao.TeacherRepository;
import com.lms.lms_system_management.dto.course.CourseFilter;
import com.lms.lms_system_management.dto.course.NewCourseRequest;
import com.lms.lms_system_management.dto.course.UpdateCourseRequest;
import com.lms.lms_system_management.dto.course.CourseResponse;
import com.lms.lms_system_management.mapper.CourseMapper;
import com.lms.lms_system_management.model.CourseEntity;
import com.lms.lms_system_management.model.TeacherEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.lms.lms_system_management.dao.specification.CourseSpecification.hasName;
import static com.lms.lms_system_management.dao.specification.CourseSpecification.hasTeacherId;

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

        Specification<CourseEntity> spec = Specification
                .allOf(
                        hasName(filter.name()),
                        hasTeacherId(filter.teacherId())
                );

        return courseRepository.findAll(spec, pageable)
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

        CourseEntity deleted = courseRepository.findByIdOrThrow(id);
        deleted.setDeleted(true);
        courseRepository.save(deleted);
    }
}
