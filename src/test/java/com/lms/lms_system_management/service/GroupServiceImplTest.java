package com.lms.lms_system_management.service;

import com.lms.lms_system_management.dao.GroupRepository;
import com.lms.lms_system_management.dto.group.NewGroupRequest;
import com.lms.lms_system_management.dto.group.UpdateGroupRequest;
import com.lms.lms_system_management.dto.group.GroupResponse;
import com.lms.lms_system_management.exception.NotFoundException;
import com.lms.lms_system_management.mapper.GroupMapper;
import com.lms.lms_system_management.model.Group;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GroupServiceImplTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private GroupMapper groupMapper;

    @InjectMocks
    private GroupServiceImpl groupService;

    // CREATE

    @Test
    void create_shouldSaveAndReturnResponse() {
        NewGroupRequest request = new NewGroupRequest("Gruppa A");
        Group entity = Group.builder().name("Gruppa A").build();
        Group saved = Group.builder().id(1L).name("Gruppa A").build();
        GroupResponse expected = new GroupResponse(1L, "Gruppa A");

        when(groupMapper.toEntity(request)).thenReturn(entity);
        when(groupRepository.save(entity)).thenReturn(saved);
        when(groupMapper.toResponse(saved)).thenReturn(expected);

        GroupResponse result = groupService.create(request);

        assertThat(result).isEqualTo(expected);
        verify(groupRepository).save(entity);
    }

    // FIND BY ID

    @Test
    void findById_whenExists_shouldReturnResponse() {
        Group group = Group.builder().id(1L).name("Gruppa A").build();
        GroupResponse expected = new GroupResponse(1L, "Gruppa A");

        when(groupRepository.findByIdOrThrow(1L)).thenReturn(group);
        when(groupMapper.toResponse(group)).thenReturn(expected);

        GroupResponse result = groupService.findById(1L);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void findById_whenNotExists_shouldThrowNotFoundException() {
        when(groupRepository.findByIdOrThrow(99L)).thenThrow(new NotFoundException("not found"));

        assertThatThrownBy(() -> groupService.findById(99L))
                .isInstanceOf(NotFoundException.class);
    }

    // FIND ALL

    @Test
    void findAll_shouldReturnListOfResponses() {
        Group g1 = Group.builder().id(1L).name("Gruppa A").build();
        Group g2 = Group.builder().id(2L).name("Gruppa B").build();
        GroupResponse r1 = new GroupResponse(1L, "Gruppa A");
        GroupResponse r2 = new GroupResponse(2L, "Gruppa B");

        when(groupRepository.findAll()).thenReturn(List.of(g1, g2));
        when(groupMapper.toResponse(g1)).thenReturn(r1);
        when(groupMapper.toResponse(g2)).thenReturn(r2);

        List<GroupResponse> result = groupService.findAll();

        assertThat(result).hasSize(2).containsExactly(r1, r2);
    }

    @Test
    void findAll_whenEmpty_shouldReturnEmptyList() {
        when(groupRepository.findAll()).thenReturn(List.of());

        assertThat(groupService.findAll()).isEmpty();
    }

    // UPDATE

    @Test
    void update_whenExists_shouldUpdateAndReturnResponse() {
        UpdateGroupRequest request = new UpdateGroupRequest("Gruppa C");
        Group group = Group.builder().id(1L).name("Gruppa A").build();
        Group saved = Group.builder().id(1L).name("Gruppa C").build();
        GroupResponse expected = new GroupResponse(1L, "Gruppa C");

        when(groupRepository.findByIdOrThrow(1L)).thenReturn(group);
        when(groupRepository.save(group)).thenReturn(saved);
        when(groupMapper.toResponse(saved)).thenReturn(expected);

        GroupResponse result = groupService.update(request, 1L);

        assertThat(result).isEqualTo(expected);
        verify(groupMapper).updateGroup(request, group);
    }

    @Test
    void update_whenNotExists_shouldThrowNotFoundException() {
        UpdateGroupRequest request = new UpdateGroupRequest("Gruppa C");
        when(groupRepository.findByIdOrThrow(99L)).thenThrow(new NotFoundException("not found"));

        assertThatThrownBy(() -> groupService.update(request, 99L))
                .isInstanceOf(NotFoundException.class);
        verify(groupRepository, never()).save(any());
    }

    // DELETE

    @Test
    void deleteById_whenExists_shouldDelete() {
        Group group = Group.builder().id(1L).name("Gruppa A").build();
        when(groupRepository.findByIdOrThrow(1L)).thenReturn(group);

        groupService.deleteById(1L);

        verify(groupRepository).delete(group);
    }

    @Test
    void deleteById_whenNotExists_shouldThrowNotFoundException() {
        when(groupRepository.findByIdOrThrow(99L)).thenThrow(new NotFoundException("not found"));

        assertThatThrownBy(() -> groupService.deleteById(99L))
                .isInstanceOf(NotFoundException.class);
        verify(groupRepository, never()).delete(any());
    }
}
