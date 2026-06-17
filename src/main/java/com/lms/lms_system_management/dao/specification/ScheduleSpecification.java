package com.lms.lms_system_management.dao.specification;

import com.lms.lms_system_management.model.ScheduleEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class ScheduleSpecification {

    public static Specification<ScheduleEntity> hasGroupId(Long groupId) {
        return ((root, query, criteriaBuilder) ->
                groupId == null ? null : criteriaBuilder.equal(root.get("groupEntity").get("id"), groupId));
    }

    public static Specification<ScheduleEntity> hasDateFrom(LocalDateTime dateFrom) {
        return ((root, query, criteriaBuilder) ->
                dateFrom == null ? null : criteriaBuilder.greaterThanOrEqualTo(root.get("dateClass"), dateFrom));
    }

    public static Specification<ScheduleEntity> hasDateTo(LocalDateTime dateTo) {
        return ((root, query, criteriaBuilder) ->
                dateTo == null ? null : criteriaBuilder.lessThan(root.get("dateClass"), dateTo));
    }
}
