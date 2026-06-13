package com.lms.lms_system_management.service;

import com.lms.lms_system_management.dao.GroupRepository;
import com.lms.lms_system_management.dao.StudentRepository;
import com.lms.lms_system_management.dto.student.NewStudentRequest;
import com.lms.lms_system_management.dto.student.UpdateStudentRequest;
import com.lms.lms_system_management.dto.student.StudentResponse;
import com.lms.lms_system_management.exception.AlreadyExistsException;
import com.lms.lms_system_management.mapper.StudentMapper;
import com.lms.lms_system_management.model.Group;
import com.lms.lms_system_management.model.Student;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final GroupRepository groupRepository;

    @Override
    public StudentResponse create(NewStudentRequest newStudentRequest) {

        Group group = groupRepository.findByIdOrThrow(newStudentRequest.groupId());
        Student student = studentMapper.toEntity(newStudentRequest, group);
        return studentMapper.toResponse(studentRepository.save(student));
    }

    @Override
    public StudentResponse getById(Long id) {

        Student student = studentRepository.findByIdOrThrow(id);
        return studentMapper.toResponse(student);
    }

    @Transactional(readOnly = true)
    @Override
    public List<StudentResponse> getAll() {

        return studentRepository.findAll()
                .stream()
                .map(studentMapper::toResponse)
                .toList();
    }

    @Transactional
    @Override
    public StudentResponse update(UpdateStudentRequest request, Long id) {

        Student student = studentRepository.findByIdOrThrow(id);
        studentMapper.updateStudent(request, student);
        if (request.groupId() != null) {
            Group group = groupRepository.findByIdOrThrow(request.groupId());
            student.setGroup(group);
        }
        return studentMapper.toResponse(studentRepository.save(student));
    }

    @Transactional
    @Override
    public void deleteById(Long id) {

        Student deleted = studentRepository.findByIdOrThrow(id);
        studentRepository.delete(deleted);
    }

    @Transactional
    @Override
    public StudentResponse addToGroup(Long studentId, Long groupId) {

        Student student = studentRepository.findByIdOrThrow(studentId);

        Group group = groupRepository.findByIdOrThrow(groupId);

        if (student.getGroup() != null && student.getGroup().getId().equals(groupId)) {
            throw new AlreadyExistsException("Студент существует в группе");
        }
        student.setGroup(group);
        return studentMapper.toResponse(studentRepository.save(student));
    }
}
