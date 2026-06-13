package com.lms.lms_system_management.service;

import com.lms.lms_system_management.dto.student.NewStudentRequest;
import com.lms.lms_system_management.dto.student.UpdateStudentRequest;
import com.lms.lms_system_management.dto.student.StudentResponse;

import java.util.List;

public interface StudentService {

    StudentResponse create(NewStudentRequest newStudentRequest);

    StudentResponse getById(Long id);

    List<StudentResponse> getAll();

    StudentResponse update(UpdateStudentRequest updateStudentRequest, Long id);

    void deleteById(Long id);

    StudentResponse addToGroup(Long studentId, Long groupId);
}
