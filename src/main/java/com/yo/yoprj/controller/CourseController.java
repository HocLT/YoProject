package com.yo.yoprj.controller;

import com.yo.yoprj.common.ApiResponse;
import com.yo.yoprj.dto.course.CourseResponse;
import com.yo.yoprj.dto.course.CourseUpsertRequest;
import com.yo.yoprj.repository.CourseRepository;
import com.yo.yoprj.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @GetMapping
    public ApiResponse<List<CourseResponse>> findAll() {
        return ApiResponse.success(courseService.findAll());
    }

    @GetMapping(value = "{id}")
    public ApiResponse findById(@PathVariable Integer id) {
        Optional<CourseResponse> courseResponse = courseService.findById(id);
        if (courseResponse.isPresent()) {
            return ApiResponse.success(courseResponse.get());
        } else {
            return ApiResponse.error("Not found.");
        }
    }

    @PostMapping
    public ApiResponse create(@Valid @RequestBody CourseUpsertRequest req) {
        return ApiResponse.success(courseService.create(req));
    }

    @PutMapping(value = "{id}")
    public ApiResponse update(@Valid @RequestBody @PathVariable Integer id, CourseUpsertRequest req) {
        return ApiResponse.success(courseService.update(id, req));
    }

    @DeleteMapping(value = "{id}")
    public void delete(@PathVariable Integer id) {
        courseService.deleteById(id);
    }
}
