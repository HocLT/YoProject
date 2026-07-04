package com.yo.yoprj.service;

import com.yo.yoprj.common.exception.BadRequestException;
import com.yo.yoprj.domain.entity.Enrollment;
import com.yo.yoprj.dto.enrollment.EnrollmentCreateRequest;
import com.yo.yoprj.dto.enrollment.EnrollmentResponse;
import com.yo.yoprj.dto.enrollment.EnrollmentUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EnrollmentService {
    EnrollmentResponse create(EnrollmentCreateRequest request) throws BadRequestException;

    Page<EnrollmentResponse> findByClassId(Integer classId, Pageable pageable);

    Enrollment getEnrollment(Integer studentId, Integer classId) throws BadRequestException;

    EnrollmentResponse update(Integer id, EnrollmentUpdateRequest request);

    Page<EnrollmentResponse> findByStudentId(Integer studentId, Pageable pageable);
}
