package com.spring.aoce.repository;

import com.spring.aoce.entity.UserNotification;
import com.spring.aoce.entity.UserNotificationKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, UserNotificationKey> {
    UserNotification findByUserIdAndNotificationRequestId(Long userId, Long notificationRequestId);
}
