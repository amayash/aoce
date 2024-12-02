package com.spring.aoce.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.spring.aoce.entity.Request;
import com.spring.aoce.entity.RequestComputer;
import com.spring.aoce.entity.enums.EquipmentStatus;
import com.spring.aoce.entity.enums.EquipmentType;
import com.spring.aoce.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequestComputerDto extends RequestDto {
    private String name;

    private String serialNumber;

    private String inventoryNumber;

    private String manufacturer;

    private String model;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private String purchaseDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private String warrantyExpiration;

    private EquipmentStatus equipmentStatus;

    private EquipmentType equipmentType;


    private String cpu;

    private Integer ram;

    private Integer storage;

    private String gpu;

    public RequestComputerDto(Request request) {
        super(request);
        if (request instanceof RequestComputer computer) {
            this.name = computer.getName();
            this.serialNumber = computer.getSerialNumber();
            this.inventoryNumber = computer.getInventoryNumber();
            this.manufacturer = computer.getManufacturer();
            this.model = computer.getModel();
            this.purchaseDate = DateUtils.formatLocalDate(computer.getPurchaseDate());
            this.warrantyExpiration = DateUtils.formatLocalDate(computer.getWarrantyExpiration());
            this.equipmentStatus = computer.getEquipmentStatus();
            this.equipmentType = computer.getEquipmentType();
            this.cpu = computer.getCpu();
            this.ram = computer.getRam();
            this.storage = computer.getStorage();
            this.gpu = computer.getGpu();
        }
    }
}
