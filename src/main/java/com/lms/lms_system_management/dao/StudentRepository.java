package com.lms.lms_system_management.dao;

import com.lms.lms_system_management.exception.NotFoundException;
import com.lms.lms_system_management.model.Student;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    default Student findByIdOrThrow(Long id) {

        return findById(id)
                .orElseThrow(() -> new NotFoundException("Студент с id " + id + " не найден"));
    }
}
