package com.yo.yoprj.event;

import com.yo.yoprj.domain.entity.Payment;

public record PaymentCompletedEvent(Payment payment) {
}
