package com.lms.lms_system_management.dao.specification;

import com.lms.lms_system_management.model.StudentEntity;
import org.springframework.data.jpa.domain.Specification;

public class StudentSpecification {

    public static Specification<StudentEntity> hasFirstName(String firstName) {

        return (root, query, cb) ->
                firstName == null ? null : cb.like(root.get("firstName"), "%" + firstName + "%");
    }

    public static Specification<StudentEntity> hasLastName(String lastName) {
        return ((root, query, criteriaBuilder) ->
                lastName == null ? null : criteriaBuilder.like(root.get("lastName"), "%" + lastName + "%"));
    }

    public static Specification<StudentEntity> hasGroupId(Long groupId) {
        return (root, query, cb) ->
                groupId == null ? null : cb.equal(root.get("groupEntity").get("id"), groupId);
    }
}
