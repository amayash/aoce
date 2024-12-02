package com.spring.aoce.entity;

import com.spring.aoce.dto.ComputerDto;
import com.spring.aoce.dto.RequestDto;
import com.spring.aoce.entity.enums.EquipmentStatus;
import com.spring.aoce.entity.enums.EquipmentType;
import com.spring.aoce.util.DateUtils;
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
public class RequestComputer extends Request {
    @Column(nullable = false)
    private String name;

    @Column(name = "serial_number", nullable = false)
    private String serialNumber;

    @Column(name = "inventory_number", nullable = false)
    private String inventoryNumber;

    @Column(nullable = false)
    private String manufacturer;

    @Column(nullable = false)
    private String model;

    @Column(name = "purchase_date", updatable = false, nullable = false)
    private LocalDate purchaseDate;

    @Column(name = "warranty_expiration", updatable = false, nullable = false)
    private LocalDate warrantyExpiration;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EquipmentStatus equipmentStatus;

    @Column(name = "equipment_type", updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private EquipmentType equipmentType;


    @Column(nullable = false)
    private String cpu;

    @Column(nullable = false)
    private Integer ram;

    @Column(nullable = false)
    private Integer storage;

    @Column
    private String gpu;

    public RequestComputer(RequestDto dto, ComputerDto equipment) {
        super(dto);
        this.name = equipment.getName();
        this.serialNumber = equipment.getSerialNumber();
        this.inventoryNumber = equipment.getInventoryNumber();
        this.manufacturer = equipment.getManufacturer();
        this.model = equipment.getModel();
        this.purchaseDate = DateUtils.parseToLocalDate(equipment.getPurchaseDate());
        this.warrantyExpiration = DateUtils.parseToLocalDate(equipment.getWarrantyExpiration());
        this.equipmentStatus = equipment.getStatus();
        this.equipmentType = EquipmentType.COMPUTER;

        this.cpu = equipment.getCpu();
        this.ram = equipment.getRam();
        this.storage = equipment.getStorage();
        this.gpu = equipment.getGpu();
    }
}
