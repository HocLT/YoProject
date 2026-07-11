package com.yo.yoprj.event;

import com.yo.yoprj.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventListener {

    private final EmailService emailService;

    @Async
    @EventListener
    public void handlePaymentCompletedEvent(PaymentCompletedEvent event) {
        log.info("Received PaymentCompletedEvent for payment code: {}", event.payment().getPaymentCode());
        emailService.sendPaymentNotification(event.payment());
    }
}
