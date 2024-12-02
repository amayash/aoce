package com.spring.aoce.dto;

import com.spring.aoce.entity.Request;
import com.spring.aoce.entity.RequestComputer;
import com.spring.aoce.entity.RequestPrinter;
import com.spring.aoce.entity.enums.EquipmentType;
import com.spring.aoce.entity.enums.RequestStatus;
import com.spring.aoce.entity.enums.RequestType;
import com.spring.aoce.service.ActionLogService;
import com.spring.aoce.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequestDto {
    private Long id;

    private EquipmentDto equipment;

    private String name;

    private RequestType requestType;

    private RequestStatus status;

    private String createdAt;

    private String processedAt;

    public RequestDto(Request request) {
        this.id = request.getId();
        if (request.getEquipment() != null) {
            switch (request.getEquipment().getEquipmentType()) {
                case COMPUTER:
                    this.equipment = ActionLogService.getOldEquipmentByRequestId(request.getId(), EquipmentType.COMPUTER, request.getEquipment().getId());
                    break;
                case PRINTER:
                    this.equipment = ActionLogService.getOldEquipmentByRequestId(request.getId(), EquipmentType.PRINTER, request.getEquipment().getId());
                    break;
            }
        }
        if (request instanceof RequestComputer computer) {
            this.name = computer.getName();
        }
        if (request instanceof RequestPrinter printer) {
            this.name = printer.getName();
        }
        this.requestType = request.getRequestType();
        this.status = request.getStatus();
        this.createdAt = DateUtils.formatLocalDateTime(request.getCreatedAt());
        this.processedAt = DateUtils.formatLocalDateTime(request.getProcessedAt());
    }
}
