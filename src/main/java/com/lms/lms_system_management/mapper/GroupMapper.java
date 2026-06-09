package com.lms.lms_system_management.mapper;

import com.lms.lms_system_management.dto.request.NewGroupRequest;
import com.lms.lms_system_management.dto.request.UpdateGroupRequest;
import com.lms.lms_system_management.dto.response.GroupResponse;
import com.lms.lms_system_management.model.Group;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    @Mapping(target = "id", ignore = true)
    Group toEntity(NewGroupRequest newGroupRequest);

    GroupResponse toResponse(Group group);

    @Mapping(target = "id", ignore = true)
    void updateGroup(UpdateGroupRequest updateGroupRequest, @MappingTarget Group group);
}
