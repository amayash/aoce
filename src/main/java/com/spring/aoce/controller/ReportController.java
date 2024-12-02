package com.spring.aoce.controller;

import com.spring.aoce.entity.enums.UserRole;
import com.spring.aoce.service.ReportService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(ReportController.REPORT_URL)
@Secured({UserRole.AsString.ADMIN})
public class ReportController {
    public static final String REPORT_URL = "/report";
    private final ReportService reportService;

    @GetMapping(ComputerController.COMPUTER_URL)
    public ResponseEntity<Void> computerReport(HttpServletResponse response) {
        reportService.exportComputerReport(response);
        return ResponseEntity.ok().build();
    }

    @GetMapping(PrinterController.PRINTER_URL)
    public ResponseEntity<Void> printerReport(HttpServletResponse response) {
        reportService.exportPrinterReport(response);
        return ResponseEntity.ok().build();
    }
}
