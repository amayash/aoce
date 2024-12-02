package com.spring.aoce.dto;

import com.spring.aoce.entity.Request;
import com.spring.aoce.entity.RequestPrinter;
import com.spring.aoce.entity.enums.EquipmentStatus;
import com.spring.aoce.entity.enums.EquipmentType;
import com.spring.aoce.entity.enums.PaperSize;
import com.spring.aoce.entity.enums.PrinterType;
import com.spring.aoce.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequestPrinterDto extends RequestDto {
    private String name;

    private String serialNumber;

    private String inventoryNumber;

    private String manufacturer;

    private String model;

    private String purchaseDate;

    private String warrantyExpiration;

    private EquipmentStatus equipmentStatus;

    private EquipmentType equipmentType;


    private PrinterType printerType;

    private Boolean isColor = false;

    private PaperSize paperSize;

    public RequestPrinterDto(Request request) {
        super(request);
        if (request instanceof RequestPrinter printer) {
            this.name = printer.getName();
            this.serialNumber = printer.getSerialNumber();
            this.inventoryNumber = printer.getInventoryNumber();
            this.manufacturer = printer.getManufacturer();
            this.model = printer.getModel();
            this.purchaseDate = DateUtils.formatLocalDate(printer.getPurchaseDate());
            this.warrantyExpiration = DateUtils.formatLocalDate(printer.getWarrantyExpiration());
            this.equipmentStatus = printer.getEquipmentStatus();
            this.equipmentType = printer.getEquipmentType();

            this.printerType = printer.getPrinterType();
            this.isColor = printer.getIsColor();
            this.paperSize = printer.getPaperSize();
        }
    }
}
