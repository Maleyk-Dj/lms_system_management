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
    StudentEntity toEntity(NewStudentRequest newStudentRequest);

    @Mapping(target = "id", ignore = true)
    StudentEntity toEntity(NewStudentRequest request, GroupEntity groupEntity);

    @Mapping(target = "groupId", source = "group.id")
    StudentResponse toResponse(StudentEntity studentEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "group", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateStudent(UpdateStudentRequest request, @MappingTarget StudentEntity studentEntity);
}
