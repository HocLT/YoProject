package com.yo.yoprj.service;

import com.yo.yoprj.dto.parent.ParentResponse;
import com.yo.yoprj.dto.parent.ParentUpsertRequest;

import java.util.List;

public interface ParentService {
    List<ParentResponse> findAll();
    ParentResponse findById(Integer id);
    ParentResponse create(ParentUpsertRequest req);
    ParentResponse update(Integer id, ParentUpsertRequest req);
    void deleteById(Integer id);
}
