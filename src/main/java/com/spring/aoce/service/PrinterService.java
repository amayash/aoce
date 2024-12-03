package com.spring.aoce.service;

import com.spring.aoce.dto.PrinterDto;
import com.spring.aoce.entity.Printer;
import com.spring.aoce.entity.Request;
import com.spring.aoce.entity.enums.EquipmentStatus;
import com.spring.aoce.entity.enums.EquipmentType;
import com.spring.aoce.entity.enums.RequestType;
import com.spring.aoce.repository.PrinterRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrinterService {
    private static PrinterRepository printerRepository;
    private final ActionLogService actionLogService;

    @Autowired
    public void setPrinterRepository(PrinterRepository repository) {
        PrinterService.printerRepository = repository;
    }

    @Transactional
    public void create(PrinterDto printer, Request request) {
        printer.setEquipmentType(EquipmentType.PRINTER);
        Printer saved = printerRepository.save(new Printer(printer));
        actionLogService.create(null, printer, RequestType.INSERT, EquipmentType.PRINTER, saved.getId(), request);
    }

    @Transactional
    public void edit(PrinterDto printer, Long id, Request request) {
        Printer printerEntity = getEntity(id);
        PrinterDto oldModel = new PrinterDto(printerEntity);

        printerEntity.setName(printer.getName());
        printerEntity.setSerialNumber(printer.getSerialNumber());
        printerEntity.setInventoryNumber(printer.getInventoryNumber());
        printerEntity.setManufacturer(printer.getManufacturer());
        printerEntity.setModel(printer.getModel());
        printerEntity.setEquipmentStatus(printer.getStatus());

        printerEntity.setPrinterType(printer.getPrinterType());
        printerEntity.setIsColor(printer.getIsColor());
        printerEntity.setPaperSize(printer.getPaperSize());

        printerRepository.save(printerEntity);
        actionLogService.create(oldModel, printer, RequestType.UPDATE, EquipmentType.PRINTER, id, request);
    }

    @Transactional
    public Page<PrinterDto> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("equipmentStatusOrder")));
        return printerRepository.findAll(pageable).map(PrinterDto::new);
    }

    @Transactional
    public List<PrinterDto> getAll() {
        return printerRepository.findAll(Sort.by(Sort.Order.asc("equipmentStatusOrder"))).stream().map(PrinterDto::new).toList();
    }

    @Transactional
    public PrinterDto get(Long id) {
        return new PrinterDto(getEntity(id));
    }

    @Transactional
    public void delete(Long id, Request request) {
        Printer printer = getEntity(id);
        PrinterDto printerDto = new PrinterDto(printer);
        printer.setEquipmentStatus(EquipmentStatus.DECOMMISSIONED);
        actionLogService.create(printerDto, null, RequestType.DELETE, printer.getEquipmentType(), printer.getId(), request);
    }

    public static Printer getEntity(Long id) {
        return printerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipment with id " + id + " not found"));
    }
}
