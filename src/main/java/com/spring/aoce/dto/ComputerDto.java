package com.spring.aoce.dto;

import com.spring.aoce.entity.Computer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ComputerDto extends EquipmentDto {
    private String cpu;

    private Integer ram;

    private Integer storage;

    private String gpu;

    public ComputerDto(Computer computer) {
        super(computer);
        this.cpu = computer.getCpu();
        this.ram = computer.getRam();
        this.storage = computer.getStorage();
        this.gpu = computer.getGpu();
    }
}
