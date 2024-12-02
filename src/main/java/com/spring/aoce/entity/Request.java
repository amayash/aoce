package com.spring.aoce.entity;

import com.spring.aoce.dto.EquipmentDto;
import com.spring.aoce.dto.RequestDto;
import com.spring.aoce.entity.enums.RequestStatus;
import com.spring.aoce.entity.enums.RequestType;
import com.spring.aoce.service.ComputerService;
import com.spring.aoce.service.PrinterService;
import com.spring.aoce.util.DateUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.spring.aoce.service.UserService.findByPrincipal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, unique = true)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false, nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "equipment_id", updatable = false)
    private Equipment equipment;

    @Column(name = "request_type", updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestType requestType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    // Дата обработки заявки (если выполнена)
    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    public Request(RequestDto requestDto) {
        this.user = findByPrincipal();
        EquipmentDto equipment = requestDto.getEquipment();
        if (equipment != null) {
            switch (equipment.getEquipmentType()) {
                case COMPUTER:
                    this.equipment = ComputerService.getEntity(equipment.getId());
                    break;
                case PRINTER:
                    this.equipment = PrinterService.getEntity(equipment.getId());
                    break;
            }
        }
        this.requestType = requestDto.getRequestType();
        this.status = requestDto.getStatus();
        this.createdAt = DateUtils.parseToLocalDateTime(requestDto.getCreatedAt());
        this.processedAt = DateUtils.parseToLocalDateTime(requestDto.getProcessedAt());
    }
}

