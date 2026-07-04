package com.yo.yoprj.controller;

import com.yo.yoprj.common.ApiResponse;
import com.yo.yoprj.common.exception.BadRequestException;
import com.yo.yoprj.dto.attendance.AttendanceBulkRequest;
import com.yo.yoprj.dto.attendance.AttendanceCreateRequest;
import com.yo.yoprj.dto.attendance.AttendanceResponse;
import com.yo.yoprj.dto.attendance.AttendanceUpdateRequest;
import com.yo.yoprj.service.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/attendances")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping
    public ApiResponse<AttendanceResponse> create(@Valid @RequestBody AttendanceCreateRequest request, Principal principal) throws BadRequestException {
        return ApiResponse.success("Attendance created", attendanceService.create(request, principal.getName()));
    }

    @GetMapping("/class/{classId}")
    public ApiResponse<Page<AttendanceResponse>> findByClassId(@PathVariable Integer classId, Pageable pageable) {
        return ApiResponse.success(attendanceService.findByClassId(classId, pageable));
    }

    @PutMapping("/{id}")
    public ApiResponse<AttendanceResponse> update(@PathVariable Integer id, @Valid @RequestBody AttendanceUpdateRequest request, Principal principal) throws BadRequestException {
        return ApiResponse.success("Attendance updated", attendanceService.update(id, request, principal.getName()));
    }

    @GetMapping("/student/{studentId}")
    public ApiResponse<Page<AttendanceResponse>> findByStudentId(@PathVariable Integer studentId, Pageable pageable) {
        return ApiResponse.success(attendanceService.findByStudentId(studentId, pageable));
    }

    @PostMapping("/bulk")
    public ApiResponse<List<AttendanceResponse>> bulkSave(@Valid @RequestBody AttendanceBulkRequest request, Principal principal) throws BadRequestException {
        return ApiResponse.success("Attendances saved successfully", attendanceService.bulkSave(request, principal.getName()));
    }

}
