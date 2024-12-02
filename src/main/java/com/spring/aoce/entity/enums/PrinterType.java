package com.spring.aoce.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PrinterType {
    LASER("Лазерный"),
    INKJET("Струйный"),
    DOT_MATRIX("Матричный");

    private final String value;
}
