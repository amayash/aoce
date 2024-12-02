package com.spring.aoce.controller;

import com.spring.aoce.dto.ComputerDto;
import com.spring.aoce.entity.enums.UserRole;
import com.spring.aoce.service.ComputerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping(ComputerController.COMPUTER_URL)
public class ComputerController {
    public static final String COMPUTER_URL = "/computer";
    private final ComputerService computerService;

    @GetMapping
    public String getAll(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<ComputerDto> computerPage = computerService.getAll(page, size);

        model.addAttribute("equipments", computerPage.getContent());
        model.addAttribute("currentType", "COMPUTER");
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", computerPage.getTotalPages());
        return "equipment";
    }


    @GetMapping("/create")
    @Secured({UserRole.AsString.ADMIN})
    public String create(Model model) {
        model.addAttribute("equipment", new ComputerDto());
        model.addAttribute("actionUrl", COMPUTER_URL);
        return "equipment-edit";
    }

    @PostMapping
    @Secured({UserRole.AsString.ADMIN})
    public String create(Model model, ComputerDto computer) {
        model.addAttribute("actionUrl", COMPUTER_URL);
        computerService.create(computer, null);
        return "redirect:/computer";
    }

    @GetMapping("/edit/{id}")
    @Secured({UserRole.AsString.ADMIN})
    public String edit(Model model, @PathVariable Long id) {
        ComputerDto computer = computerService.get(id);
        model.addAttribute("equipment", computer);
        model.addAttribute("actionUrl", COMPUTER_URL + "/edit/" + id);
        return "equipment-edit";
    }

    @PostMapping("/edit/{id}")
    @Secured({UserRole.AsString.ADMIN})
    public String edit(Model model, ComputerDto computer, @PathVariable Long id) {
        computerService.edit(computer, id, null);
        return "redirect:/computer";
    }

    @PostMapping("/delete/{id}")
    @Secured({UserRole.AsString.ADMIN})
    public String delete(Model model, @PathVariable Long id) {
        computerService.delete(id, null);
        return "redirect:/computer";
    }
}
