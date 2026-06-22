package com.lms.lms_system_management.dao.specification;

import com.lms.lms_system_management.dto.student.StudentFilter;
import com.lms.lms_system_management.model.GroupEntity_;
import com.lms.lms_system_management.model.StudentEntity;
import com.lms.lms_system_management.model.StudentEntity_;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.ofNullable;

public class StudentSpecification {

    public static Specification<StudentEntity> build(StudentFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            ofNullable(filter.firstName()).ifPresent(firstName ->
                    predicates.add(cb.like(root.get(StudentEntity_.firstName), "%" + firstName + "%")));

            ofNullable(filter.lastName()).ifPresent(lastName ->
                    predicates.add(cb.like(root.get(StudentEntity_.lastName), "%" + lastName + "%")));

            ofNullable(filter.groupId()).ifPresent(groupId ->
                    predicates.add(cb.equal(root.get(StudentEntity_.groupEntity).get(GroupEntity_.id), groupId)));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
