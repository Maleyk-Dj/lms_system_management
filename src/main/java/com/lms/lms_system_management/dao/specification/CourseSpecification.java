package com.lms.lms_system_management.dao.specification;

import com.lms.lms_system_management.model.CourseEntity;
import org.springframework.data.jpa.domain.Specification;

public class CourseSpecification {

    public static Specification<CourseEntity> hasName(String name) {

        return ((root, query, criteriaBuilder) ->
                name == null ? null : criteriaBuilder.like(root.get("name"), "%" + name + "%"));
    }

    public static Specification<CourseEntity> hasTeacherId(Long teacherId) {
        return ((root, query, criteriaBuilder) ->
                teacherId == null ? null : criteriaBuilder.equal(root.get("teacherEntity").get("id"), teacherId));
    }
}

