package com.lms.lms_system_management.service;

import com.lms.lms_system_management.dao.CourseRepository;
import com.lms.lms_system_management.dao.TeacherRepository;
import com.lms.lms_system_management.dto.request.NewCourseRequest;
import com.lms.lms_system_management.dto.request.UpdateCourseRequest;
import com.lms.lms_system_management.dto.response.CourseResponse;
import com.lms.lms_system_management.exception.NotFoundException;
import com.lms.lms_system_management.mapper.CourseMapper;
import com.lms.lms_system_management.model.Course;
import com.lms.lms_system_management.model.Teacher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService{
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final TeacherRepository teacherRepository;

    @Override
    public CourseResponse create(NewCourseRequest newCourseRequest) {
        Long teacherId=newCourseRequest.teacherId();
        Teacher teacher=teacherRepository.findById(teacherId)
                .orElseThrow(()-> new NotFoundException ("Преподаватель с id "+ teacherId+" не найден"));
        Course course = courseMapper.toEntity(newCourseRequest, teacher);
        Course saved=courseRepository.save(course);
        return courseMapper.toResponse(saved);
    }

    @Transactional (readOnly = true)
    @Override
    public CourseResponse findById(Long id) {
        Course findable=courseRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Курс с id "+ id+" не найден"));
        return courseMapper.toResponse(findable);
    }

    @Transactional (readOnly = true)
    @Override
    public List<CourseResponse> findAll() {
        return courseRepository.findAll()
                .stream()
                .map(courseMapper::toResponse)
                .toList();
    }
    @Transactional
    @Override
    public CourseResponse update(UpdateCourseRequest updateCourseRequest, Long id) {
        Course course=courseRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Курс с id "+ id+" не найден"));
        Long teacherId=updateCourseRequest.teacherId();
        Teacher teacher=teacherRepository.findById(teacherId)
                .orElseThrow(()->new NotFoundException("Учитель с id "+ teacherId+" не найден"));
        courseMapper.updateEntity(updateCourseRequest,teacher,course);
        Course updated=courseRepository.save(course);
        return courseMapper.toResponse(updated);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Course deleted =courseRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Курс с id "+ id+" не найден"));
        courseRepository.delete(deleted);
    }
}
