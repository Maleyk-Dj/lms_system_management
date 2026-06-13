package com.lms.lms_system_management.mapper;

import com.lms.lms_system_management.dto.student.NewStudentRequest;
import com.lms.lms_system_management.dto.student.UpdateStudentRequest;
import com.lms.lms_system_management.dto.student.StudentResponse;
import com.lms.lms_system_management.model.Group;
import com.lms.lms_system_management.model.Student;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(target = "id", ignore = true)
    Student toEntity(NewStudentRequest newStudentRequest);

    @Mapping(target = "id", ignore = true)
    Student toEntity(NewStudentRequest request, Group group);

    @Mapping(target = "groupId", source = "group.id")
    StudentResponse toResponse(Student student);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "group", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateStudent(UpdateStudentRequest request, @MappingTarget Student student);
}
