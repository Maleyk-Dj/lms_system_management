package com.lms.lms_system_management.dao.specification;

import com.lms.lms_system_management.model.TeacherEntity;
import org.springframework.data.jpa.domain.Specification;

public class TeacherSpecification {

    public static Specification<TeacherEntity> hasFirstName(String firstName) {

        return (root, query, cb) ->
                firstName == null ? null : cb.like(root.get("firstName"), "%" + firstName + "%");
    }

    public static Specification<TeacherEntity> hasLastName(String lastName) {
        return (root, query, cb) ->
                lastName==null ? null : cb.like(root.get("lastName"), "%" + lastName + "%");
    }
}
