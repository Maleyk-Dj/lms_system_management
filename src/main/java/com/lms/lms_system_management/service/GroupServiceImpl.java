package com.lms.lms_system_management.service;

import com.lms.lms_system_management.dao.GroupRepository;
import com.lms.lms_system_management.dto.request.NewGroupRequest;
import com.lms.lms_system_management.dto.request.UpdateGroupRequest;
import com.lms.lms_system_management.dto.response.GroupResponse;
import com.lms.lms_system_management.exception.NotFoundException;
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

    @Transactional
    @Override
    public GroupResponse create(NewGroupRequest newGroup) {
        Group saved=groupRepository.save(groupMapper.toEntity(newGroup));
        return groupMapper.toResponse(saved);
    }

    @Transactional (readOnly = true)
    @Override
    public GroupResponse findById(Long id) {
        Group groupEntity = groupRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Группа с id " + id + "  не найдена"));
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
        Group updated = groupRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Группа с id " + id + "  не найдена"));
        groupMapper.updateGroup(updateGroupRequest, updated);
        Group saved = groupRepository.save(updated);
        return groupMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        Group deleted = groupRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Группа с таким ID " + id + "  не найдена"));
        groupRepository.delete(deleted);

    }
}
