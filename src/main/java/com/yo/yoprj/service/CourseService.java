package com.yo.yoprj.service;

import com.yo.yoprj.dto.course.CourseResponse;
import com.yo.yoprj.dto.course.CourseUpsertRequest;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    List<CourseResponse> findAll();

    Optional<CourseResponse> findById(Integer id);

    CourseResponse create(CourseUpsertRequest req);

    CourseResponse update(Integer id, CourseUpsertRequest req);

    void deleteById(Integer id);
}
