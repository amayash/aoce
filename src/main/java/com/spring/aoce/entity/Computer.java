package com.spring.aoce.entity;

import com.spring.aoce.dto.ComputerDto;
import com.spring.aoce.entity.enums.EquipmentStatus;
import com.spring.aoce.entity.enums.EquipmentType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Computer extends Equipment {
    @Column(nullable = false)
    private String cpu;

    @Column(nullable = false)
    private Integer ram;

    @Column(nullable = false)
    private Integer storage;

    @Column
    private String gpu;

    public Computer(Long id, String name, String serialNumber, String inventoryNumber, String manufacturer, String model, LocalDate purchaseDate, LocalDate warrantyExpiration, EquipmentStatus status, EquipmentType equipmentType, String cpu, Integer ram, Integer storage, String gpu) {
        super(id, name, serialNumber, inventoryNumber, manufacturer, model, purchaseDate, warrantyExpiration, status, status.getOrder(), equipmentType);
        this.cpu = cpu;
        this.ram = ram;
        this.storage = storage;
        this.gpu = gpu;
    }

    public Computer(ComputerDto dto) {
        super(dto);
        this.cpu = dto.getCpu();
        this.ram = dto.getRam();
        this.storage = dto.getStorage();
        this.gpu = dto.getGpu();
    }
}
