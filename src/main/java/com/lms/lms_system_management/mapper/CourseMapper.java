package com.lms.lms_system_management.mapper;

import com.lms.lms_system_management.dto.course.NewCourseRequest;
import com.lms.lms_system_management.dto.course.UpdateCourseRequest;
import com.lms.lms_system_management.dto.course.CourseResponse;
import com.lms.lms_system_management.model.CourseEntity;
import com.lms.lms_system_management.model.TeacherEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {TeacherMapper.class})
public interface CourseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "teacherEntity", source = "teacherEntity")
    CourseEntity toEntity(NewCourseRequest course, TeacherEntity teacherEntity);

    @Mapping(target = "teacher", source = "teacherEntity")
    CourseResponse toResponse(CourseEntity courseEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "teacherEntity", source = "teacherEntity")
    void updateEntity(UpdateCourseRequest updateCourseRequest, TeacherEntity teacherEntity, @MappingTarget CourseEntity courseEntity);
}
