package com.yo.yoprj.repository;

import com.yo.yoprj.domain.entity.Notification;
import com.yo.yoprj.domain.enums.NotificationRecipientType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    java.util.List<Notification> findByRecipientTypeAndRecipientRefIdOrderByCreatedAtDesc(NotificationRecipientType recipientType, Integer recipientRefId);
}
