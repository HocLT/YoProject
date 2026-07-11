package com.yo.yoprj.repository;

import com.yo.yoprj.domain.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    // invoiceId -> TuitionInvoice
    List<Payment> findByInvoiceId(Integer invoiceId);
}
