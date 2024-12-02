package com.spring.aoce.controller;

import com.spring.aoce.dto.PrinterDto;
import com.spring.aoce.entity.enums.UserRole;
import com.spring.aoce.service.PrinterService;
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
@RequestMapping(PrinterController.PRINTER_URL)
public class PrinterController {
    public static final String PRINTER_URL = "/printer";
    private final PrinterService printerService;

    @GetMapping
    public String getAll(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<PrinterDto> printerPage = printerService.getAll(page, size);

        model.addAttribute("equipments", printerPage.getContent());
        model.addAttribute("currentType", "PRINTER");
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", printerPage.getTotalPages());
        return "equipment";
    }

    @GetMapping("/create")
    @Secured({UserRole.AsString.ADMIN})
    public String create(Model model) {
        model.addAttribute("equipment", new PrinterDto());
        model.addAttribute("actionUrl", PRINTER_URL);
        return "equipment-edit";
    }

    @PostMapping
    @Secured({UserRole.AsString.ADMIN})
    public String create(Model model, PrinterDto printer) {
        model.addAttribute("actionUrl", PRINTER_URL);
        printerService.create(printer, null);
        return "redirect:" + PRINTER_URL;
    }

    @GetMapping("/edit/{id}")
    @Secured({UserRole.AsString.ADMIN})
    public String edit(Model model, @PathVariable Long id) {
        PrinterDto printer = printerService.get(id);
        model.addAttribute("equipment", printer);
        model.addAttribute("actionUrl", PRINTER_URL + "/edit/" + id);
        return "equipment-edit";
    }

    @PostMapping("/edit/{id}")
    @Secured({UserRole.AsString.ADMIN})
    public String edit(Model model, PrinterDto printer, @PathVariable Long id) {
        printerService.edit(printer, id, null);
        return "redirect:" + PRINTER_URL;
    }

    @PostMapping("/delete/{id}")
    @Secured({UserRole.AsString.ADMIN})
    public String delete(Model model, @PathVariable Long id) {
        printerService.delete(id, null);
        return "redirect:" + PRINTER_URL;
    }
}
