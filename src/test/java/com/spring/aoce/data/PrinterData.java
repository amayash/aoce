package com.spring.aoce.data;

import com.spring.aoce.dto.PrinterDto;
import com.spring.aoce.entity.enums.EquipmentStatus;
import com.spring.aoce.entity.enums.EquipmentType;
import com.spring.aoce.entity.enums.PaperSize;
import com.spring.aoce.entity.enums.PrinterType;

public class PrinterData {
    public static PrinterDto createPrinterDto() {
        PrinterDto printerDto = new PrinterDto();
        printerDto.setName("Test Printer");
        printerDto.setSerialNumber("SN12345");
        printerDto.setInventoryNumber("INV-67890");
        printerDto.setManufacturer("Test Manufacturer");
        printerDto.setModel("Test Model");
        printerDto.setStatus(EquipmentStatus.IN_USE);
        printerDto.setEquipmentType(EquipmentType.PRINTER);
        printerDto.setPurchaseDate("2023-01-01");
        printerDto.setWarrantyExpiration("2025-01-01");

        printerDto.setPrinterType(PrinterType.LASER);
        printerDto.setIsColor(true);
        printerDto.setPaperSize(PaperSize.A4);
        return printerDto;
    }
}
