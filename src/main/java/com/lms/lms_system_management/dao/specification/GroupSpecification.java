package com.lms.lms_system_management.dao.specification;

import com.lms.lms_system_management.model.GroupEntity;
import org.springframework.data.jpa.domain.Specification;

public class GroupSpecification {

    public static Specification<GroupEntity> hasName(String name) {
        return ((root, query, criteriaBuilder) ->
                name == null ? null : criteriaBuilder.like(root.get("name"), "%" + name + "%"));
    }
}
