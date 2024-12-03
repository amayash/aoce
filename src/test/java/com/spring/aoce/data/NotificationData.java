package com.spring.aoce.data;

import com.spring.aoce.entity.Notification;

public class NotificationData {
    public static Notification createNotification() {
        Notification notification = new Notification();
        notification.setId(1L);
        return notification;
    }
}
