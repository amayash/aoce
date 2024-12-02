package com.spring.aoce.controller;

import com.spring.aoce.dto.ActionLogDto;
import com.spring.aoce.entity.enums.UserRole;
import com.spring.aoce.service.ActionLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping(ActionLogController.ACTION_LOG_URL)
@Secured({UserRole.AsString.ADMIN})
public class ActionLogController {
    public static final String ACTION_LOG_URL = "/action-log";
    private final ActionLogService actionLogService;

    @GetMapping
    public String actionLog(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ActionLogDto> logPage = actionLogService.getAll(page, size);

        model.addAttribute("logs", logPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", logPage.getTotalPages());
        return "log";
    }
}
