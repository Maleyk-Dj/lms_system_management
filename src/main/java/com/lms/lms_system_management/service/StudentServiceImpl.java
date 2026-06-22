package com.lms.lms_system_management.service;

import com.lms.lms_system_management.dao.GroupRepository;
import com.lms.lms_system_management.dao.StudentRepository;
import com.lms.lms_system_management.dao.specification.StudentSpecification;
import com.lms.lms_system_management.dto.student.NewStudentRequest;
import com.lms.lms_system_management.dto.student.StudentFilter;
import com.lms.lms_system_management.dto.student.UpdateStudentRequest;
import com.lms.lms_system_management.dto.student.StudentResponse;
import com.lms.lms_system_management.exception.AlreadyExistsException;
import com.lms.lms_system_management.exception.NotFoundException;
import com.lms.lms_system_management.mapper.StudentMapper;
import com.lms.lms_system_management.model.GroupEntity;
import com.lms.lms_system_management.model.StudentEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final GroupRepository groupRepository;

    @Override
    public StudentResponse create(NewStudentRequest newStudentRequest) {
        GroupEntity groupEntity = groupRepository.findByIdOrThrow(newStudentRequest.groupId());
        StudentEntity studentEntity = studentMapper.toEntity(newStudentRequest, groupEntity);
        return studentMapper.toResponse(studentRepository.save(studentEntity));
    }

    @Override
    public StudentResponse getById(Long id) {
        StudentEntity studentEntity = studentRepository.findByIdOrThrow(id);
        return studentMapper.toResponse(studentEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<StudentResponse> getAll(StudentFilter filter, Pageable pageable) {
        return studentRepository.findAll(StudentSpecification.build(filter), pageable)
                .map(studentMapper::toResponse);
    }

    @Transactional
    @Override
    public StudentResponse update(UpdateStudentRequest request, Long id) {
        StudentEntity studentEntity = studentRepository.findByIdOrThrow(id);
        studentMapper.updateStudent(request, studentEntity);
        if (request.groupId() != null) {
            GroupEntity groupEntity = groupRepository.findByIdOrThrow(request.groupId());
            studentEntity.setGroupEntity(groupEntity);
        }
        return studentMapper.toResponse(studentRepository.save(studentEntity));
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        int updated = studentRepository.softDeleteById(id);

        if (updated == 0) {
            throw new NotFoundException("Студент c id " + id + " не найден");
        }
    }

    @Transactional
    @Override
    public StudentResponse addToGroup(Long studentId, Long groupId) {
        StudentEntity studentEntity = studentRepository.findByIdOrThrow(studentId);

        GroupEntity groupEntity = groupRepository.findByIdOrThrow(groupId);

        if (studentEntity.getGroupEntity() != null && studentEntity.getGroupEntity().getId().equals(groupId)) {
            throw new AlreadyExistsException("Студент существует в группе");
        }
        studentEntity.setGroupEntity(groupEntity);
        return studentMapper.toResponse(studentRepository.save(studentEntity));
    }
}
