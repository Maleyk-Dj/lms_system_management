package com.lms.lms_system_management.controller;

import com.lms.lms_system_management.dto.student.NewStudentRequest;
import com.lms.lms_system_management.dto.student.StudentFilter;
import com.lms.lms_system_management.dto.student.UpdateStudentRequest;
import com.lms.lms_system_management.dto.student.StudentResponse;
import com.lms.lms_system_management.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Validated
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentResponse create(@Valid @RequestBody NewStudentRequest request) {
        return studentService.create(request);
    }

    @GetMapping("/{studentId}")
    public StudentResponse getById(@PathVariable("studentId") Long id) {
        return studentService.getById(id);
    }

    @GetMapping
    public Page<StudentResponse> getAll(@ModelAttribute StudentFilter filter, Pageable pageable) {
        return studentService.getAll(filter, pageable);
    }

    @PatchMapping("/{studentId}")
    public StudentResponse update(@PathVariable("studentId") Long id,
                                  @Valid @RequestBody UpdateStudentRequest request) {
        return studentService.update(request, id);
    }

    @DeleteMapping("/{studentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("studentId") Long id) {
        studentService.deleteById(id);
    }

    @PatchMapping("/{studentId}/groups/{groupId}")
    public StudentResponse addToGroup(@PathVariable("studentId") Long studentId,
                                      @PathVariable("groupId") Long groupId) {
        return studentService.addToGroup(studentId, groupId);
    }
}
