package com.lms.lms_system_management.dao;

import com.lms.lms_system_management.exception.NotFoundException;
import com.lms.lms_system_management.model.TeacherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<TeacherEntity, Long>,
        JpaSpecificationExecutor<TeacherEntity> {

    default TeacherEntity findByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new NotFoundException("Учитель с id " + id + " не найден"));
    }

    @Modifying
    @Query("UPDATE TeacherEntity t set t.deleted=true WHERE t.id=:id")
    int softDeletedById(@Param("id") Long id);

    @Query(value = "SELECT * FROM teachers WHERE id = :id", nativeQuery = true)
    Optional<TeacherEntity> findByIdIgnoringDeletedFlag(@Param("id") Long id);
}
