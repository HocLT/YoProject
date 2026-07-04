package com.yo.yoprj.controller;

import com.yo.yoprj.common.ApiResponse;
import com.yo.yoprj.common.exception.BadRequestException;
import com.yo.yoprj.dto.enrollment.EnrollmentCreateRequest;
import com.yo.yoprj.dto.enrollment.EnrollmentResponse;
import com.yo.yoprj.dto.enrollment.EnrollmentUpdateRequest;
import com.yo.yoprj.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping
    public ApiResponse<EnrollmentResponse> create(@Valid @RequestBody EnrollmentCreateRequest request) throws BadRequestException {
        return ApiResponse.success("Enrollment created", enrollmentService.create(request));
    }

    @GetMapping("/class/{classId}")
    public ApiResponse<Page<EnrollmentResponse>> findByClassId(@PathVariable Integer classId, Pageable pageable) {
        return ApiResponse.success(enrollmentService.findByClassId(classId, pageable));
    }

    @PutMapping("/{id}")
    public ApiResponse<EnrollmentResponse> update(@PathVariable Integer id, @Valid @RequestBody EnrollmentUpdateRequest request) {
        return ApiResponse.success("Enrollment updated", enrollmentService.update(id, request));
    }

    @GetMapping("/student/{studentId}")
    public ApiResponse<Page<EnrollmentResponse>> findByStudentId(@PathVariable Integer studentId, Pageable pageable) {
        return ApiResponse.success(enrollmentService.findByStudentId(studentId, pageable));
    }
}
