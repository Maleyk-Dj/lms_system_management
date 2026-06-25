package com.lms.lms_system_management.controller;

import com.lms.lms_system_management.dto.course.CourseFilter;
import com.lms.lms_system_management.dto.course.NewCourseRequest;
import com.lms.lms_system_management.dto.course.UpdateCourseRequest;
import com.lms.lms_system_management.dto.course.CourseResponse;
import com.lms.lms_system_management.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/courses")
@Validated
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourseResponse createCourse(@Valid @RequestBody NewCourseRequest request) {
        return courseService.create(request);
    }

    @GetMapping("/{courseId}")
    public CourseResponse getCourse(@PathVariable("courseId") Long id) {
        return courseService.getById(id);
    }

    @GetMapping
    public Page<CourseResponse> getCourses(@ModelAttribute CourseFilter filter, Pageable pageable) {
        return courseService.findAll(filter, pageable);
    }

    @PutMapping("/{courseId}")
    public CourseResponse updateCourse(@Valid @RequestBody UpdateCourseRequest request,
                                       @PathVariable("courseId") Long id) {
        return courseService.update(request, id);
    }

    @DeleteMapping("/{courseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourse(@PathVariable("courseId") Long id) {
        courseService.deleteById(id);
    }
}
