package com.yo.yoprj.service;

import com.yo.yoprj.common.exception.BadRequestException;
import com.yo.yoprj.dto.learning.LearningResultCreateRequest;
import com.yo.yoprj.dto.learning.LearningResultResponse;

import java.util.List;

public interface LearningResultService {

    LearningResultResponse create(LearningResultCreateRequest request, String username) throws BadRequestException;

    List<LearningResultResponse> findByStudentId(Integer studentId, String username) throws BadRequestException;
}
