package com.lms.lms_system_management.service;

import com.lms.lms_system_management.dto.request.NewCourseRequest;
import com.lms.lms_system_management.dto.request.UpdateCourseRequest;
import com.lms.lms_system_management.dto.response.CourseResponse;

import java.util.List;

public interface CourseService {

    CourseResponse create(NewCourseRequest newCourseRequest);

    CourseResponse findById(Long id);

    List<CourseResponse> findAll();

    CourseResponse update(UpdateCourseRequest updateCourseRequest, Long id);

    void delete(Long id);

}
