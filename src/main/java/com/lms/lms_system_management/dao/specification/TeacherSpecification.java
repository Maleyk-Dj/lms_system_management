package com.lms.lms_system_management.dao.specification;

import com.lms.lms_system_management.dto.teacher.TeacherFilter;
import com.lms.lms_system_management.model.TeacherEntity;
import com.lms.lms_system_management.model.TeacherEntity_;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.ofNullable;

public class TeacherSpecification {

    public static Specification<TeacherEntity> build(TeacherFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            ofNullable(filter.firstName()).ifPresent(firstName ->
                    predicates.add(cb.like(root.get(TeacherEntity_.firstName), "%" + firstName + "%"))
            );

            ofNullable(filter.lastName()).ifPresent(lastName ->
                    predicates.add(cb.like(root.get(TeacherEntity_.lastName), "%" + lastName + "%"))
            );

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

