package com.yo.yoprj.controller;


import com.yo.yoprj.common.ApiResponse;
import com.yo.yoprj.dto.student.StudentResponse;
import com.yo.yoprj.dto.student.StudentUpsertRequest;
import com.yo.yoprj.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ApiResponse<List<StudentResponse>> findAll() {
        return ApiResponse.success(studentService.findAll());
    }

    @GetMapping(value = "{id}")
    public ApiResponse findById(@PathVariable int id) {
        Optional<StudentResponse> result = studentService.findById(id);
        if (result.isPresent()) {
            return ApiResponse.success(result.get());
        } else {
            return ApiResponse.error("Student["+id+"] not found");
        }
    }

    @PostMapping
    public ApiResponse<StudentResponse> create(
            @Valid
            @RequestBody
            StudentUpsertRequest req
    ) {
        return ApiResponse.success(studentService.create(req));
    }

    @PutMapping(value = "{id}")
    public ApiResponse<StudentResponse> update(@PathVariable int id, @Valid @RequestBody StudentUpsertRequest req) {
        return ApiResponse.success(studentService.update(id, req));
    }
}
