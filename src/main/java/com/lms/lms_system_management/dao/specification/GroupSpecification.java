package com.lms.lms_system_management.dao.specification;

import com.lms.lms_system_management.dto.group.GroupFilter;
import com.lms.lms_system_management.model.GroupEntity;
import com.lms.lms_system_management.model.GroupEntity_;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.ofNullable;

public class GroupSpecification {

    public static Specification<GroupEntity> builder(GroupFilter filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            ofNullable(filter.name()).ifPresent(name ->
                predicates.add(criteriaBuilder.like(root.get(GroupEntity_.name), "%" + name + "%")));

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            };
        }
    }

