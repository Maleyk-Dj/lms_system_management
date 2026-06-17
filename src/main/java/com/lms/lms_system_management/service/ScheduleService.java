package com.lms.lms_system_management.service;

import com.lms.lms_system_management.dto.schedule.NewScheduleRequest;
import com.lms.lms_system_management.dto.schedule.ScheduleFilter;
import com.lms.lms_system_management.dto.schedule.UpdateScheduleRequest;
import com.lms.lms_system_management.dto.schedule.ScheduleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ScheduleService {

    ScheduleResponse assignCourseTime(NewScheduleRequest request);

    ScheduleResponse update(Long scheduleId, UpdateScheduleRequest request);

    void delete(Long id);

    Page<ScheduleResponse> getScheduleByGroup(ScheduleFilter filter, Pageable pageable);
}
