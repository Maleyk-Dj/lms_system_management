package com.lms.lms_system_management.service;

import com.lms.lms_system_management.dto.teacher.NewTeacherRequest;
import com.lms.lms_system_management.dto.teacher.UpdateTeacherRequest;
import com.lms.lms_system_management.dto.schedule.ScheduleResponse;
import com.lms.lms_system_management.dto.teacher.TeacherResponse;


import java.util.List;

public interface TeacherService {

    TeacherResponse create(NewTeacherRequest teacher);

    TeacherResponse getById(Long id);

    List<TeacherResponse> getAll();

    TeacherResponse update(UpdateTeacherRequest teacher, Long id);

    void deleteById(Long id);

    List<ScheduleResponse> getScheduleByTeacher(Long id);

}
