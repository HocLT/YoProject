package com.yo.yoprj.service;

import com.yo.yoprj.dto.student.StudentResponse;
import com.yo.yoprj.dto.student.StudentUpsertRequest;

import java.util.List;
import java.util.Optional;

public interface StudentService {

    List<StudentResponse> findAll();

    Optional<StudentResponse> findById(int id);

    StudentResponse create(StudentUpsertRequest req);

    StudentResponse update(int id, StudentUpsertRequest req);

    void deleteById(int id);

    List<StudentResponse> findByStudentCode(String code);
}
