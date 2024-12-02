package com.spring.aoce.entity;

import com.spring.aoce.dto.PrinterDto;
import com.spring.aoce.entity.enums.EquipmentStatus;
import com.spring.aoce.entity.enums.EquipmentType;
import com.spring.aoce.entity.enums.PaperSize;
import com.spring.aoce.entity.enums.PrinterType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Printer extends Equipment {
    @Enumerated(EnumType.STRING)
    @Column(name = "printer_type", nullable = false)
    private PrinterType printerType;

    @Column(name = "is_color", nullable = false)
    private Boolean isColor = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "paper_size", nullable = false)
    private PaperSize paperSize;

    public Printer(Long id, String name, String serialNumber, String inventoryNumber, String manufacturer, String model, LocalDate purchaseDate, LocalDate warrantyExpiration, EquipmentStatus status, EquipmentType equipmentType, PrinterType printerType, Boolean isColor, PaperSize paperSize) {
        super(id, name, serialNumber, inventoryNumber, manufacturer, model, purchaseDate, warrantyExpiration, status, status.getOrder(), equipmentType);
        this.printerType = printerType;
        this.isColor = isColor;
        this.paperSize = paperSize;
    }

    public Printer(PrinterDto dto) {
        super(dto);
        this.printerType = dto.getPrinterType();
        this.isColor = dto.getIsColor();
        this.paperSize = dto.getPaperSize();
    }
}
