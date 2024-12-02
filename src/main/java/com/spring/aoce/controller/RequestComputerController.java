package com.spring.aoce.controller;

import com.spring.aoce.dto.ComputerDto;
import com.spring.aoce.dto.RequestDto;
import com.spring.aoce.entity.enums.EquipmentType;
import com.spring.aoce.entity.enums.UserRole;
import com.spring.aoce.service.ComputerService;
import com.spring.aoce.service.RequestService;
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

import static com.spring.aoce.controller.ComputerController.COMPUTER_URL;
import static com.spring.aoce.controller.RequestController.REQUEST_URL;

@Controller
@RequiredArgsConstructor
@RequestMapping(RequestComputerController.COMPUTER_REQUEST_URL)
@Secured({UserRole.AsString.USER})
public class RequestComputerController {
    public static final String COMPUTER_REQUEST_URL = COMPUTER_URL + REQUEST_URL;
    private final RequestService requestService;
    private final ComputerService computerService;

    @GetMapping("/{id}")
    public String get(Model model, @PathVariable Long id) {
        model.addAttribute("request", requestService.get(id));
        return "request";
    }

    @GetMapping
    public String getAll(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<RequestDto> requestPage = requestService.getAll(EquipmentType.COMPUTER, page, size);

        model.addAttribute("requests", requestPage.getContent());
        model.addAttribute("currentType", "COMPUTER");
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", requestPage.getTotalPages());
        return "requests";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("actionUrl", COMPUTER_REQUEST_URL + "/create");
        model.addAttribute("equipment", new ComputerDto());
        return "request-edit";
    }

    @PostMapping("/create")
    public String create(Model model, ComputerDto equipment) {
        model.addAttribute("actionUrl", COMPUTER_REQUEST_URL + "/create");
        requestService.create(equipment);
        return "redirect:" + COMPUTER_REQUEST_URL;
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable Long id) {
        ComputerDto computer = computerService.get(id);
        model.addAttribute("equipment", computer);
        model.addAttribute("actionUrl", COMPUTER_REQUEST_URL + "/edit/" + id);
        return "request-edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(Model model, ComputerDto equipment, @PathVariable Long id) {
        requestService.edit(equipment, id);
        return "redirect:" + COMPUTER_REQUEST_URL;
    }

    @PostMapping("/delete/{id}")
    public String delete(Model model, @PathVariable Long id) {
        requestService.deleteComputer(id);
        return "redirect:" + COMPUTER_REQUEST_URL;
    }
}
