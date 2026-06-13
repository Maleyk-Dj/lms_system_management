package com.lms.lms_system_management.mapper;

import com.lms.lms_system_management.dto.schedule.NewScheduleRequest;
import com.lms.lms_system_management.dto.schedule.UpdateScheduleRequest;
import com.lms.lms_system_management.dto.schedule.ScheduleResponse;
import com.lms.lms_system_management.model.Course;
import com.lms.lms_system_management.model.Group;
import com.lms.lms_system_management.model.Schedule;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {

    @Mapping(target = "id", ignore = true)
    Schedule toEntity(NewScheduleRequest newScheduleRequest, Group group, Course course);

    ScheduleResponse toResponse(Schedule schedule);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "group", source = "group")
    @Mapping(target = "course", source = "course")
    void updateSchedule(UpdateScheduleRequest request, Group group, Course course, @MappingTarget Schedule schedule);
}
