package com.lms.lms_system_management.service;

import com.lms.lms_system_management.dto.group.NewGroupRequest;
import com.lms.lms_system_management.dto.group.UpdateGroupRequest;
import com.lms.lms_system_management.dto.group.GroupResponse;

import java.util.List;

public interface GroupService {

    GroupResponse create(NewGroupRequest newGroup);

    GroupResponse findById(Long id);

    List<GroupResponse> findAll();

    GroupResponse update(UpdateGroupRequest updateGroupRequest, Long id);

    void deleteById(Long id);
}
