package com.lms.lms_system_management.dao;

import com.lms.lms_system_management.exception.NotFoundException;
import com.lms.lms_system_management.model.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long> {

    default CourseEntity findByIdOrThrow(Long id) {

        return findById(id).
                orElseThrow(() -> new NotFoundException("Курс с id " + id + " не найден"));
    }
}
