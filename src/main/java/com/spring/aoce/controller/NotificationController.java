package com.spring.aoce.controller;

import com.spring.aoce.dto.NotificationDto;
import com.spring.aoce.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping(NotificationController.NOTIFICATION_URL)
public class NotificationController {
    public static final String NOTIFICATION_URL = "/notification";
    private final NotificationService notificationService;

    @GetMapping
    public String getAll(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<NotificationDto> notificationPage = notificationService.getAll(page, size);

        model.addAttribute("notifications", notificationPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", notificationPage.getTotalPages());
        return "notification";
    }


    @PostMapping("/{id}/read")
    public String markNotificationAsRead(Model model, @PathVariable Long id) {
        notificationService.markAsRead(id);
        return "redirect:/notification";
    }
}
