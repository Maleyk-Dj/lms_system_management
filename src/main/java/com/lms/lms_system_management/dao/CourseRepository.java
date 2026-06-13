package com.lms.lms_system_management.dao;

import com.lms.lms_system_management.model.Course;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    default Course findByIdOrThrow(Long id) {

        return findById(id).
                orElseThrow(() -> new EntityNotFoundException("Курс с id " + id + " не найден"));
    }
}
