package com.yo.yoprj.service;

import com.yo.yoprj.dto.teacher.TeacherResponse;
import com.yo.yoprj.dto.teacher.TeacherUpsertRequest;

import java.util.List;

public interface TeacherService {
    List<TeacherResponse> findAll();
    TeacherResponse findById(Integer id);
    TeacherResponse create(TeacherUpsertRequest req);
    TeacherResponse update(Integer id, TeacherUpsertRequest req);
    void deleteById(Integer id);
}
