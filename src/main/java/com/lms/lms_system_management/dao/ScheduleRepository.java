package com.lms.lms_system_management.dao;

import com.lms.lms_system_management.exception.NotFoundException;
import com.lms.lms_system_management.model.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long>,
        JpaSpecificationExecutor<ScheduleEntity> {

    default ScheduleEntity findByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new NotFoundException("Расписание с id " + id + " не найдено"));
    }

    List<ScheduleEntity> findByCourseEntityTeacherEntityId(Long teacherId);
}
