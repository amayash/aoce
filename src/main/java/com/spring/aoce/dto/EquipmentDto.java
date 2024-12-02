package com.spring.aoce.dto;

import com.spring.aoce.entity.Equipment;
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
public class EquipmentDto {
    private Long id;

    private String name;

    private String serialNumber;

    private String inventoryNumber;

    private String manufacturer;

    private String model;

    private String purchaseDate;

    private String warrantyExpiration;

    private EquipmentStatus status;

    private EquipmentType equipmentType;

    public EquipmentDto(Equipment equipment) {
        this.id = equipment.getId();
        this.name = equipment.getName();
        this.serialNumber = equipment.getSerialNumber();
        this.inventoryNumber = equipment.getInventoryNumber();
        this.manufacturer = equipment.getManufacturer();
        this.model = equipment.getModel();
        this.purchaseDate = DateUtils.formatLocalDate(equipment.getPurchaseDate());
        this.warrantyExpiration = DateUtils.formatLocalDate(equipment.getWarrantyExpiration());
        this.status = equipment.getEquipmentStatus();
        this.equipmentType = equipment.getEquipmentType();
    }
}
