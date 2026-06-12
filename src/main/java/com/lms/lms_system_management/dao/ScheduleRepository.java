package com.lms.lms_system_management.dao;

import com.lms.lms_system_management.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByCourse_Teacher_Id(Long teacherId);

    List<Schedule> findAllByGroup_Id(Long groupId);
}
