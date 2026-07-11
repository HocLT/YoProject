package com.yo.yoprj.controller;

import com.yo.yoprj.common.ApiResponse;
import com.yo.yoprj.common.exception.BadRequestException;
import com.yo.yoprj.dto.parent.ParentDashboardResponse;
import com.yo.yoprj.service.ParentPortalService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/parent")
@RequiredArgsConstructor
public class ParentPortalController {

    private final ParentPortalService parentPortalService;

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('PARENT')")
    public ApiResponse<ParentDashboardResponse> dashboard(Principal principal) throws BadRequestException {
        return ApiResponse.success(parentPortalService.getDashboard(principal.getName()));
    }
}
