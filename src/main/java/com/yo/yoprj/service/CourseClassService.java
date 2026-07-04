package com.yo.yoprj.service;

import com.yo.yoprj.common.exception.NotFoundException;
import com.yo.yoprj.domain.entity.CourseClass;
import com.yo.yoprj.dto.courseclass.CourseClassCreateRequest;
import com.yo.yoprj.dto.courseclass.CourseClassResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseClassService {

    Page<CourseClassResponse> findAll(Pageable pageable);

    CourseClassResponse create(CourseClassCreateRequest request) throws NotFoundException;

    CourseClass getCourseClass(Integer id) throws NotFoundException;

    CourseClass getCourseClassForLock(Integer id);
}
