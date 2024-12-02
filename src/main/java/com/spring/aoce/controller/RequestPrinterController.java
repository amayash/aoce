package com.spring.aoce.controller;

import com.spring.aoce.dto.PrinterDto;
import com.spring.aoce.dto.RequestDto;
import com.spring.aoce.entity.enums.EquipmentType;
import com.spring.aoce.entity.enums.UserRole;
import com.spring.aoce.service.PrinterService;
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

import static com.spring.aoce.controller.PrinterController.PRINTER_URL;

@Controller
@RequiredArgsConstructor
@RequestMapping(RequestPrinterController.PRINTER_REQUEST_URL)
@Secured({UserRole.AsString.USER})
public class RequestPrinterController {
    public static final String PRINTER_REQUEST_URL = PRINTER_URL + "/request";
    private final RequestService requestService;
    private final PrinterService printerService;

    @GetMapping
    public String getAll(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<RequestDto> requestPage = requestService.getAll(EquipmentType.PRINTER, page, size);

        model.addAttribute("requests", requestPage.getContent());
        model.addAttribute("currentType", "PRINTER");
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", requestPage.getTotalPages());
        return "requests";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("actionUrl", PRINTER_REQUEST_URL + "/create");
        model.addAttribute("equipment", new PrinterDto());
        return "request-edit";
    }

    @PostMapping("/create")
    public String create(Model model, PrinterDto equipment) {
        model.addAttribute("actionUrl", PRINTER_REQUEST_URL + "/create");
        requestService.create(equipment);
        return "redirect:" + PRINTER_REQUEST_URL;
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable Long id) {
        PrinterDto printer = printerService.get(id);
        model.addAttribute("equipment", printer);
        model.addAttribute("actionUrl", PRINTER_REQUEST_URL + "/edit/" + id);
        return "request-edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(Model model, PrinterDto equipment, @PathVariable Long id) {
        requestService.edit(equipment, id);
        return "redirect:" + PRINTER_REQUEST_URL;
    }

    @PostMapping("/delete/{id}")
    public String delete(Model model, @PathVariable Long id) {
        requestService.deletePrinter(id);
        return "redirect:" + PRINTER_REQUEST_URL;
    }
}
