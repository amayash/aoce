package com.spring.aoce.controller;

import com.spring.aoce.dto.RequestComputerDto;
import com.spring.aoce.entity.enums.UserRole;
import com.spring.aoce.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@RequestMapping(RequestController.REQUEST_URL)
public class RequestController {
    public static final String REQUEST_URL = "/request";
    private final RequestService requestService;

    @GetMapping("/{id}")
    public String get(Model model, @PathVariable Long id) {
        model.addAttribute("request", requestService.get(id));
        model.addAttribute("currentType", requestService.get(id) instanceof RequestComputerDto ? "COMPUTER" : "PRINTER");
        return "request";
    }

    @PostMapping("/accept/{id}")
    @Secured({UserRole.AsString.ADMIN})
    public ModelAndView accept(Model model, @PathVariable Long id) {
        requestService.accept(id);
        return new ModelAndView("redirect:/notification");
    }

    @PostMapping("/reject/{id}")
    @Secured({UserRole.AsString.ADMIN})
    public String reject(Model model, @PathVariable Long id) {
        requestService.reject(id);
        return "redirect:/notification";
    }
}
