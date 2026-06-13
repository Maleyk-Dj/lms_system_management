package com.lms.lms_system_management.mapper;

import com.lms.lms_system_management.dto.teacher.NewTeacherRequest;
import com.lms.lms_system_management.dto.teacher.UpdateTeacherRequest;
import com.lms.lms_system_management.dto.teacher.TeacherResponse;
import com.lms.lms_system_management.model.Teacher;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "courses", ignore = true)
    Teacher toEntity(NewTeacherRequest teacher);

    TeacherResponse toResponse(Teacher teacher);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateEntity(UpdateTeacherRequest request, @MappingTarget Teacher teacher);
}
