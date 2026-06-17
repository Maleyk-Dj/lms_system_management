package com.lms.lms_system_management.controller;

import com.lms.lms_system_management.dto.schedule.NewScheduleRequest;
import com.lms.lms_system_management.dto.schedule.ScheduleFilter;
import com.lms.lms_system_management.dto.schedule.UpdateScheduleRequest;
import com.lms.lms_system_management.dto.schedule.ScheduleResponse;
import com.lms.lms_system_management.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
        scheduleService.deleteById(id);
    }

    @GetMapping
    public Page<ScheduleResponse> getScheduleByGroup(@ModelAttribute ScheduleFilter filter, Pageable pageable) {
        return scheduleService.getScheduleByGroup(filter, pageable);
    }
}
