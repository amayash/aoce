package com.spring.aoce.service;

import com.spring.aoce.dto.ComputerDto;
import com.spring.aoce.entity.Computer;
import com.spring.aoce.entity.Request;
import com.spring.aoce.entity.enums.EquipmentStatus;
import com.spring.aoce.entity.enums.EquipmentType;
import com.spring.aoce.entity.enums.RequestType;
import com.spring.aoce.repository.ComputerRepository;
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
public class ComputerService {
    private static ComputerRepository computerRepository;
    private final ActionLogService actionLogService;

    @Autowired
    public void setComputerRepository(ComputerRepository repository) {
        ComputerService.computerRepository = repository;
    }

    @Transactional
    public void create(ComputerDto computer, Request request) {
        computer.setEquipmentType(EquipmentType.COMPUTER);
        Computer saved = computerRepository.save(new Computer(computer));
        actionLogService.create(null, computer, RequestType.INSERT, EquipmentType.COMPUTER, saved.getId(), request);
    }

    @Transactional
    public void edit(ComputerDto computer, Long id, Request request) {
        Computer computerEntity = getEntity(id);
        ComputerDto oldModel = new ComputerDto(computerEntity);

        computerEntity.setName(computer.getName());
        computerEntity.setSerialNumber(computer.getSerialNumber());
        computerEntity.setInventoryNumber(computer.getInventoryNumber());
        computerEntity.setManufacturer(computer.getManufacturer());
        computerEntity.setModel(computer.getModel());
        computerEntity.setEquipmentStatus(computer.getStatus());

        computerEntity.setCpu(computer.getCpu());
        computerEntity.setRam(computer.getRam());
        computerEntity.setStorage(computer.getStorage());
        computerEntity.setGpu(computer.getGpu());

        computerRepository.save(computerEntity);
        actionLogService.create(oldModel, computer, RequestType.UPDATE, EquipmentType.COMPUTER, id, request);
    }

    @Transactional
    public Page<ComputerDto> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("equipmentStatusOrder")));
        return computerRepository.findAll(pageable).map(ComputerDto::new);
    }

    @Transactional
    public List<ComputerDto> getAll() {
        return computerRepository.findAll(Sort.by(Sort.Order.asc("equipmentStatusOrder"))).stream().map(ComputerDto::new).toList();
    }

    @Transactional
    public ComputerDto get(Long id) {
        return new ComputerDto(getEntity(id));
    }

    @Transactional
    public void delete(Long id, Request request) {
        Computer computer = getEntity(id);
        ComputerDto computerDto = new ComputerDto(computer);
        computer.setEquipmentStatus(EquipmentStatus.DECOMMISSIONED);
        computerRepository.save(computer);
        actionLogService.create(computerDto, null, RequestType.DELETE, computer.getEquipmentType(), computer.getId(), request);
    }

    public static Computer getEntity(Long id) {
        return computerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipment with id " + id + " not found"));
    }
}
