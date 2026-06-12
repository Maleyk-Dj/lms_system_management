package com.lms.lms_system_management.service;

import com.lms.lms_system_management.dto.request.NewTeacherRequest;
import com.lms.lms_system_management.dto.request.UpdateTeacherRequest;
import com.lms.lms_system_management.dto.response.ScheduleResponse;
import com.lms.lms_system_management.dto.response.TeacherResponse;


import java.util.List;

public interface TeacherService {

    TeacherResponse create (NewTeacherRequest teacher);

    TeacherResponse getById(Long id);

    List<TeacherResponse> getAll();

    TeacherResponse update (UpdateTeacherRequest teacher, Long id);

    void deleteById(Long id);

    List <ScheduleResponse> getScheduleByTeacher(Long id);

}
