package com.lms.lms_system_management.service;

import com.lms.lms_system_management.dao.GroupRepository;
import com.lms.lms_system_management.dto.group.NewGroupRequest;
import com.lms.lms_system_management.dto.group.UpdateGroupRequest;
import com.lms.lms_system_management.dto.group.GroupResponse;
import com.lms.lms_system_management.mapper.GroupMapper;
import com.lms.lms_system_management.model.Group;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;

    @Override
    public GroupResponse create(NewGroupRequest newGroup) {

        Group saved = groupRepository.save(groupMapper.toEntity(newGroup));
        return groupMapper.toResponse(saved);
    }

    @Override
    public GroupResponse findById(Long id) {

        Group groupEntity = groupRepository.findByIdOrThrow(id);
        return groupMapper.toResponse(groupEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public List<GroupResponse> findAll() {

        return groupRepository.findAll()
                .stream()
                .map(groupMapper::toResponse)
                .toList();
    }

    @Transactional
    @Override
    public GroupResponse update(UpdateGroupRequest updateGroupRequest, Long id) {

        Group updated = groupRepository.findByIdOrThrow(id);
        groupMapper.updateGroup(updateGroupRequest, updated);
        Group saved = groupRepository.save(updated);
        return groupMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {

        Group deleted = groupRepository.findByIdOrThrow(id);
        groupRepository.delete(deleted);
    }
}
