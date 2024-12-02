package com.spring.aoce.dto;

import com.spring.aoce.entity.Printer;
import com.spring.aoce.entity.enums.PaperSize;
import com.spring.aoce.entity.enums.PrinterType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PrinterDto extends EquipmentDto {
    private PrinterType printerType;

    private Boolean isColor = false;

    private PaperSize paperSize;

    public PrinterDto(Printer printer) {
        super(printer);
        this.printerType = printer.getPrinterType();
        this.isColor = printer.getIsColor();
        this.paperSize = printer.getPaperSize();
    }
}
