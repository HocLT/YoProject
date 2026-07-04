package com.yo.yoprj.service;

import com.yo.yoprj.common.exception.BadRequestException;
import com.yo.yoprj.dto.attendance.AttendanceBulkRequest;
import com.yo.yoprj.dto.attendance.AttendanceCreateRequest;
import com.yo.yoprj.dto.attendance.AttendanceResponse;
import com.yo.yoprj.dto.attendance.AttendanceUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AttendanceService {

    AttendanceResponse create(AttendanceCreateRequest request, String username) throws BadRequestException;

    Page<AttendanceResponse> findByClassId(Integer classId, Pageable pageable);

    Page<AttendanceResponse> findByStudentId(Integer studentId, Pageable pageable);

    AttendanceResponse update(Integer id, AttendanceUpdateRequest request, String username) throws BadRequestException;

    List<AttendanceResponse> bulkSave(AttendanceBulkRequest request, String username) throws BadRequestException;
}
