package com.spring.aoce.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PaperSize {
    A4("A4 (Стандартный)"),
    A3("A3 (Большой)"),
    A5("A5 (Маленький)"),
    LETTER("Letter (Письмо)"),
    LEGAL("Legal (Юридический)");

    private final String value;
}

