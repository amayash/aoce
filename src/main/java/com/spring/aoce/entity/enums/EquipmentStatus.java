package com.spring.aoce.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EquipmentStatus {
    IN_USE("В использовании", 1),
    IN_REPAIR("В ремонте", 2),
    DECOMMISSIONED("Списана", 3);

    private final String value;
    private final int order;
}


