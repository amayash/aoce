package com.spring.aoce.aspect;

import com.spring.aoce.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Aspect
@Component
@RequiredArgsConstructor
public class NotificationsCheckAspect {
    private final NotificationService notificationService;

    @Before("execution(* com.spring.aoce.controller.*.*(..)) && args(model,..) ")
    public void beforeControllerMethodExecution(Model model) {
        try {
            long hasUnreadNotifications = notificationService.getAll().stream().filter(n -> !n.getIsRead()).count();
            model.addAttribute("hasUnreadNotifications", hasUnreadNotifications > 0);
        } catch (EntityNotFoundException ignored) {
        }
    }
}
