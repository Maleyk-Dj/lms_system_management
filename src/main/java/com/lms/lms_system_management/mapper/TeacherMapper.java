package com.lms.lms_system_management.mapper;

import com.lms.lms_system_management.dto.request.NewTeacherRequest;
import com.lms.lms_system_management.dto.request.UpdateTeacherRequest;
import com.lms.lms_system_management.dto.response.TeacherResponse;
import com.lms.lms_system_management.model.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TeacherMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "courses", ignore = true)
    Teacher toEntity(NewTeacherRequest teacher);

    TeacherResponse toResponse(Teacher teacher);

    @Mapping(target = "id", ignore = true)
    void updateEntity(UpdateTeacherRequest request, @MappingTarget Teacher teacher);
}
