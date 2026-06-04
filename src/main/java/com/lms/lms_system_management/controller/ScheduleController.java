package com.lms.lms_system_management.controller;

import com.lms.lms_system_management.dto.request.NewScheduleRequest;
import com.lms.lms_system_management.dto.request.UpdateScheduleRequest;
import com.lms.lms_system_management.dto.response.ScheduleResponse;
import com.lms.lms_system_management.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
@Validated
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ScheduleResponse assignCourseTime(@RequestBody NewScheduleRequest request) {
        return scheduleService.assignCourseTime(request);
    }

    @PutMapping("/{schedule_id}")
    public ScheduleResponse update(@RequestBody UpdateScheduleRequest request,
                                   @PathVariable("schedule_id") Long id) {
        return scheduleService.update(id, request);
    }

    @DeleteMapping("/{schedule_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("schedule_id") Long id) {
        scheduleService.delete(id);
    }

    @GetMapping("/groups/{group_id}")
    public List<ScheduleResponse> getScheduleByGroup(@PathVariable("group_id") Long id) {
        return scheduleService.getScheduleByGroup(id);
    }
}
