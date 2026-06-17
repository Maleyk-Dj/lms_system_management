package com.lms.lms_system_management.service;

import com.lms.lms_system_management.dto.course.CourseFilter;
import com.lms.lms_system_management.dto.course.NewCourseRequest;
import com.lms.lms_system_management.dto.course.UpdateCourseRequest;
import com.lms.lms_system_management.dto.course.CourseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseService {

    CourseResponse create(NewCourseRequest newCourseRequest);

    CourseResponse getById(Long id);

    Page<CourseResponse> findAll(CourseFilter filter, Pageable pageable);

    CourseResponse update(UpdateCourseRequest updateCourseRequest, Long id);

    void deleteById(Long id);

}
