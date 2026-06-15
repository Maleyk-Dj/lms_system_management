package com.lms.lms_system_management.service;

import com.lms.lms_system_management.dto.teacher.NewTeacherRequest;
import com.lms.lms_system_management.dto.teacher.TeacherFilter;
import com.lms.lms_system_management.dto.teacher.UpdateTeacherRequest;
import com.lms.lms_system_management.dto.schedule.ScheduleResponse;
import com.lms.lms_system_management.dto.teacher.TeacherResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

public interface TeacherService {

    TeacherResponse create(NewTeacherRequest teacher);

    TeacherResponse getById(Long id);

    Page<TeacherResponse> getAll(TeacherFilter filter, Pageable pageable);

    TeacherResponse update(UpdateTeacherRequest teacher, Long id);

    void deleteById(Long id);

    List<ScheduleResponse> getScheduleByTeacher(Long id);

}
