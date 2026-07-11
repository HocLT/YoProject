package com.yo.yoprj.service;

import com.yo.yoprj.common.exception.BadRequestException;
import com.yo.yoprj.dto.parent.ParentDashboardResponse;

public interface ParentPortalService {

    ParentDashboardResponse getDashboard(String username) throws BadRequestException;

}
