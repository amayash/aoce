package com.spring.aoce.dto;

import com.spring.aoce.entity.Notification;
import com.spring.aoce.entity.RequestComputer;
import com.spring.aoce.entity.RequestPrinter;
import com.spring.aoce.entity.UserNotification;
import com.spring.aoce.entity.enums.EquipmentType;
import com.spring.aoce.service.ActionLogService;
import com.spring.aoce.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

import static com.spring.aoce.service.UserService.findByPrincipal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NotificationDto {
    private Long id;

    private String user;

    private RequestDto request;

    private String sentAt;

    private Boolean isRead = false;

    private String text;

    public NotificationDto(Notification notification) {
        this.id = notification.getId();
        this.user = notification.getSender().getEmail();
        this.sentAt = DateUtils.formatLocalDateTime(notification.getSentAt());
        // у уведомления берем пользователей, которые его читали/не читали
        // фильтруем по текущему пользователю
        // проверяем, прочитано или нет
        this.isRead = notification.getUsers().stream()
                .filter(un -> Objects.equals(un.getUser().getId(), findByPrincipal().getId()))
                .map(UserNotification::getIsRead)
                .findFirst().orElse(false);

        RequestDto requestDto = null;
        EquipmentDto equipment = null;

        if (notification.getRequest() instanceof RequestComputer requestComputer) {
            RequestComputerDto requestComputerDto = new RequestComputerDto(requestComputer);
            requestDto = requestComputerDto;
            if (requestComputer.getEquipment() != null) {
                equipment = ActionLogService.getOldEquipmentByRequestId(notification.getRequest().getId(), EquipmentType.COMPUTER, requestComputer.getEquipment().getId());
            }

            this.text = getComputerDetails(equipment, requestComputerDto);
        } else if (notification.getRequest() instanceof RequestPrinter requestPrinter) {
            RequestPrinterDto requestPrinterDto = new RequestPrinterDto(requestPrinter);
            requestDto = requestPrinterDto;
            if (requestPrinter.getEquipment() != null) {
                equipment = ActionLogService.getOldEquipmentByRequestId(notification.getRequest().getId(), EquipmentType.PRINTER, requestPrinter.getEquipment().getId());
            }

            this.text = getPrinterDetails(equipment, requestPrinterDto);
        } else {
            this.text = "Запрос неизвестного типа.";
        }

        this.request = requestDto;
    }

    private String getComputerDetails(EquipmentDto equipment, RequestComputerDto requestComputerDto) {
        if (equipment instanceof ComputerDto oldEquipment) {
            return String.format(
                    "<strong>Запрос на изменение компьютера:</strong><br><strong>Старые характеристики:</strong><br>" +
                            "Название: <strong>%s</strong><br>" +
                            "Серийный номер: <strong>%s</strong><br>" +
                            "Инвентарный номер: <strong>%s</strong><br>" +
                            "Производитель: <strong>%s</strong><br>" +
                            "Модель: <strong>%s</strong><br>" +
                            "Дата покупки: <strong>%s</strong><br>" +
                            "Гарантия до: <strong>%s</strong><br>" +
                            "Статус: <strong>%s</strong><br>" +
                            "Тип: <strong>%s</strong><br>" +
                            "Процессор: <strong>%s</strong><br>" +
                            "ОЗУ: <strong>%dGB</strong><br>" +
                            "Накопитель: <strong>%dGB</strong><br>" +
                            "Видеокарта: <strong>%s</strong><br>" +
                            "<br><strong>Новые характеристики:</strong><br>" +
                            "Название: <strong>%s</strong><br>" +
                            "Серийный номер: <strong>%s</strong><br>" +
                            "Инвентарный номер: <strong>%s</strong><br>" +
                            "Производитель: <strong>%s</strong><br>" +
                            "Модель: <strong>%s</strong><br>" +
                            "Дата покупки: <strong>%s</strong><br>" +
                            "Гарантия до: <strong>%s</strong><br>" +
                            "Статус: <strong>%s</strong><br>" +
                            "Тип: <strong>%s</strong><br>" +
                            "Процессор: <strong>%s</strong><br>" +
                            "ОЗУ: <strong>%dGB</strong><br>" +
                            "Накопитель: <strong>%dGB</strong><br>" +
                            "Видеокарта: <strong>%s</strong><br>",
                    oldEquipment.getName(), oldEquipment.getSerialNumber(), oldEquipment.getInventoryNumber(),
                    oldEquipment.getManufacturer(), oldEquipment.getModel(), oldEquipment.getPurchaseDate(),
                    oldEquipment.getWarrantyExpiration(), oldEquipment.getStatus().getValue(),
                    oldEquipment.getEquipmentType().getValue(), oldEquipment.getCpu(), oldEquipment.getRam(),
                    oldEquipment.getStorage(), oldEquipment.getGpu(),
                    requestComputerDto.getName(), requestComputerDto.getSerialNumber(),
                    requestComputerDto.getInventoryNumber(), requestComputerDto.getManufacturer(),
                    requestComputerDto.getModel(), requestComputerDto.getPurchaseDate(),
                    requestComputerDto.getWarrantyExpiration(), requestComputerDto.getEquipmentStatus().getValue(),
                    requestComputerDto.getEquipmentType().getValue(), requestComputerDto.getCpu(),
                    requestComputerDto.getRam(), requestComputerDto.getStorage(), requestComputerDto.getGpu()
            );
        } else {
            return String.format(
                    "<strong>Запрос на добавление нового компьютера:</strong><br>Название: <strong>%s</strong><br>Серийный номер: <strong>%s</strong><br>" +
                            "Инвентарный номер: <strong>%s</strong><br>Производитель: <strong>%s</strong><br>Модель: <strong>%s</strong><br>" +
                            "Дата покупки: <strong>%s</strong><br>Дата окончания гарантии: <strong>%s</strong><br>" +
                            "Статус оборудования: <strong>%s</strong><br>Тип оборудования: <strong>%s</strong><br>" +
                            "Процессор: <strong>%s</strong><br>Оперативная память: <strong>%d GB</strong><br>Накопитель: <strong>%d GB</strong><br>" +
                            "Видеокарта: <strong>%s</strong>",
                    requestComputerDto.getName(), requestComputerDto.getSerialNumber(),
                    requestComputerDto.getInventoryNumber(), requestComputerDto.getManufacturer(),
                    requestComputerDto.getModel(), requestComputerDto.getPurchaseDate(),
                    requestComputerDto.getWarrantyExpiration(), requestComputerDto.getEquipmentStatus().getValue(),
                    requestComputerDto.getEquipmentType().getValue(), requestComputerDto.getCpu(),
                    requestComputerDto.getRam(), requestComputerDto.getStorage(), requestComputerDto.getGpu()
            );
        }
    }

    private String getPrinterDetails(EquipmentDto equipment, RequestPrinterDto requestPrinterDto) {
        if (equipment instanceof PrinterDto oldEquipment) {
            return String.format(
                    "<strong>Запрос на изменение принтера:</strong><br><strong>Старые характеристики:</strong><br>" +
                            "Название: <strong>%s</strong><br>" +
                            "Серийный номер: <strong>%s</strong><br>" +
                            "Инвентарный номер: <strong>%s</strong><br>" +
                            "Производитель: <strong>%s</strong><br>" +
                            "Модель: <strong>%s</strong><br>" +
                            "Дата покупки: <strong>%s</strong><br>" +
                            "Гарантия до: <strong>%s</strong><br>" +
                            "Статус: <strong>%s</strong><br>" +
                            "Тип: <strong>%s</strong><br>" +
                            "Тип принтера: <strong>%s</strong><br>" +
                            "Цветная печать: <strong>%s</strong><br>" +
                            "Размер бумаги: <strong>%s</strong><br>" +
                            "<br><strong>Новые характеристики:</strong><br>" +
                            "Название: <strong>%s</strong><br>" +
                            "Серийный номер: <strong>%s</strong><br>" +
                            "Инвентарный номер: <strong>%s</strong><br>" +
                            "Производитель: <strong>%s</strong><br>" +
                            "Модель: <strong>%s</strong><br>" +
                            "Дата покупки: <strong>%s</strong><br>" +
                            "Гарантия до: <strong>%s</strong><br>" +
                            "Статус: <strong>%s</strong><br>" +
                            "Тип: <strong>%s</strong><br>" +
                            "Тип принтера: <strong>%s</strong><br>" +
                            "Цветная печать: <strong>%s</strong><br>" +
                            "Размер бумаги: <strong>%s</strong><br>",
                    oldEquipment.getName(), oldEquipment.getSerialNumber(), oldEquipment.getInventoryNumber(),
                    oldEquipment.getManufacturer(), oldEquipment.getModel(), oldEquipment.getPurchaseDate(),
                    oldEquipment.getWarrantyExpiration(), oldEquipment.getStatus().getValue(),
                    oldEquipment.getEquipmentType().getValue(), oldEquipment.getPrinterType().getValue(),
                    oldEquipment.getIsColor() ? "Да" : "Нет", oldEquipment.getPaperSize(),
                    requestPrinterDto.getName(), requestPrinterDto.getSerialNumber(),
                    requestPrinterDto.getInventoryNumber(), requestPrinterDto.getManufacturer(),
                    requestPrinterDto.getModel(), requestPrinterDto.getPurchaseDate(),
                    requestPrinterDto.getWarrantyExpiration(), requestPrinterDto.getEquipmentStatus().getValue(),
                    requestPrinterDto.getEquipmentType().getValue(), requestPrinterDto.getPrinterType().getValue(),
                    requestPrinterDto.getIsColor() ? "Да" : "Нет", requestPrinterDto.getPaperSize().getValue()
            );
        } else {
            return String.format(
                    "<strong>Запрос на добавление нового принтера:</strong><br>Название: <strong>%s</strong><br>Серийный номер: <strong>%s</strong><br>" +
                            "Инвентарный номер: <strong>%s</strong><br>Производитель: <strong>%s</strong><br>Модель: <strong>%s</strong><br>" +
                            "Дата покупки: <strong>%s</strong><br>Дата окончания гарантии: <strong>%s</strong><br>" +
                            "Статус оборудования: <strong>%s</strong><br>Тип оборудования: <strong>%s</strong><br>" +
                            "Тип принтера: <strong>%s</strong><br>Цветная печать: <strong>%s</strong><br>Размер бумаги: <strong>%s</strong>",
                    requestPrinterDto.getName(), requestPrinterDto.getSerialNumber(),
                    requestPrinterDto.getInventoryNumber(), requestPrinterDto.getManufacturer(),
                    requestPrinterDto.getModel(), requestPrinterDto.getPurchaseDate(),
                    requestPrinterDto.getWarrantyExpiration(), requestPrinterDto.getEquipmentStatus().getValue(),
                    requestPrinterDto.getEquipmentType().getValue(), requestPrinterDto.getPrinterType().getValue(),
                    requestPrinterDto.getIsColor() ? "Да" : "Нет", requestPrinterDto.getPaperSize().getValue()
            );
        }
    }
}
