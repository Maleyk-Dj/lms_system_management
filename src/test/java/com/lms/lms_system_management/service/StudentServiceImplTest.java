package com.lms.lms_system_management.service;

import com.lms.lms_system_management.dao.GroupRepository;
import com.lms.lms_system_management.dao.StudentRepository;
import com.lms.lms_system_management.dto.request.NewStudentRequest;
import com.lms.lms_system_management.dto.request.UpdateStudentRequest;
import com.lms.lms_system_management.dto.response.StudentResponse;
import com.lms.lms_system_management.exception.AlreadyExistsException;
import com.lms.lms_system_management.exception.NotFoundException;
import com.lms.lms_system_management.mapper.StudentMapper;
import com.lms.lms_system_management.model.Group;
import com.lms.lms_system_management.model.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

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
        Group group = Group.builder().id(1L).name("Gruppa A").build();
        NewStudentRequest request = new NewStudentRequest("Ivan", "Petrov", 1L);
        Student entity = Student.builder().firstName("Ivan").lastName("Petrov").group(group).build();
        Student saved = Student.builder().id(10L).firstName("Ivan").lastName("Petrov").group(group).build();
        StudentResponse expected = new StudentResponse(10L, "Ivan", "Petrov", 1L);

        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(studentMapper.toEntity(request, group)).thenReturn(entity);
        when(studentRepository.save(entity)).thenReturn(saved);
        when(studentMapper.toResponse(saved)).thenReturn(expected);

        StudentResponse result = studentServiceImpl.create(request);

        assertThat(result).isEqualTo(expected);
        verify(studentRepository).save(entity);
    }

    @Test
    void create_whenGroupNotExists_shouldThrowNotFoundException() {
        NewStudentRequest request = new NewStudentRequest("Ivan", "Petrov", 99L);
        when(groupRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentServiceImpl.create(request))
                .isInstanceOf(NotFoundException.class);
        verify(studentRepository, never()).save(any());
    }

    // GET BY ID

    @Test
    void getById_whenExists_shouldReturnResponse() {
        Group group = Group.builder().id(1L).build();
        Student student = Student.builder().id(5L).firstName("Ivan").lastName("Petrov").group(group).build();
        StudentResponse expected = new StudentResponse(5L, "Ivan", "Petrov", 1L);

        when(studentRepository.findById(5L)).thenReturn(Optional.of(student));
        when(studentMapper.toResponse(student)).thenReturn(expected);

        StudentResponse result = studentServiceImpl.getById(5L);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void getById_whenNotExists_shouldThrowNotFoundException() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentServiceImpl.getById(99L))
                .isInstanceOf(NotFoundException.class);
    }

    // GET ALL

    @Test
    void getAll_shouldReturnListOfResponses() {
        Group group = Group.builder().id(1L).build();
        Student s1 = Student.builder().id(1L).firstName("Ivan").lastName("Petrov").group(group).build();
        Student s2 = Student.builder().id(2L).firstName("Anna").lastName("Ivanova").group(group).build();
        StudentResponse r1 = new StudentResponse(1L, "Ivan", "Petrov", 1L);
        StudentResponse r2 = new StudentResponse(2L, "Anna", "Ivanova", 1L);

        when(studentRepository.findAll()).thenReturn(List.of(s1, s2));
        when(studentMapper.toResponse(s1)).thenReturn(r1);
        when(studentMapper.toResponse(s2)).thenReturn(r2);

        List<StudentResponse> result = studentServiceImpl.getAll();

        assertThat(result).hasSize(2).containsExactly(r1, r2);
    }

    @Test
    void getAll_whenEmpty_shouldReturnEmptyList() {
        when(studentRepository.findAll()).thenReturn(List.of());

        assertThat(studentServiceImpl.getAll()).isEmpty();
    }

    // UPDATE

    @Test
    void update_withoutGroupChange_shouldUpdateAndReturnResponse() {
        Group group = Group.builder().id(1L).build();
        Student student = Student.builder().id(5L).firstName("Ivan").lastName("Petrov").group(group).build();
        UpdateStudentRequest request = new UpdateStudentRequest("Oleg", "Olegov", null);
        Student saved = Student.builder().id(5L).firstName("Oleg").lastName("Olegov").group(group).build();
        StudentResponse expected = new StudentResponse(5L, "Oleg", "Olegov", 1L);

        when(studentRepository.findById(5L)).thenReturn(Optional.of(student));
        when(studentRepository.save(student)).thenReturn(saved);
        when(studentMapper.toResponse(saved)).thenReturn(expected);

        StudentResponse result = studentServiceImpl.update(request, 5L);

        assertThat(result).isEqualTo(expected);
        verify(studentMapper).updateStudent(request, student);
        verify(groupRepository, never()).findById(any());
    }

    @Test
    void update_withGroupChange_shouldUpdateGroupAndReturnResponse() {
        Group oldGroup = Group.builder().id(1L).build();
        Group newGroup = Group.builder().id(2L).build();
        Student student = Student.builder().id(5L).firstName("Ivan").lastName("Petrov").group(oldGroup).build();
        UpdateStudentRequest request = new UpdateStudentRequest(null, null, 2L);
        Student saved = Student.builder().id(5L).firstName("Ivan").lastName("Petrov").group(newGroup).build();
        StudentResponse expected = new StudentResponse(5L, "Ivan", "Petrov", 2L);

        when(studentRepository.findById(5L)).thenReturn(Optional.of(student));
        when(groupRepository.findById(2L)).thenReturn(Optional.of(newGroup));
        when(studentRepository.save(student)).thenReturn(saved);
        when(studentMapper.toResponse(saved)).thenReturn(expected);

        StudentResponse result = studentServiceImpl.update(request, 5L);

        assertThat(result.groupId()).isEqualTo(2L);
    }

    @Test
    void update_whenStudentNotExists_shouldThrowNotFoundException() {
        UpdateStudentRequest request = new UpdateStudentRequest("Oleg", "Olegov", null);
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentServiceImpl.update(request, 99L))
                .isInstanceOf(NotFoundException.class);
        verify(studentRepository, never()).save(any());
    }

    @Test
    void update_whenNewGroupNotExists_shouldThrowNotFoundException() {
        Group group = Group.builder().id(1L).build();
        Student student = Student.builder().id(5L).group(group).build();
        UpdateStudentRequest request = new UpdateStudentRequest(null, null, 99L);

        when(studentRepository.findById(5L)).thenReturn(Optional.of(student));
        when(groupRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentServiceImpl.update(request, 5L))
                .isInstanceOf(NotFoundException.class);
        verify(studentRepository, never()).save(any());
    }

    // DELETE

    @Test
    void deleteById_whenExists_shouldDelete() {
        Student student = Student.builder().id(5L).build();
        when(studentRepository.findById(5L)).thenReturn(Optional.of(student));

        studentServiceImpl.deleteById(5L);

        verify(studentRepository).delete(student);
    }

    @Test
    void deleteById_whenNotExists_shouldThrowNotFoundException() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentServiceImpl.deleteById(99L))
                .isInstanceOf(NotFoundException.class);
        verify(studentRepository, never()).delete(any());
    }

    // ADD TO GROUP

    @Test
    void addToGroup_whenValid_shouldUpdateGroupAndReturnResponse() {
        Group oldGroup = Group.builder().id(1L).build();
        Group newGroup = Group.builder().id(2L).build();
        Student student = Student.builder().id(5L).firstName("Ivan").lastName("Petrov").group(oldGroup).build();
        Student saved = Student.builder().id(5L).firstName("Ivan").lastName("Petrov").group(newGroup).build();
        StudentResponse expected = new StudentResponse(5L, "Ivan", "Petrov", 2L);

        when(studentRepository.findById(5L)).thenReturn(Optional.of(student));
        when(groupRepository.findById(2L)).thenReturn(Optional.of(newGroup));
        when(studentRepository.save(student)).thenReturn(saved);
        when(studentMapper.toResponse(saved)).thenReturn(expected);

        StudentResponse result = studentServiceImpl.addToGroup(5L, 2L);

        assertThat(result.groupId()).isEqualTo(2L);
    }

    @Test
    void addToGroup_whenAlreadyInGroup_shouldThrowAlreadyExistsException() {
        Group group = Group.builder().id(1L).build();
        Student student = Student.builder().id(5L).group(group).build();

        when(studentRepository.findById(5L)).thenReturn(Optional.of(student));
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));

        assertThatThrownBy(() -> studentServiceImpl.addToGroup(5L, 1L))
                .isInstanceOf(AlreadyExistsException.class);
        verify(studentRepository, never()).save(any());
    }

    @Test
    void addToGroup_whenStudentNotExists_shouldThrowNotFoundException() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentServiceImpl.addToGroup(99L, 1L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void addToGroup_whenGroupNotExists_shouldThrowNotFoundException() {
        Student student = Student.builder().id(5L).group(Group.builder().id(1L).build()).build();
        when(studentRepository.findById(5L)).thenReturn(Optional.of(student));
        when(groupRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentServiceImpl.addToGroup(5L, 99L))
                .isInstanceOf(NotFoundException.class);
        verify(studentRepository, never()).save(any());
    }
}
