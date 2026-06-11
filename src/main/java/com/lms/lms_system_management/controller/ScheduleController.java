package com.lms.lms_system_management.controller;

import com.lms.lms_system_management.dto.request.NewScheduleRequest;
import com.lms.lms_system_management.dto.request.UpdateScheduleRequest;
import com.lms.lms_system_management.dto.response.ScheduleResponse;
import com.lms.lms_system_management.service.ScheduleService;
import jakarta.validation.Valid;
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


import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
@Validated
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ScheduleResponse assignCourseTime(@Valid @RequestBody NewScheduleRequest request) {
        return scheduleService.assignCourseTime(request);
    }

    @PutMapping("/{scheduleId}")
    public ScheduleResponse update(@Valid @RequestBody UpdateScheduleRequest request,
                                   @PathVariable("scheduleId") Long id) {
        return scheduleService.update(id, request);
    }

    @DeleteMapping("/{scheduleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("scheduleId") Long id) {
        scheduleService.delete(id);
    }

    @GetMapping("/groups/{groupId}")
    public List<ScheduleResponse> getScheduleByGroup(@PathVariable("groupId") Long id) {
        return scheduleService.getScheduleByGroup(id);
    }
}
