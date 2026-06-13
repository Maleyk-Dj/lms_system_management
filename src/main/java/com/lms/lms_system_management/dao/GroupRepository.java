package com.lms.lms_system_management.dao;

import com.lms.lms_system_management.exception.NotFoundException;
import com.lms.lms_system_management.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    default Group findByIdOrThrow(Long id) {

        return findById(id).orElseThrow(
                ()-> new NotFoundException("Группа с ID " + id + "  не найдена."));
    }
}
