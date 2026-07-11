package com.yo.yoprj.service.impl;

import com.yo.yoprj.common.exception.NotFoundException;
import com.yo.yoprj.domain.entity.Student;
import com.yo.yoprj.dto.student.StudentResponse;
import com.yo.yoprj.dto.student.StudentUpsertRequest;
import com.yo.yoprj.repository.ParentRepository;
import com.yo.yoprj.repository.StudentRepository;
import com.yo.yoprj.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;
    private final ModelMapper modelMapper;

    private StudentResponse map(Student student) {
        return modelMapper.map(student, StudentResponse.class);
    }

    private Student toStudent(StudentUpsertRequest req) {
        Student student = new Student();
        modelMapper.map(req, student);
        
        if (req.getParentId() > 0) {
            student.setParent(parentRepository.findById(req.getParentId())
                    .orElseThrow(() -> new NotFoundException("Parent not found: " + req.getParentId())));
        }
        
        return student;
    }

    public List<StudentResponse> findAll(){
        return studentRepository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public Page<StudentResponse> findAll(Pageable pageable) {
        return studentRepository.findAll(pageable)
                .map(this::map);
    }

    public StudentResponse findById(int id) {
        return studentRepository.findById(id).map(this::map)
                .orElseThrow(() -> new NotFoundException("Student not found: " + id));
    }

    public StudentResponse create(StudentUpsertRequest req) {
        Student student = toStudent(req);
        student = studentRepository.save(student);
        return map(student);
    }

    public StudentResponse update(int id, StudentUpsertRequest req) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student not found: " + id));
        
        modelMapper.map(req, student);
        
        if (req.getParentId() > 0) {
            student.setParent(parentRepository.findById(req.getParentId())
                    .orElseThrow(() -> new NotFoundException("Parent not found: " + req.getParentId())));
        } else {
            student.setParent(null);
        }
        
        student = studentRepository.save(student);
        return map(student);
    }

    public void deleteById(int id) {
        studentRepository.deleteById(id);
    }

    public List<StudentResponse> findByStudentCode(String code) {
        return studentRepository.findByStudentCodeLike("%" + code + "%")
                .stream()
                .map(this::map)
                .toList();
    }

    @Transactional(readOnly = true)
    public Student getStudentForParent(Integer studentId, Integer parentId) {
        Student student = getStudent(studentId);
        if (student.getParent() == null || student.getParent().getId() != parentId) {
            throw new org.springframework.security.access.AccessDeniedException("Student does not belong to current parent account");
        }
        return student;
    }

    public Student getStudent(Integer id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> findByParentId(Integer parentId) {
        return studentRepository.findByParentId(parentId).stream().map(o->modelMapper.map(o, StudentResponse.class)).toList();
    }
}
