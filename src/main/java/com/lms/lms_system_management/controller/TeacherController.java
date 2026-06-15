package com.lms.lms_system_management.controller;

import com.lms.lms_system_management.dto.teacher.NewTeacherRequest;
import com.lms.lms_system_management.dto.teacher.TeacherFilter;
import com.lms.lms_system_management.dto.teacher.UpdateTeacherRequest;
import com.lms.lms_system_management.dto.schedule.ScheduleResponse;
import com.lms.lms_system_management.dto.teacher.TeacherResponse;
import com.lms.lms_system_management.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public TeacherResponse create(@Valid @RequestBody NewTeacherRequest newTeacherRequest) {
        return teacherService.create(newTeacherRequest);
    }

    @GetMapping("/{teacherId}")
    public TeacherResponse getTeacher(@PathVariable("teacherId") Long id) {
        return teacherService.getById(id);
    }

    @GetMapping
    public Page<TeacherResponse> getTeachers(@ModelAttribute TeacherFilter teacherFilter, Pageable pageable) {
        return teacherService.getAll(teacherFilter, pageable);
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
