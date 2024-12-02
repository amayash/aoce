package com.spring.aoce.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RequestType {
    INSERT("Добавление"),
    UPDATE("Изменение"),
    DELETE("Списание");

    private final String value;
}

