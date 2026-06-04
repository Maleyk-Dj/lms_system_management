package com.lms.lms_system_management.controller;

import com.lms.lms_system_management.dto.request.NewCourseRequest;
import com.lms.lms_system_management.dto.request.UpdateCourseRequest;
import com.lms.lms_system_management.dto.response.CourseResponse;
import com.lms.lms_system_management.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/courses")
@Validated
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourseResponse createCourse(@RequestBody NewCourseRequest request) {
        return courseService.create(request);
    }

    @GetMapping("/{course_id}")
    public CourseResponse getCourse(@PathVariable("course_id") Long id) {
        return courseService.findById(id);
    }

    @GetMapping
    public List<CourseResponse> getCourses() {
        return courseService.findAll();
    }

    @PutMapping("/{course_id}")
    public CourseResponse updateCourse(@RequestBody UpdateCourseRequest request,
                                       @PathVariable("course_id") Long id) {
        return courseService.update(request, id);
    }

    @DeleteMapping("/{course_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourse(@PathVariable("course_id") Long id) {
        courseService.delete(id);
    }
}
