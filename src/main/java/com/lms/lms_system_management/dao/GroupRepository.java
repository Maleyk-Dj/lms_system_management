package com.lms.lms_system_management.dao;

import com.lms.lms_system_management.exception.NotFoundException;
import com.lms.lms_system_management.model.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, Long>,
        JpaSpecificationExecutor<GroupEntity> {

    default GroupEntity findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(
                () -> new NotFoundException("Группа с ID " + id + "  не найдена."));
    }

    @Modifying
    @Query("UPDATE GroupEntity g SET g.deleted = true WHERE g.id= :id")
    int softDeleteById(@Param("id") Long id);
}
