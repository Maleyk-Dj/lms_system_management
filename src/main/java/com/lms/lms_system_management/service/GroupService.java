package com.lms.lms_system_management.service;

import com.lms.lms_system_management.dto.request.NewGroupRequest;
import com.lms.lms_system_management.dto.request.UpdateGroupRequest;
import com.lms.lms_system_management.dto.response.GroupResponse;

import java.util.List;

public interface GroupService {

    GroupResponse create (NewGroupRequest newGroup);

    GroupResponse findById(Long id);

    List<GroupResponse> findAll();

    GroupResponse update (UpdateGroupRequest updateGroupRequest, Long id);

    void deleteById(Long id);
}
