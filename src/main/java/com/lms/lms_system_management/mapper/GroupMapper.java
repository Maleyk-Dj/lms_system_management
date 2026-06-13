package com.lms.lms_system_management.mapper;

import com.lms.lms_system_management.dto.group.NewGroupRequest;
import com.lms.lms_system_management.dto.group.UpdateGroupRequest;
import com.lms.lms_system_management.dto.group.GroupResponse;
import com.lms.lms_system_management.model.Group;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    @Mapping(target = "id", ignore = true)
    Group toEntity(NewGroupRequest newGroupRequest);

    GroupResponse toResponse(Group group);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateGroup(UpdateGroupRequest updateGroupRequest, @MappingTarget Group group);
}
