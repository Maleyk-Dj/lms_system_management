package com.lms.lms_system_management.service;

import com.lms.lms_system_management.dto.request.NewScheduleRequest;
import com.lms.lms_system_management.dto.request.UpdateScheduleRequest;
import com.lms.lms_system_management.dto.response.ScheduleResponse;

import java.util.List;

public interface ScheduleService {
    ScheduleResponse assignCourseTime (NewScheduleRequest request);

    ScheduleResponse update (Long scheduleId, UpdateScheduleRequest request);
     void delete (Long id);

     List<ScheduleResponse> getScheduleByGroup(Long groupId);
}
