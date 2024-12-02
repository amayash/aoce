package com.spring.aoce.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RequestStatus {
    PENDING_REVIEW("Ожидает рассмотрения"),
    APPROVED("Одобрена"),
    REJECTED("Отклонена");

    private final String value;
}

