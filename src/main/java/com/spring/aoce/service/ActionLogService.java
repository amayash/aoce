package com.spring.aoce.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.spring.aoce.dto.ActionLogDto;
import com.spring.aoce.dto.ComputerDto;
import com.spring.aoce.dto.EquipmentDto;
import com.spring.aoce.dto.PrinterDto;
import com.spring.aoce.entity.ActionLog;
import com.spring.aoce.entity.Computer;
import com.spring.aoce.entity.Request;
import com.spring.aoce.entity.enums.EquipmentType;
import com.spring.aoce.entity.enums.RequestType;
import com.spring.aoce.repository.ActionLogRepository;
import com.spring.aoce.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.spring.aoce.service.UserService.findByPrincipal;

@Service
@RequiredArgsConstructor
public class ActionLogService {
    private static ActionLogRepository actionLogRepository;

    @Autowired
    public void setActionLogRepository(ActionLogRepository actionLogRepository) {
        ActionLogService.actionLogRepository = actionLogRepository;
    }

    @Transactional(readOnly = true)
    public Page<ActionLogDto> getAll(int page, int size) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("timestamp")));
        Page<ActionLog> logs = actionLogRepository.findAll(pageable);
        return logs.map(log -> {
            ActionLogDto dto = new ActionLogDto();
            dto.setId(log.getId());
            dto.setActionType(log.getActionType());
            dto.setTimestamp(DateUtils.formatLocalDateTime(log.getTimestamp()));
            dto.setUser(log.getUser().getEmail());
            if (log.getRequest() != null) {
                dto.setRequest(log.getRequest().getId());
            }
            try {
                if (log.getOldModel() != null) {
                    if (log.getEquipment().getClass().equals(Computer.class)) {
                        dto.setOldModel(objectMapper.readValue(log.getOldModel(), ComputerDto.class));
                    } else {
                        dto.setOldModel(objectMapper.readValue(log.getOldModel(), PrinterDto.class));
                    }
                }

                if (log.getNewModel() != null) {
                    if (log.getEquipment().getClass().equals(Computer.class)) {
                        dto.setNewModel(objectMapper.readValue(log.getNewModel(), ComputerDto.class));
                    } else {
                        dto.setNewModel(objectMapper.readValue(log.getNewModel(), PrinterDto.class));
                    }
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            return dto;
        });
    }

    @Transactional
    public void create(EquipmentDto oldModel,
                       EquipmentDto newModel,
                       RequestType actionType,
                       EquipmentType equipmentType,
                       Long equipmentId,
                       Request request) {
        try {
            ActionLog actionLog = new ActionLog();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            actionLog.setActionType(actionType);
            actionLog.setTimestamp(LocalDateTime.now());
            actionLog.setUser(findByPrincipal());
            actionLog.setRequest(request);
            if (oldModel != null) {
                actionLog.setOldModel(objectMapper.writeValueAsString(oldModel));
            }
            if (newModel != null) {
                actionLog.setNewModel(objectMapper.writeValueAsString(newModel));
            }
            switch (equipmentType) {
                case COMPUTER -> actionLog.setEquipment(ComputerService.getEntity(equipmentId));
                case PRINTER -> actionLog.setEquipment(PrinterService.getEntity(equipmentId));
            }

            actionLogRepository.save(actionLog);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static EquipmentDto getOldEquipmentByRequestId(Long requestId, EquipmentType equipmentType, Long equipmentId) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            ActionLog log = actionLogRepository.findByRequestId(requestId);
            if (log == null) {
                return switch (equipmentType) {
                    case COMPUTER -> new ComputerDto(ComputerService.getEntity(equipmentId));
                    case PRINTER -> new PrinterDto(PrinterService.getEntity(equipmentId));
                };
            }
            if (log.getEquipment().getClass().equals(Computer.class)) {
                return objectMapper.readValue(log.getOldModel(), ComputerDto.class);
            } else {
                return objectMapper.readValue(log.getOldModel(), PrinterDto.class);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
