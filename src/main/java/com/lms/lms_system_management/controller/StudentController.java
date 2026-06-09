package com.lms.lms_system_management.controller;

import com.lms.lms_system_management.dto.request.NewStudentRequest;
import com.lms.lms_system_management.dto.request.UpdateStudentRequest;
import com.lms.lms_system_management.dto.response.StudentResponse;
import com.lms.lms_system_management.service.StudentService;
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
import org.springframework.web.bind.annotation.PatchMapping;

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

    @GetMapping("/{studentId}")
    public StudentResponse getById(@PathVariable("studentId") Long id) {
        return studentService.getById(id);
    }

    @GetMapping
    public List<StudentResponse> getAll() {
        return studentService.getAll();
    }

    @PutMapping("/{studentId}")
    public StudentResponse update(@PathVariable("studentId") Long id,
                                  @RequestBody UpdateStudentRequest request) {
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
