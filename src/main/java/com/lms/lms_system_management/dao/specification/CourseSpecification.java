package com.lms.lms_system_management.dao.specification;

import com.lms.lms_system_management.dto.course.CourseFilter;
import com.lms.lms_system_management.model.CourseEntity;
import com.lms.lms_system_management.model.CourseEntity_;
import com.lms.lms_system_management.model.TeacherEntity_;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.ofNullable;

public class CourseSpecification {

    public static Specification<CourseEntity> builder(CourseFilter filter) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            ofNullable(filter.name()).ifPresent(name ->
                    predicates.add(criteriaBuilder.like(root.get(CourseEntity_.name), "%" + name + "%")));

            ofNullable(filter.teacherId()).ifPresent(teacherId ->
                    predicates.add(criteriaBuilder.equal(root.get(CourseEntity_.teacherEntity)
                            .get(TeacherEntity_.id), teacherId)));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

