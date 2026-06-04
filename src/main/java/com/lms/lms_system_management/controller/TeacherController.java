package com.lms.lms_system_management.controller;

import com.lms.lms_system_management.dto.request.NewTeacherRequest;
import com.lms.lms_system_management.dto.request.UpdateTeacherRequest;
import com.lms.lms_system_management.dto.response.TeacherResponse;
import com.lms.lms_system_management.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
@Validated
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TeacherResponse create(@RequestBody NewTeacherRequest newTeacherRequest) {
        return teacherService.create(newTeacherRequest);
    }

    @GetMapping("/{teacher_id}")
    public TeacherResponse getTeacher(@PathVariable("teacher_id") Long id) {
        return teacherService.getById(id);
    }

    @GetMapping
    public List<TeacherResponse> getTeachers() {
        return teacherService.getAll();
    }

    @PutMapping("/{teacher_id}")
    public TeacherResponse update(@RequestBody UpdateTeacherRequest updateTeacherRequest,
                                  @PathVariable("teacher_id") Long id) {
        return teacherService.update(updateTeacherRequest, id);
    }

    @DeleteMapping("/{teacher_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("teacher_id") Long id) {
        teacherService.deleteById(id);
    }
}
