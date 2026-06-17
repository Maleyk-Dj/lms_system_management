package com.lms.lms_system_management.dao;

import com.lms.lms_system_management.exception.NotFoundException;
import com.lms.lms_system_management.model.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, Long>,
        JpaSpecificationExecutor<StudentEntity> {

    default StudentEntity findByIdOrThrow(Long id) {

        return findById(id)
                .orElseThrow(() -> new NotFoundException("Студент с id " + id + " не найден"));
    }
}
