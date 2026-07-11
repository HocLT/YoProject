package com.yo.yoprj.service;

import com.yo.yoprj.common.exception.NotFoundException;
import com.yo.yoprj.domain.entity.Student;
import com.yo.yoprj.dto.student.StudentResponse;
import com.yo.yoprj.dto.student.StudentUpsertRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface StudentService {

    List<StudentResponse> findAll();

    Page<StudentResponse> findAll(Pageable pageable);

    StudentResponse findById(int id);

    StudentResponse create(StudentUpsertRequest req);

    StudentResponse update(int id, StudentUpsertRequest req);

    void deleteById(int id);

    List<StudentResponse> findByStudentCode(String code);

    Student getStudentForParent(Integer studentId, Integer parentId) throws NotFoundException;

    Student getStudent(Integer id);

    List<StudentResponse> findByParentId(Integer parentId);
}
