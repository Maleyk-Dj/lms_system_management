package com.lms.lms_system_management.service;

import com.lms.lms_system_management.dto.course.NewCourseRequest;
import com.lms.lms_system_management.dto.course.UpdateCourseRequest;
import com.lms.lms_system_management.dto.course.CourseResponse;

import java.util.List;

public interface CourseService {

    CourseResponse create(NewCourseRequest newCourseRequest);

    CourseResponse findById(Long id);

    List<CourseResponse> findAll();

    CourseResponse update(UpdateCourseRequest updateCourseRequest, Long id);

    void delete(Long id);

}
