package com.spring.aoce.service;

import com.spring.aoce.dto.ComputerDto;
import com.spring.aoce.dto.PrinterDto;
import com.spring.aoce.dto.RequestComputerDto;
import com.spring.aoce.dto.RequestDto;
import com.spring.aoce.dto.RequestPrinterDto;
import com.spring.aoce.entity.Notification;
import com.spring.aoce.entity.Request;
import com.spring.aoce.entity.RequestComputer;
import com.spring.aoce.entity.RequestPrinter;
import com.spring.aoce.entity.UserNotification;
import com.spring.aoce.entity.enums.EquipmentStatus;
import com.spring.aoce.entity.enums.EquipmentType;
import com.spring.aoce.entity.enums.RequestStatus;
import com.spring.aoce.entity.enums.RequestType;
import com.spring.aoce.repository.RequestComputerRepository;
import com.spring.aoce.repository.RequestPrinterRepository;
import com.spring.aoce.repository.RequestRepository;
import com.spring.aoce.util.DateUtils;
import jakarta.persistence.EntityNotFoundException;
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
public class RequestService {
    private static RequestRepository requestRepository;
    private final RequestComputerRepository requestComputerRepository;
    private final RequestPrinterRepository requestPrinterRepository;
    private final NotificationService notificationService;
    private final ComputerService computerService;
    private final PrinterService printerService;

    @Autowired
    public void setRequestRepository(RequestRepository repository) {
        RequestService.requestRepository = repository;
    }

    @Transactional(readOnly = true)
    public RequestDto get(Long id) {
        Request request = getEntity(id);
        if (request instanceof RequestComputer) {
            return new RequestComputerDto(request);
        } else {
            return new RequestPrinterDto(request);
        }
    }

    @Transactional(readOnly = true)
    public Page<RequestDto> getAll(EquipmentType equipmentType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdAt")));
        return switch (equipmentType) {
            case COMPUTER -> requestComputerRepository.findAllByUserId(findByPrincipal().getId(), pageable)
                    .map(RequestDto::new);
            case PRINTER -> requestPrinterRepository.findAllByUserId(findByPrincipal().getId(), pageable)
                    .map(RequestDto::new);
        };
    }

    @Transactional
    public void create(ComputerDto equipment) {
        RequestDto requestDto = new RequestDto();
        requestDto.setRequestType(RequestType.INSERT);
        requestDto.setCreatedAt(DateUtils.formatLocalDateTime(LocalDateTime.now()));
        requestDto.setStatus(RequestStatus.PENDING_REVIEW);
        RequestComputer requestComputer = requestComputerRepository.save(new RequestComputer(requestDto, equipment));

        Notification notification = notificationService.create(requestComputer);
        notificationService.markAsRead(notification.getId());
    }

    @Transactional
    public void create(PrinterDto equipment) {
        RequestDto requestDto = new RequestDto();
        requestDto.setRequestType(RequestType.INSERT);
        requestDto.setCreatedAt(DateUtils.formatLocalDateTime(LocalDateTime.now()));
        requestDto.setStatus(RequestStatus.PENDING_REVIEW);
        RequestPrinter requestPrinter = requestPrinterRepository.save(new RequestPrinter(requestDto, equipment));

        Notification notification = notificationService.create(requestPrinter);
        notificationService.markAsRead(notification.getId());
    }

    @Transactional
    public void edit(ComputerDto equipment, Long id) {
        RequestDto requestDto = new RequestDto();
        requestDto.setRequestType(RequestType.UPDATE);
        requestDto.setCreatedAt(DateUtils.formatLocalDateTime(LocalDateTime.now()));
        requestDto.setStatus(RequestStatus.PENDING_REVIEW);
        // old values
        ComputerDto oldEquipment = new ComputerDto(ComputerService.getEntity(id));
        equipment.setPurchaseDate(oldEquipment.getPurchaseDate());
        equipment.setWarrantyExpiration(oldEquipment.getWarrantyExpiration());
        requestDto.setEquipment(oldEquipment);
        RequestComputer requestComputer = requestComputerRepository.save(new RequestComputer(requestDto, equipment));

        Notification notification = notificationService.create(requestComputer);
        notificationService.markAsRead(notification.getId());
    }

    @Transactional
    public void edit(PrinterDto equipment, Long id) {
        RequestDto requestDto = new RequestDto();
        requestDto.setRequestType(RequestType.UPDATE);
        requestDto.setCreatedAt(DateUtils.formatLocalDateTime(LocalDateTime.now()));
        requestDto.setStatus(RequestStatus.PENDING_REVIEW);
        // old values
        PrinterDto oldEquipment = new PrinterDto(PrinterService.getEntity(id));
        equipment.setPurchaseDate(oldEquipment.getPurchaseDate());
        equipment.setWarrantyExpiration(oldEquipment.getWarrantyExpiration());
        requestDto.setEquipment(oldEquipment);
        RequestPrinter requestPrinter = requestPrinterRepository.save(new RequestPrinter(requestDto, equipment));

        Notification notification = notificationService.create(requestPrinter);
        notificationService.markAsRead(notification.getId());
    }

    @Transactional
    public void deleteComputer(Long equipmentId) {
        RequestDto requestDto = new RequestDto();
        requestDto.setRequestType(RequestType.DELETE);
        requestDto.setCreatedAt(DateUtils.formatLocalDateTime(LocalDateTime.now()));
        requestDto.setStatus(RequestStatus.PENDING_REVIEW);

        ComputerDto equipment = new ComputerDto(ComputerService.getEntity(equipmentId));
        equipment.setStatus(EquipmentStatus.DECOMMISSIONED);
        requestDto.setEquipment(equipment);

        RequestComputer requestComputer = requestComputerRepository.save(new RequestComputer(requestDto, equipment));
        Notification notification = notificationService.create(requestComputer);
        notificationService.markAsRead(notification.getId());
    }

    @Transactional
    public void deletePrinter(Long equipmentId) {
        RequestDto requestDto = new RequestDto();
        requestDto.setRequestType(RequestType.DELETE);
        requestDto.setCreatedAt(DateUtils.formatLocalDateTime(LocalDateTime.now()));
        requestDto.setStatus(RequestStatus.PENDING_REVIEW);

        PrinterDto equipment = new PrinterDto(PrinterService.getEntity(equipmentId));
        equipment.setStatus(EquipmentStatus.DECOMMISSIONED);
        requestDto.setEquipment(equipment);

        RequestPrinter requestPrinter = requestPrinterRepository.save(new RequestPrinter(requestDto, equipment));
        Notification notification = notificationService.create(requestPrinter);
        notificationService.markAsRead(notification.getId());
    }

    @Transactional
    public void accept(Long id) {
        Request request = getEntity(id);
        if (request.getStatus() != RequestStatus.PENDING_REVIEW) {
            throw new RuntimeException("Заявка уже обработана.");
        }
        if (request.getRequestType().equals(RequestType.INSERT)) {
            if (request instanceof RequestComputer) {
                ComputerDto equipmentDto = buildComputerDto((RequestComputer) request);
                computerService.create(equipmentDto, request);
            } else if (request instanceof RequestPrinter) {
                PrinterDto equipmentDto = buildPrinterDto((RequestPrinter) request);
                printerService.create(equipmentDto, request);
            }
        } else if (request.getRequestType().equals(RequestType.UPDATE)) {
            if (request instanceof RequestComputer) {
                ComputerDto equipmentDto = buildComputerDto((RequestComputer) request);
                computerService.edit(equipmentDto, request.getEquipment().getId(), request);
            } else if (request instanceof RequestPrinter) {
                PrinterDto equipmentDto = buildPrinterDto((RequestPrinter) request);
                printerService.edit(equipmentDto, request.getEquipment().getId(), request);
            }
        } else if (request.getRequestType().equals(RequestType.DELETE)) {
            if (request instanceof RequestComputer) {
                computerService.delete(request.getEquipment().getId(), request);
            } else if (request instanceof RequestPrinter) {
                printerService.delete(request.getEquipment().getId(), request);
            }
        }
        request.setProcessedAt(LocalDateTime.now());
        request.setStatus(RequestStatus.APPROVED);
        setNotificationUnread(request.getUser().getId(), request.getId());
        requestRepository.save(request);
    }

    @Transactional
    public void reject(Long id) {
        Request request = getEntity(id);
        request.setStatus(RequestStatus.REJECTED);
        setNotificationUnread(request.getUser().getId(), request.getId());
        requestRepository.save(request);
    }

    public static Request getEntity(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Request with id " + id + " not found"));
    }

    private ComputerDto buildComputerDto(RequestComputer requestComputer) {
        ComputerDto dto = new ComputerDto();
        dto.setName(requestComputer.getName());
        dto.setSerialNumber(requestComputer.getSerialNumber());
        dto.setInventoryNumber(requestComputer.getInventoryNumber());
        dto.setManufacturer(requestComputer.getManufacturer());
        dto.setModel(requestComputer.getModel());
        dto.setPurchaseDate(DateUtils.formatLocalDate(requestComputer.getPurchaseDate()));
        dto.setWarrantyExpiration(DateUtils.formatLocalDate(requestComputer.getWarrantyExpiration()));
        dto.setStatus(requestComputer.getEquipmentStatus());

        dto.setCpu(requestComputer.getCpu());
        dto.setRam(requestComputer.getRam());
        dto.setStorage(requestComputer.getStorage());
        dto.setGpu(requestComputer.getGpu());
        return dto;
    }

    private PrinterDto buildPrinterDto(RequestPrinter requestPrinter) {
        PrinterDto dto = new PrinterDto();
        dto.setName(requestPrinter.getName());
        dto.setSerialNumber(requestPrinter.getSerialNumber());
        dto.setInventoryNumber(requestPrinter.getInventoryNumber());
        dto.setManufacturer(requestPrinter.getManufacturer());
        dto.setModel(requestPrinter.getModel());
        dto.setPurchaseDate(DateUtils.formatLocalDate(requestPrinter.getPurchaseDate()));
        dto.setWarrantyExpiration(DateUtils.formatLocalDate(requestPrinter.getWarrantyExpiration()));
        dto.setStatus(requestPrinter.getEquipmentStatus());

        dto.setPrinterType(requestPrinter.getPrinterType());
        dto.setIsColor(requestPrinter.getIsColor());
        dto.setPaperSize(requestPrinter.getPaperSize());
        return dto;
    }

    private void setNotificationUnread(Long userId, Long requestId) {
        UserNotification userNotification = notificationService.findBySenderIdAndRequestId(userId, requestId);
        if (userNotification != null) {
            userNotification.setIsRead(false);
            userNotification.getNotification().setSentAt(LocalDateTime.now());
        }
    }
}
