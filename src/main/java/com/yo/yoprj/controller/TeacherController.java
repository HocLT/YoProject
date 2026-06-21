package com.yo.yoprj.controller;

import com.yo.yoprj.dto.teacher.TeacherResponse;
import com.yo.yoprj.dto.teacher.TeacherUpsertRequest;
import com.yo.yoprj.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping
    public List<TeacherResponse> findAll() {
        return teacherService.findAll();
    }

    @GetMapping("/{id}")
    public TeacherResponse findById(@PathVariable Integer id) {
        return teacherService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TeacherResponse create(@RequestBody @Valid TeacherUpsertRequest request) {
        return teacherService.create(request);
    }

    @PutMapping("/{id}")
    public TeacherResponse update(@PathVariable Integer id, @RequestBody @Valid TeacherUpsertRequest request) {
        return teacherService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Integer id) {
        teacherService.deleteById(id);
    }
}
