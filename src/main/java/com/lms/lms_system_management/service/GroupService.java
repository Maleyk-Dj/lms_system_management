package com.lms.lms_system_management.service;

import com.lms.lms_system_management.dto.group.GroupFilter;
import com.lms.lms_system_management.dto.group.NewGroupRequest;
import com.lms.lms_system_management.dto.group.UpdateGroupRequest;
import com.lms.lms_system_management.dto.group.GroupResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GroupService {

    GroupResponse create(NewGroupRequest newGroup);

    GroupResponse findById(Long id);

    Page<GroupResponse> findAll(GroupFilter filter, Pageable pageable);

    GroupResponse update(UpdateGroupRequest updateGroupRequest, Long id);

    void deleteById(Long id);
}
