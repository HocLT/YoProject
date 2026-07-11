package com.yo.yoprj.service;

import com.yo.yoprj.common.exception.BadRequestException;
import com.yo.yoprj.dto.billing.InvoiceCreateRequest;
import com.yo.yoprj.dto.billing.InvoiceResponse;
import com.yo.yoprj.dto.billing.PaymentCreateRequest;
import com.yo.yoprj.dto.billing.PaymentResponse;

import java.util.List;

public interface BillingService {

    InvoiceResponse createInvoice(InvoiceCreateRequest request);

    List<InvoiceResponse> findInvoicesByStudent(Integer studentId, String username) throws BadRequestException;

    PaymentResponse createPayment(PaymentCreateRequest request, String username) throws BadRequestException;
}
