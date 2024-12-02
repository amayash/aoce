package com.spring.aoce.entity;

import com.spring.aoce.dto.EquipmentDto;
import com.spring.aoce.entity.enums.EquipmentStatus;
import com.spring.aoce.entity.enums.EquipmentType;
import com.spring.aoce.util.DateUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@FieldNameConstants
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "serial_number", nullable = false)
    private String serialNumber;

    @Column(name = "inventory_number", nullable = false, unique = true)
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

    @Column(name = "equipment_status_order", nullable = false)
    private Integer equipmentStatusOrder;

    @Column(name = "equipment_type", updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private EquipmentType equipmentType;

    public Equipment(EquipmentDto dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.serialNumber = dto.getSerialNumber();
        this.inventoryNumber = dto.getInventoryNumber();
        this.manufacturer = dto.getManufacturer();
        this.model = dto.getModel();
        this.purchaseDate = DateUtils.parseToLocalDate(dto.getPurchaseDate());
        this.warrantyExpiration = DateUtils.parseToLocalDate(dto.getWarrantyExpiration());
        this.equipmentStatus = dto.getStatus();
        this.equipmentType = dto.getEquipmentType();
        this.equipmentStatusOrder = dto.getStatus().getOrder();
    }
}
