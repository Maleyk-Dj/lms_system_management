package com.lms.lms_system_management.controller;

import com.lms.lms_system_management.dto.request.NewCourseRequest;
import com.lms.lms_system_management.dto.request.UpdateCourseRequest;
import com.lms.lms_system_management.dto.response.CourseResponse;
import com.lms.lms_system_management.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

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
        return courseService.findById(id);
    }

    @GetMapping
    public List<CourseResponse> getCourses() {
        return courseService.findAll();
    }

    @PutMapping("/{courseId}")
    public CourseResponse updateCourse(@Valid @RequestBody UpdateCourseRequest request,
                                       @PathVariable("courseId") Long id) {
        return courseService.update(request, id);
    }

    @DeleteMapping("/{courseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourse(@PathVariable("courseId") Long id) {
        courseService.delete(id);
    }
}
