package com.lms.lms_system_management.dao;

import com.lms.lms_system_management.exception.NotFoundException;
import com.lms.lms_system_management.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    default Teacher findByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new NotFoundException("Учитель с id " + id + " не найден"));
    }
}
