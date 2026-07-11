package com.yo.yoprj.service.impl;

import com.yo.yoprj.common.exception.BadRequestException;
import com.yo.yoprj.common.exception.NotFoundException;
import com.yo.yoprj.domain.entity.Payment;
import com.yo.yoprj.domain.entity.Promotion;
import com.yo.yoprj.domain.entity.TuitionInvoice;
import com.yo.yoprj.domain.entity.User;
import com.yo.yoprj.domain.enums.DiscountType;
import com.yo.yoprj.domain.enums.InvoiceStatus;
import com.yo.yoprj.dto.billing.InvoiceCreateRequest;
import com.yo.yoprj.dto.billing.InvoiceResponse;
import com.yo.yoprj.dto.billing.PaymentCreateRequest;
import com.yo.yoprj.dto.billing.PaymentResponse;
import com.yo.yoprj.repository.PaymentRepository;
import com.yo.yoprj.repository.PromotionRepository;
import com.yo.yoprj.repository.TuitionInvoiceRepository;
import com.yo.yoprj.service.AuthService;
import com.yo.yoprj.service.BillingService;
import com.yo.yoprj.service.CourseClassService;
import com.yo.yoprj.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillingServiceImpl implements BillingService {
    private final TuitionInvoiceRepository tuitionInvoiceRepository;
    private final PromotionRepository promotionRepository;
    private final StudentService studentService;
    private final CourseClassService courseClassService;
    private final AuthService authService;
    private final PaymentRepository  paymentRepository;
    private final ModelMapper mapper;

    @Transactional
    public InvoiceResponse createInvoice(InvoiceCreateRequest request) {
        TuitionInvoice invoice = new TuitionInvoice();
        invoice.setInvoiceCode(request.getInvoiceCode());
        invoice.setStudent(studentService.getStudent(request.getStudentId()));
        invoice.setCourseClass(courseClassService.getCourseClass(request.getCourseClassId()));
        invoice.setBillingMonth(request.getBillingMonth());

        BigDecimal originalAmount = request.getOriginalAmount() != null
                ? request.getOriginalAmount()
                : invoice.getCourseClass().getTuitionFee();
        invoice.setOriginalAmount(originalAmount);

        Promotion promotion = null;
        BigDecimal discountAmount = BigDecimal.ZERO;
        if (request.getPromotionId() != null) {
            promotion = promotionRepository.findById(request.getPromotionId())
                    .orElseThrow(() -> new NotFoundException("Promotion not found: " + request.getPromotionId()));
            discountAmount = calculateDiscount(originalAmount, promotion);
        }

        BigDecimal finalAmount = originalAmount.subtract(discountAmount).max(BigDecimal.ZERO);
        invoice.setPromotion(promotion);
        invoice.setDiscountAmount(discountAmount);
        invoice.setFinalAmount(finalAmount);
        invoice.setAmountPaid(BigDecimal.ZERO);
        invoice.setBalanceAmount(finalAmount);
        invoice.setStatus(finalAmount.compareTo(BigDecimal.ZERO) == 0 ? InvoiceStatus.PAID : InvoiceStatus.UNPAID);
        invoice.setDueDate(request.getDueDate());
        invoice.setNote(request.getNote());
        TuitionInvoice inv = tuitionInvoiceRepository.save(invoice);
        return mapper.map(inv, InvoiceResponse.class);
    }

    @Transactional(readOnly = true)
    public List<InvoiceResponse> findInvoicesByStudent(Integer studentId, String username) throws BadRequestException {
        User user = authService.findActiveUserByUsername(username);
        if (user.getRole().name().equals("PARENT")) {
            studentService.getStudentForParent(studentId, user.getParent().getId());
        }
        return tuitionInvoiceRepository.findByStudentId(studentId).stream().map(t->mapper.map(t, InvoiceResponse.class)).toList();
    }

    private BigDecimal calculateDiscount(BigDecimal originalAmount, Promotion promotion) {
        if (promotion.getDiscountType() == DiscountType.PERCENT) {
            return originalAmount.multiply(promotion.getDiscountValue()).divide(BigDecimal.valueOf(100));
        }
        return promotion.getDiscountValue();
    }

    private InvoiceStatus calculateInvoiceStatus(BigDecimal balance, BigDecimal amountPaid) {
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            return InvoiceStatus.OVERPAID;
        }
        if (balance.compareTo(BigDecimal.ZERO) == 0) {
            return InvoiceStatus.PAID;
        }
        if (amountPaid.compareTo(BigDecimal.ZERO) > 0) {
            return InvoiceStatus.PARTIAL;
        }
        return InvoiceStatus.UNPAID;
    }

    @Transactional
    public PaymentResponse createPayment(PaymentCreateRequest request, String username) throws BadRequestException {
        TuitionInvoice invoice = tuitionInvoiceRepository.findById(request.getInvoiceId())
                .orElseThrow(() -> new NotFoundException("Invoice not found: " + request.getInvoiceId()));
        if (request.getPaidAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Paid amount must be greater than 0");
        }

        User cashier = authService.findActiveUserByUsername(username);
        Payment payment = new Payment();
        payment.setInvoice(invoice);
        payment.setPaymentCode(request.getPaymentCode());
        payment.setPaidAmount(request.getPaidAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setPaidAt(request.getPaidAt());
        payment.setCashierUser(cashier);
        payment.setNote(request.getNote());
        Payment savedPayment = paymentRepository.save(payment);

        BigDecimal newAmountPaid = invoice.getAmountPaid().add(request.getPaidAmount());
        BigDecimal balance = invoice.getFinalAmount().subtract(newAmountPaid);
        invoice.setAmountPaid(newAmountPaid);
        invoice.setBalanceAmount(balance);
        invoice.setStatus(calculateInvoiceStatus(balance, newAmountPaid));
        tuitionInvoiceRepository.save(invoice);

        return mapper.map(savedPayment, PaymentResponse.class);
    }
}
