package com.lms.lms_system_management.mapper;

import com.lms.lms_system_management.dto.request.NewCourseRequest;
import com.lms.lms_system_management.dto.request.UpdateCourseRequest;
import com.lms.lms_system_management.dto.response.CourseResponse;
import com.lms.lms_system_management.model.Course;
import com.lms.lms_system_management.model.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper (componentModel = "spring")
public interface CourseMapper {
    @Mapping(target = "id", ignore = true)
    Course toEntity(NewCourseRequest course, Teacher teacher);
    CourseResponse toResponse(Course course);
    @Mapping(target = "id", ignore = true)
    void updateEntity (UpdateCourseRequest updateCourseRequest, Teacher teacher, @MappingTarget Course course);
}
