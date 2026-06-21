package com.yo.yoprj.controller;

import com.yo.yoprj.common.ApiResponse;
import com.yo.yoprj.common.exception.NotFoundException;
import com.yo.yoprj.dto.courseclass.CourseClassCreateRequest;
import com.yo.yoprj.dto.courseclass.CourseClassResponse;
import com.yo.yoprj.service.CourseClassService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/course-class")
@RequiredArgsConstructor
public class CourseClassController {
    private final CourseClassService courseClassService;

    @GetMapping
    public ApiResponse<Page<CourseClassResponse>> findAll(Pageable pageable) {
        return ApiResponse.success(courseClassService.findAll(pageable));
    }

    @PostMapping
    public ApiResponse<CourseClassResponse> create(@Valid @RequestBody CourseClassCreateRequest request) throws NotFoundException {
        return ApiResponse.success("Class created", courseClassService.create(request));
    }
}