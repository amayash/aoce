package com.spring.aoce.data;

import com.spring.aoce.dto.ComputerDto;
import com.spring.aoce.entity.enums.EquipmentStatus;
import com.spring.aoce.entity.enums.EquipmentType;

public class ComputerData {
    public static ComputerDto createComputerDto() {
        ComputerDto computerDto = new ComputerDto();
        computerDto.setName("Test Computer");
        computerDto.setSerialNumber("SN12345");
        computerDto.setInventoryNumber("INV-67890");
        computerDto.setManufacturer("Test Manufacturer");
        computerDto.setModel("Test Model");
        computerDto.setStatus(EquipmentStatus.IN_USE);
        computerDto.setEquipmentType(EquipmentType.COMPUTER);
        computerDto.setPurchaseDate("2023-01-01");
        computerDto.setWarrantyExpiration("2025-01-01");

        computerDto.setCpu("Intel Core i7");
        computerDto.setRam(16);
        computerDto.setStorage(512);
        computerDto.setGpu("NVIDIA GTX 1650");
        return computerDto;
    }
}
