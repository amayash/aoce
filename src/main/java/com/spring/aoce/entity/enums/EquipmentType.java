package com.spring.aoce.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EquipmentType {
    COMPUTER("Компьютер"),
    PRINTER("Принтер");

    private final String value;
}
