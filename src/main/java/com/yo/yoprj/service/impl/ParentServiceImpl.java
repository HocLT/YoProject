package com.yo.yoprj.service.impl;

import com.yo.yoprj.common.exception.NotFoundException;
import com.yo.yoprj.domain.entity.Parent;
import com.yo.yoprj.dto.parent.ParentResponse;
import com.yo.yoprj.dto.parent.ParentUpsertRequest;
import com.yo.yoprj.repository.ParentRepository;
import com.yo.yoprj.service.ParentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParentServiceImpl implements ParentService {

    private final ParentRepository parentRepository;
    private final ModelMapper modelMapper;

    private ParentResponse map(Parent parent) {
        return modelMapper.map(parent, ParentResponse.class);
    }

    private Parent toParent(ParentUpsertRequest req) {
        Parent parent = new Parent();
        modelMapper.map(req, parent);
        return parent;
    }

    @Override
    public List<ParentResponse> findAll() {
        return parentRepository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public ParentResponse findById(Integer id) {
        return parentRepository.findById(id)
                .map(this::map)
                .orElseThrow(() -> new NotFoundException("Parent not found: " + id));
    }

    @Override
    public ParentResponse create(ParentUpsertRequest req) {
        Parent parent = toParent(req);
        parent = parentRepository.save(parent);
        return map(parent);
    }

    @Override
    public ParentResponse update(Integer id, ParentUpsertRequest req) {
        Parent parent = parentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Parent not found: " + id));
        modelMapper.map(req, parent);
        parent = parentRepository.save(parent);
        return map(parent);
    }

    @Override
    public void deleteById(Integer id) {
        parentRepository.deleteById(id);
    }
}
