package com.lms.lms_system_management.mapper;

import com.lms.lms_system_management.dto.schedule.NewScheduleRequest;
import com.lms.lms_system_management.dto.schedule.UpdateScheduleRequest;
import com.lms.lms_system_management.dto.schedule.ScheduleResponse;
import com.lms.lms_system_management.model.CourseEntity;
import com.lms.lms_system_management.model.GroupEntity;
import com.lms.lms_system_management.model.ScheduleEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "newScheduleRequest.date", target = "dateClass")
    ScheduleEntity toEntity(NewScheduleRequest newScheduleRequest, GroupEntity groupEntity, CourseEntity courseEntity);

    @Mapping(source = "dateClass", target = "date")
    ScheduleResponse toResponse(ScheduleEntity scheduleEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "group", source = "group")
    @Mapping(target = "course", source = "course")
    @Mapping(source = "request.date", target = "dateClass")
    void updateSchedule(UpdateScheduleRequest request, GroupEntity groupEntity, CourseEntity courseEntity, @MappingTarget ScheduleEntity scheduleEntity);
}
