package com.lms.lms_system_management.controller;

import com.lms.lms_system_management.dto.request.NewStudentRequest;
import com.lms.lms_system_management.dto.request.UpdateStudentRequest;
import com.lms.lms_system_management.dto.response.StudentResponse;
import com.lms.lms_system_management.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Validated
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentResponse create(@RequestBody NewStudentRequest request) {
        return studentService.create(request);
    }

    @GetMapping("/{student_id}")
    public StudentResponse getById(@PathVariable("student_id") Long id) {
        return studentService.getById(id);
    }

    @GetMapping
    public List<StudentResponse> getAll() {
        return studentService.getAll();
    }

    @PutMapping("/{student_id}")
    public StudentResponse update(@PathVariable("student_id") Long id,
                                  @RequestBody UpdateStudentRequest request) {
        return studentService.update(request, id);
    }

    @DeleteMapping("/{student_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("student_id") Long id) {
        studentService.deleteById(id);
    }

    @PatchMapping("/{student_id}/groups/{group_id}")
    public StudentResponse addToGroup(@PathVariable("student_id") Long studentId,
                                      @PathVariable("group_id") Long groupId) {
        return studentService.addToGroup(studentId, groupId);
    }
}
