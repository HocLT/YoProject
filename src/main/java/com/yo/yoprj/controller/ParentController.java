package com.yo.yoprj.controller;

import com.yo.yoprj.dto.parent.ParentResponse;
import com.yo.yoprj.dto.parent.ParentUpsertRequest;
import com.yo.yoprj.service.ParentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parents")
@RequiredArgsConstructor
public class ParentController {

    private final ParentService parentService;

    @GetMapping
    public List<ParentResponse> findAll() {
        return parentService.findAll();
    }

    @GetMapping("/{id}")
    public ParentResponse findById(@PathVariable Integer id) {
        return parentService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParentResponse create(@RequestBody @Valid ParentUpsertRequest request) {
        return parentService.create(request);
    }

    @PutMapping("/{id}")
    public ParentResponse update(@PathVariable Integer id, @RequestBody @Valid ParentUpsertRequest request) {
        return parentService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Integer id) {
        parentService.deleteById(id);
    }
}
