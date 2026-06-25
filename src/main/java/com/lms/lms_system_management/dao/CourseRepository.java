package com.lms.lms_system_management.dao;

import com.lms.lms_system_management.exception.NotFoundException;
import com.lms.lms_system_management.model.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long>,
        JpaSpecificationExecutor<CourseEntity> {

    default CourseEntity findByIdOrThrow(Long id) {
        return findById(id).
                orElseThrow(() -> new NotFoundException("Курс с id " + id + " не найден"));
    }

    @Modifying
    @Query("UPDATE TeacherEntity t SET t.deleted = true WHERE t.id= :id")
    int softDeleteById(@Param("id") Long id);
}
