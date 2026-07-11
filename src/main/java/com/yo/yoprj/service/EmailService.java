package com.yo.yoprj.service;

import com.yo.yoprj.domain.entity.Payment;

public interface EmailService {
    void sendPaymentNotification(Payment payment);
}
