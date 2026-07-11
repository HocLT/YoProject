package com.yo.yoprj.service;

import com.yo.yoprj.common.exception.BadRequestException;
import com.yo.yoprj.dto.billing.InvoiceCreateRequest;
import com.yo.yoprj.dto.billing.InvoiceResponse;

import java.util.List;

public interface BillingService {

    InvoiceResponse createInvoice(InvoiceCreateRequest request);

    List<InvoiceResponse> findInvoicesByStudent(Integer studentId, String username) throws BadRequestException;
}
