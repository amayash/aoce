package com.spring.aoce.dto;

import com.spring.aoce.entity.enums.RequestType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ActionLogDto {
    private Long id;

    private RequestType actionType;

    private String timestamp;

    private EquipmentDto oldModel;

    private EquipmentDto newModel;

    private String user;

    private Long request;
}
