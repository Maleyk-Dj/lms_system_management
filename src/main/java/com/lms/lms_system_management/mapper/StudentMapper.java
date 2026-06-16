package com.lms.lms_system_management.mapper;

import com.lms.lms_system_management.dto.student.NewStudentRequest;
import com.lms.lms_system_management.dto.student.UpdateStudentRequest;
import com.lms.lms_system_management.dto.student.StudentResponse;
import com.lms.lms_system_management.model.GroupEntity;
import com.lms.lms_system_management.model.StudentEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "groupEntity", ignore = true)
    StudentEntity toEntity(NewStudentRequest newStudentRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "groupEntity", source = "groupEntity")
    StudentEntity toEntity(NewStudentRequest request, GroupEntity groupEntity);

    @Mapping(target = "groupId", source = "groupEntity.id")
    StudentResponse toResponse(StudentEntity studentEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "groupEntity", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateStudent(UpdateStudentRequest request, @MappingTarget StudentEntity studentEntity);
}
