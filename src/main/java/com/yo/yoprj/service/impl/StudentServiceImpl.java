package com.yo.yoprj.service.impl;

import com.yo.yoprj.domain.entity.Student;
import com.yo.yoprj.domain.enums.Gender;
import com.yo.yoprj.domain.enums.Status;
import com.yo.yoprj.dto.student.StudentResponse;
import com.yo.yoprj.dto.student.StudentUpsertRequest;
import com.yo.yoprj.repository.StudentRepository;
import com.yo.yoprj.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    private StudentResponse map(Student student) {
        StudentResponse studentResponse = new StudentResponse();
        studentResponse.setId(student.getId());
        studentResponse.setStudentCode(student.getStudentCode());
        studentResponse.setFullName(student.getFullName());
        studentResponse.setDateOfBirth(student.getDateOfBirth());
        studentResponse.setGender(student.getGender());
        studentResponse.setGradeLevel(student.getGradeLevel());
        studentResponse.setSchoolName(student.getSchoolName());
        studentResponse.setPhone(student.getPhone());
        studentResponse.setParentId(student.getParentId());
        studentResponse.setStatus(student.getStatus());
        studentResponse.setNote(student.getNote());
        studentResponse.setCreatedAt(student.getCreatedAt());
        studentResponse.setUpdatedAt(student.getUpdatedAt());

        return studentResponse;
    }

    private Student toStudent(StudentUpsertRequest req) {
        Student student = new Student();
        student.setStudentCode(req.getStudentCode());
        student.setFullName(req.getFullName());
        student.setDateOfBirth(req.getDateOfBirth());
        student.setGender(req.getGender());
        student.setGradeLevel(req.getGradeLevel());
        student.setSchoolName(req.getSchoolName());
        student.setPhone(req.getPhone());
        student.setParentId(req.getParentId());
        student.setStatus(req.getStatus());
        student.setNote(req.getNote());

        return student;
    }

    public List<StudentResponse> findAll(){
        return studentRepository.findAll()
                .stream()
                .map(this::map)
                .toList();

//        List<StudentResponse> result = new ArrayList<>();
//        for (Student student : studentRepository.findAll()) {
//            StudentResponse studentResponse = map(student);
//            result.add(studentResponse);
//        }
//        return result;
    }

    public Optional<StudentResponse> findById(int id){
        return studentRepository.findById(id).map(this::map);
    }

    public StudentResponse create(StudentUpsertRequest req) {
        Student student = toStudent(req);
        studentRepository.save(student);
        return map(student);
    }

    public StudentResponse update(int id, StudentUpsertRequest req) {
        Student student = toStudent(req);
        student.setId(id);
        studentRepository.save(student);
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
}
