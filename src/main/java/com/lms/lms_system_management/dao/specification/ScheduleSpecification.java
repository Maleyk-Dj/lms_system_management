package com.lms.lms_system_management.dao.specification;

import com.lms.lms_system_management.dto.schedule.ScheduleFilter;
import com.lms.lms_system_management.model.GroupEntity_;
import com.lms.lms_system_management.model.ScheduleEntity;
import com.lms.lms_system_management.model.ScheduleEntity_;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.ofNullable;

public class ScheduleSpecification {

    public static Specification<ScheduleEntity> builder(ScheduleFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            ofNullable(filter.groupId()).ifPresent(groupId ->
                    predicates.add(cb.equal(root.get(ScheduleEntity_.groupEntity).get(GroupEntity_.id), groupId)));

            ofNullable(filter.dateFrom()).ifPresent(dateFrom ->
                    predicates.add(cb.greaterThanOrEqualTo(root.get(ScheduleEntity_.dateClass), dateFrom)));

            ofNullable(filter.dateTo()).ifPresent(dateTo ->
                    predicates.add(cb.lessThanOrEqualTo(root.get(ScheduleEntity_.dateClass), dateTo)));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
