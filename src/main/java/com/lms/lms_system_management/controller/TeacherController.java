package com.lms.lms_system_management.controller;

import com.lms.lms_system_management.dto.request.NewTeacherRequest;
import com.lms.lms_system_management.dto.request.UpdateTeacherRequest;
import com.lms.lms_system_management.dto.response.ScheduleResponse;
import com.lms.lms_system_management.dto.response.TeacherResponse;
import com.lms.lms_system_management.service.TeacherService;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
@Validated
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TeacherResponse create(@Valid @RequestBody NewTeacherRequest newTeacherRequest) {
        return teacherService.create(newTeacherRequest);
    }

    @GetMapping("/{teacherId}")
    public TeacherResponse getTeacher(@PathVariable("teacherId") Long id) {
        return teacherService.getById(id);
    }

    @GetMapping
    public List<TeacherResponse> getTeachers() {
        return teacherService.getAll();
    }

    @PatchMapping("/{teacherId}")
    public TeacherResponse update(@Valid @RequestBody UpdateTeacherRequest updateTeacherRequest,
                                  @PathVariable("teacherId") Long id) {
        return teacherService.update(updateTeacherRequest, id);
    }

    @DeleteMapping("/{teacherId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("teacherId") Long id) {
        teacherService.deleteById(id);
    }

    @GetMapping("/{teacherId}/schedules")
    public List<ScheduleResponse> getScheduleByTeacher(@PathVariable("teacherId") Long id) {
        return teacherService.getScheduleByTeacher(id);
    }
}
