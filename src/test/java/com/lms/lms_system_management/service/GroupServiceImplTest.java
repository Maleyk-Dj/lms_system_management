package com.lms.lms_system_management.service;

import com.lms.lms_system_management.dao.GroupRepository;
import com.lms.lms_system_management.dto.group.GroupFilter;
import com.lms.lms_system_management.dto.group.NewGroupRequest;
import com.lms.lms_system_management.dto.group.UpdateGroupRequest;
import com.lms.lms_system_management.dto.group.GroupResponse;
import com.lms.lms_system_management.exception.NotFoundException;
import com.lms.lms_system_management.mapper.GroupMapper;
import com.lms.lms_system_management.model.GroupEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

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
        GroupEntity entity = new GroupEntity();
        entity.setName("Gruppa A");
        GroupEntity saved = new GroupEntity();
        saved.setId(1L);
        saved.setName("Gruppa A");
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
    void getById_whenExists_shouldReturnResponse() {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setId(1L);
        groupEntity.setName("Gruppa A");
        GroupResponse expected = new GroupResponse(1L, "Gruppa A");

        when(groupRepository.findByIdOrThrow(1L)).thenReturn(groupEntity);
        when(groupMapper.toResponse(groupEntity)).thenReturn(expected);

        GroupResponse result = groupService.getById(1L);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void getById_whenNotExists_shouldThrowNotFoundException() {
        when(groupRepository.findByIdOrThrow(99L)).thenThrow(new NotFoundException("not found"));

        assertThatThrownBy(() -> groupService.getById(99L))
                .isInstanceOf(NotFoundException.class);
    }

    // FIND ALL

    @Test
    void getAll_shouldReturnListOfResponses() {
        GroupFilter filter = new GroupFilter(null);
        Pageable pageable = PageRequest.of(0, 10);

        GroupEntity g1 = new GroupEntity();
        g1.setId(1L);
        g1.setName("Gruppa A");
        GroupEntity g2 = new GroupEntity();
        g2.setId(2L);
        g2.setName("Gruppa B");
        GroupResponse r1 = new GroupResponse(1L, "Gruppa A");
        GroupResponse r2 = new GroupResponse(2L, "Gruppa B");

        when(groupRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(g1, g2)));
        when(groupMapper.toResponse(g1)).thenReturn(r1);
        when(groupMapper.toResponse(g2)).thenReturn(r2);

        Page<GroupResponse> result = groupService.getAll(filter, pageable);

        assertThat(result).hasSize(2).containsExactly(r1, r2);
    }

    @Test
    void getAll_whenEmpty_shouldReturnEmptyList() {
        GroupFilter filter = new GroupFilter(null);
        Pageable pageable = PageRequest.of(0, 10);

        when(groupRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of()));

        assertThat(groupService.getAll(filter, pageable).getContent()).isEmpty();
    }

    // UPDATE

    @Test
    void update_whenExists_shouldUpdateAndReturnResponse() {
        UpdateGroupRequest request = new UpdateGroupRequest("Gruppa C");
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setId(1L);
        groupEntity.setName("Gruppa A");
        GroupEntity saved = new GroupEntity();
        saved.setId(1L);
        saved.setName("Gruppa C");
        GroupResponse expected = new GroupResponse(1L, "Gruppa C");

        when(groupRepository.findByIdOrThrow(1L)).thenReturn(groupEntity);
        when(groupRepository.save(groupEntity)).thenReturn(saved);
        when(groupMapper.toResponse(saved)).thenReturn(expected);

        GroupResponse result = groupService.update(request, 1L);

        assertThat(result).isEqualTo(expected);
        verify(groupMapper).updateGroup(request, groupEntity);
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
        when(groupRepository.softDeleteById(1L)).thenReturn(1);

        groupService.deleteById(1L);

        verify(groupRepository).softDeleteById(1L);
    }

    @Test
    void deleteById_whenNotExists_shouldThrowNotFoundException() {
        when(groupRepository.softDeleteById(99L)).thenReturn(0);

        assertThatThrownBy(() -> groupService.deleteById(99L))
                .isInstanceOf(NotFoundException.class);
        verify(groupRepository, never()).delete(any(GroupEntity.class));
    }
}
