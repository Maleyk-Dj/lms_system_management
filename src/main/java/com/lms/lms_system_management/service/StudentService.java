package com.lms.lms_system_management.service;

import com.lms.lms_system_management.dto.student.NewStudentRequest;
import com.lms.lms_system_management.dto.student.StudentFilter;
import com.lms.lms_system_management.dto.student.UpdateStudentRequest;
import com.lms.lms_system_management.dto.student.StudentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentService {

    StudentResponse create(NewStudentRequest newStudentRequest);

    StudentResponse getById(Long id);

    Page<StudentResponse> getAll(StudentFilter filter, Pageable pageable);

    StudentResponse update(UpdateStudentRequest updateStudentRequest, Long id);

    void deleteById(Long id);

    StudentResponse addToGroup(Long studentId, Long groupId);
}
