package com.lms.lms_system_management.service;

import com.lms.lms_system_management.dao.GroupRepository;
import com.lms.lms_system_management.dao.StudentRepository;
import com.lms.lms_system_management.dto.student.NewStudentRequest;
import com.lms.lms_system_management.dto.student.StudentFilter;
import com.lms.lms_system_management.dto.student.UpdateStudentRequest;
import com.lms.lms_system_management.dto.student.StudentResponse;
import com.lms.lms_system_management.exception.AlreadyExistsException;
import com.lms.lms_system_management.exception.NotFoundException;
import com.lms.lms_system_management.mapper.StudentMapper;
import com.lms.lms_system_management.model.GroupEntity;
import com.lms.lms_system_management.model.StudentEntity;
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
class StudentEntityServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentMapper studentMapper;

    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private StudentServiceImpl studentServiceImpl;

    // CREATE

    @Test
    void create_whenGroupExists_shouldSaveAndReturnResponse() {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setId(1L);
        groupEntity.setName("Gruppa A");

        NewStudentRequest request = new NewStudentRequest("Ivan", "Petrov", 1L);

        StudentEntity entity = new StudentEntity();
        entity.setFirstName("Ivan");
        entity.setLastName("Petrov");
        entity.setGroupEntity(groupEntity);

        StudentEntity saved = new StudentEntity();
        saved.setId(10L);
        saved.setFirstName("Ivan");
        saved.setLastName("Petrov");
        saved.setGroupEntity(groupEntity);

        StudentResponse expected = new StudentResponse(10L, "Ivan", "Petrov", 1L);

        when(groupRepository.findByIdOrThrow(1L)).thenReturn(groupEntity);
        when(studentMapper.toEntity(request, groupEntity)).thenReturn(entity);
        when(studentRepository.save(entity)).thenReturn(saved);
        when(studentMapper.toResponse(saved)).thenReturn(expected);

        StudentResponse result = studentServiceImpl.create(request);

        assertThat(result).isEqualTo(expected);
        verify(studentRepository).save(entity);
    }

    @Test
    void create_whenGroupNotExists_shouldThrowNotFoundException() {
        NewStudentRequest request = new NewStudentRequest("Ivan", "Petrov", 99L);
        when(groupRepository.findByIdOrThrow(99L)).thenThrow(new NotFoundException("not found"));

        assertThatThrownBy(() -> studentServiceImpl.create(request))
                .isInstanceOf(NotFoundException.class);
        verify(studentRepository, never()).save(any());
    }

    // GET BY ID

    @Test
    void getById_whenExists_shouldReturnResponse() {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setId(1L);

        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setId(5L);
        studentEntity.setFirstName("Ivan");
        studentEntity.setLastName("Petrov");
        studentEntity.setGroupEntity(groupEntity);

        StudentResponse expected = new StudentResponse(5L, "Ivan", "Petrov", 1L);

        when(studentRepository.findByIdOrThrow(5L)).thenReturn(studentEntity);
        when(studentMapper.toResponse(studentEntity)).thenReturn(expected);

        StudentResponse result = studentServiceImpl.getById(5L);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void getById_whenNotExists_shouldThrowNotFoundException() {
        when(studentRepository.findByIdOrThrow(99L)).thenThrow(new NotFoundException("not found"));

        assertThatThrownBy(() -> studentServiceImpl.getById(99L))
                .isInstanceOf(NotFoundException.class);
    }

    // GET ALL

    @Test
    void getAll_shouldReturnListOfResponses() {
        StudentFilter filter = new StudentFilter(null, null, null);
        Pageable pageable = PageRequest.of(0, 10);

        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setId(1L);

        StudentEntity s1 = new StudentEntity();
        s1.setId(1L);
        s1.setFirstName("Ivan");
        s1.setLastName("Petrov");
        s1.setGroupEntity(groupEntity);

        StudentEntity s2 = new StudentEntity();
        s2.setId(2L);
        s2.setFirstName("Anna");
        s2.setLastName("Ivanova");
        s2.setGroupEntity(groupEntity);

        StudentResponse r1 = new StudentResponse(1L, "Ivan", "Petrov", 1L);
        StudentResponse r2 = new StudentResponse(2L, "Anna", "Ivanova", 1L);

        when(studentRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(s1, s2)));
        when(studentMapper.toResponse(s1)).thenReturn(r1);
        when(studentMapper.toResponse(s2)).thenReturn(r2);

        Page<StudentResponse> result = studentServiceImpl.getAll(filter, pageable);

        assertThat(result.getContent()).hasSize(2).containsExactly(r1, r2);
    }

    @Test
    void getAll_whenEmpty_shouldReturnEmptyList() {
        StudentFilter filter = new StudentFilter(null, null, null);
        Pageable pageable = PageRequest.of(0, 10);

        when(studentRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of()));

        assertThat(studentServiceImpl.getAll(filter, pageable).getContent()).isEmpty();
    }

    // UPDATE

    @Test
    void update_withoutGroupChange_shouldUpdateAndReturnResponse() {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setId(1L);

        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setId(5L);
        studentEntity.setFirstName("Ivan");
        studentEntity.setLastName("Petrov");
        studentEntity.setGroupEntity(groupEntity);

        UpdateStudentRequest request = new UpdateStudentRequest("Oleg", "Olegov", null);

        StudentEntity saved = new StudentEntity();
        saved.setId(5L);
        saved.setFirstName("Oleg");
        saved.setLastName("Olegov");
        saved.setGroupEntity(groupEntity);

        StudentResponse expected = new StudentResponse(5L, "Oleg", "Olegov", 1L);

        when(studentRepository.findByIdOrThrow(5L)).thenReturn(studentEntity);
        when(studentRepository.save(studentEntity)).thenReturn(saved);
        when(studentMapper.toResponse(saved)).thenReturn(expected);

        StudentResponse result = studentServiceImpl.update(request, 5L);

        assertThat(result).isEqualTo(expected);
        verify(studentMapper).updateStudent(request, studentEntity);
        verify(groupRepository, never()).findByIdOrThrow(any());
    }

    @Test
    void update_withGroupChange_shouldUpdateGroupAndReturnResponse() {
        GroupEntity oldGroupEntity = new GroupEntity();
        oldGroupEntity.setId(1L);

        GroupEntity newGroupEntity = new GroupEntity();
        newGroupEntity.setId(2L);

        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setId(5L);
        studentEntity.setFirstName("Ivan");
        studentEntity.setLastName("Petrov");
        studentEntity.setGroupEntity(oldGroupEntity);

        UpdateStudentRequest request = new UpdateStudentRequest(null, null, 2L);

        StudentEntity saved = new StudentEntity();
        saved.setId(5L);
        saved.setFirstName("Ivan");
        saved.setLastName("Petrov");
        saved.setGroupEntity(newGroupEntity);

        StudentResponse expected = new StudentResponse(5L, "Ivan", "Petrov", 2L);

        when(studentRepository.findByIdOrThrow(5L)).thenReturn(studentEntity);
        when(groupRepository.findByIdOrThrow(2L)).thenReturn(newGroupEntity);
        when(studentRepository.save(studentEntity)).thenReturn(saved);
        when(studentMapper.toResponse(saved)).thenReturn(expected);

        StudentResponse result = studentServiceImpl.update(request, 5L);

        assertThat(result.groupId()).isEqualTo(2L);
    }

    @Test
    void update_whenStudentNotExists_shouldThrowNotFoundException() {
        UpdateStudentRequest request = new UpdateStudentRequest("Oleg", "Olegov", null);
        when(studentRepository.findByIdOrThrow(99L)).thenThrow(new NotFoundException("not found"));

        assertThatThrownBy(() -> studentServiceImpl.update(request, 99L))
                .isInstanceOf(NotFoundException.class);
        verify(studentRepository, never()).save(any());
    }

    @Test
    void update_whenNewGroupNotExists_shouldThrowNotFoundException() {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setId(1L);

        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setId(5L);
        studentEntity.setGroupEntity(groupEntity);

        UpdateStudentRequest request = new UpdateStudentRequest(null, null, 99L);

        when(studentRepository.findByIdOrThrow(5L)).thenReturn(studentEntity);
        when(groupRepository.findByIdOrThrow(99L)).thenThrow(new NotFoundException("not found"));

        assertThatThrownBy(() -> studentServiceImpl.update(request, 5L))
                .isInstanceOf(NotFoundException.class);
        verify(studentRepository, never()).save(any());
    }

    // DELETE

    @Test
    void deleteById_whenExists_shouldDelete() {
        when(studentRepository.softDeleteById(5L)).thenReturn(1);

        studentServiceImpl.deleteById(5L);

        verify(studentRepository).softDeleteById(5L);
    }

    @Test
    void deleteById_whenNotExists_shouldThrowNotFoundException() {
        when(studentRepository.softDeleteById(99L)).thenReturn(0);

        assertThatThrownBy(() -> studentServiceImpl.deleteById(99L))
                .isInstanceOf(NotFoundException.class);
        verify(studentRepository, never()).delete(any(StudentEntity.class));
    }

    // ADD TO GROUP

    @Test
    void addToGroup_whenValid_shouldUpdateGroupAndReturnResponse() {
        GroupEntity oldGroupEntity = new GroupEntity();
        oldGroupEntity.setId(1L);

        GroupEntity newGroupEntity = new GroupEntity();
        newGroupEntity.setId(2L);

        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setId(5L);
        studentEntity.setFirstName("Ivan");
        studentEntity.setLastName("Petrov");
        studentEntity.setGroupEntity(oldGroupEntity);

        StudentEntity saved = new StudentEntity();
        saved.setId(5L);
        saved.setFirstName("Ivan");
        saved.setLastName("Petrov");
        saved.setGroupEntity(newGroupEntity);

        StudentResponse expected = new StudentResponse(5L, "Ivan", "Petrov", 2L);

        when(studentRepository.findByIdOrThrow(5L)).thenReturn(studentEntity);
        when(groupRepository.findByIdOrThrow(2L)).thenReturn(newGroupEntity);
        when(studentRepository.save(studentEntity)).thenReturn(saved);
        when(studentMapper.toResponse(saved)).thenReturn(expected);

        StudentResponse result = studentServiceImpl.addToGroup(5L, 2L);

        assertThat(result.groupId()).isEqualTo(2L);
    }

    @Test
    void addToGroup_whenAlreadyInGroup_shouldThrowAlreadyExistsException() {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setId(1L);

        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setId(5L);
        studentEntity.setGroupEntity(groupEntity);

        when(studentRepository.findByIdOrThrow(5L)).thenReturn(studentEntity);
        when(groupRepository.findByIdOrThrow(1L)).thenReturn(groupEntity);

        assertThatThrownBy(() -> studentServiceImpl.addToGroup(5L, 1L))
                .isInstanceOf(AlreadyExistsException.class);
        verify(studentRepository, never()).save(any());
    }

    @Test
    void addToGroup_whenStudentNotExists_shouldThrowNotFoundException() {
        when(studentRepository.findByIdOrThrow(99L)).thenThrow(new NotFoundException("not found"));

        assertThatThrownBy(() -> studentServiceImpl.addToGroup(99L, 1L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void addToGroup_whenGroupNotExists_shouldThrowNotFoundException() {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setId(1L);

        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setId(5L);
        studentEntity.setGroupEntity(groupEntity);

        when(studentRepository.findByIdOrThrow(5L)).thenReturn(studentEntity);
        when(groupRepository.findByIdOrThrow(99L)).thenThrow(new NotFoundException("not found"));

        assertThatThrownBy(() -> studentServiceImpl.addToGroup(5L, 99L))
                .isInstanceOf(NotFoundException.class);
        verify(studentRepository, never()).save(any());
    }
}
