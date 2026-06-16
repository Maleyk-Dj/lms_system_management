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
    @Mapping(target = "groupEntity", source = "groupEntity")
    @Mapping(target = "courseEntity", source = "courseEntity")
    @Mapping(source = "newScheduleRequest.date", target = "dateClass")
    ScheduleEntity toEntity(NewScheduleRequest newScheduleRequest, GroupEntity groupEntity,
                            CourseEntity courseEntity);

    @Mapping(source = "dateClass", target = "date")
    @Mapping(source = "groupEntity", target = "group")
    @Mapping(source = "courseEntity", target = "course")
    ScheduleResponse toResponse(ScheduleEntity scheduleEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "groupEntity", source = "groupEntity")
    @Mapping(target = "courseEntity", source = "courseEntity")
    @Mapping(source = "request.date", target = "dateClass")
    void updateSchedule(UpdateScheduleRequest request, GroupEntity groupEntity,
                        CourseEntity courseEntity, @MappingTarget ScheduleEntity scheduleEntity);
}
