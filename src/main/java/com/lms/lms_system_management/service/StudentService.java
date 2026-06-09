package com.lms.lms_system_management.service;

import com.lms.lms_system_management.dto.request.NewStudentRequest;
import com.lms.lms_system_management.dto.request.UpdateStudentRequest;
import com.lms.lms_system_management.dto.response.GroupResponse;
import com.lms.lms_system_management.dto.response.StudentResponse;

import java.util.List;

public interface StudentService {
    StudentResponse create (NewStudentRequest newStudentRequest);
    StudentResponse getById(Long id);
    List<StudentResponse> getAll();
    StudentResponse update (UpdateStudentRequest updateStudentRequest, Long id);
    void deleteById(Long id);
    StudentResponse addToGroup (Long studentId, Long groupId);
}
