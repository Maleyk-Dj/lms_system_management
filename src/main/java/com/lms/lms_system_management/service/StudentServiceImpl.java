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
        Student student = studentMapper.toEntity(newStudentRequest);
        return studentMapper.toResponse(studentRepository.save(student));
    }

    @Transactional(readOnly = true)
    @Override
    public StudentResponse getById(Long id) {
        Student student=studentRepository.findById(id)
                .orElseThrow(()-> new NotFoundException ("Студента с таким id: " + id+ " не существует"));
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
        Student student = studentRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Студент с id: " + id+ " не найден"));
        studentMapper.updateStudent(request,student);
        if (request.groupId()!=null){
            Group group=groupRepository.findById(request.groupId())
                    .orElseThrow(()-> new NotFoundException("Группа с id: " + request.groupId() + " не найдена"));
            student.setGroup(group);
        }
        return studentMapper.toResponse(studentRepository.save(student));
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        Student deleted = studentRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Студента с таким id " + id + " не существует"));
        studentRepository.delete(deleted);
    }

    @Transactional
    @Override
    public StudentResponse addToGroup(Long studentId, Long groupId) {
        Student student=studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Студент с id " + studentId + " не найден"));

        Group group=groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException("Группа с id " + groupId + " не найдена"));
        if (student.getGroup()!=null&&student.getGroup().getId().equals(groupId)){
            throw new AlreadyExistsException("Студент существует в группе");
        }
        student.setGroup(group);
        return studentMapper.toResponse(studentRepository.save(student));
    }
}
