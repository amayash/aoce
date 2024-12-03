package com.spring.aoce.service;

import com.spring.aoce.dto.ComputerDto;
import com.spring.aoce.entity.Computer;
import com.spring.aoce.entity.enums.EquipmentStatus;
import com.spring.aoce.entity.enums.EquipmentType;
import com.spring.aoce.entity.enums.RequestType;
import com.spring.aoce.repository.ComputerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static com.spring.aoce.data.ComputerData.createComputerDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ComputerServiceTest {

    @Mock
    private ComputerRepository computerRepository;

    @Mock
    private ActionLogService actionLogService;

    @InjectMocks
    private ComputerService computerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        computerService.setComputerRepository(computerRepository);
    }

    @Test
    void create_ShouldSaveComputerAndLogAction() {
        ComputerDto computerDto = createComputerDto();

        Computer savedComputer = new Computer(computerDto);
        savedComputer.setId(1L);

        when(computerRepository.save(any(Computer.class))).thenReturn(savedComputer);

        computerService.create(computerDto, null);

        ArgumentCaptor<Computer> computerCaptor = ArgumentCaptor.forClass(Computer.class);
        verify(computerRepository).save(computerCaptor.capture());
        assertEquals("Test Computer", computerCaptor.getValue().getName());
        assertEquals(EquipmentType.COMPUTER, computerCaptor.getValue().getEquipmentType());

        verify(actionLogService).create(
                isNull(),
                eq(computerDto),
                eq(RequestType.INSERT),
                eq(EquipmentType.COMPUTER),
                eq(1L),
                eq(null)
        );
    }

    @Test
    void edit_ShouldUpdateComputerAndLogAction() {
        Computer existingComputer = new Computer(createComputerDto());
        existingComputer.setId(1L);
        existingComputer.setName("Old Computer");

        ComputerDto updatedComputer = createComputerDto();
        updatedComputer.setName("Updated Computer");

        when(computerRepository.findById(1L)).thenReturn(Optional.of(existingComputer));

        computerService.edit(updatedComputer, 1L, null);

        assertEquals("Updated Computer", existingComputer.getName());
        verify(computerRepository).save(existingComputer);
        verify(actionLogService).create(
                any(ComputerDto.class),
                eq(updatedComputer),
                eq(RequestType.UPDATE),
                eq(EquipmentType.COMPUTER),
                eq(1L),
                eq(null)
        );
    }

    @Test
    void getAll_ShouldReturnPagedComputers() {
        Computer computer1 = new Computer(createComputerDto());
        Computer computer2 = new Computer(createComputerDto());
        List<Computer> computers = List.of(computer1, computer2);

        Page<Computer> computerPage = new PageImpl<>(computers);
        when(computerRepository.findAll(any(PageRequest.class))).thenReturn(computerPage);

        Page<ComputerDto> result = computerService.getAll(0, 2);

        assertEquals(2, result.getTotalElements());
        verify(computerRepository).findAll(any(PageRequest.class));
    }

    @Test
    void get_ShouldReturnComputerDto() {
        Computer computer = new Computer(createComputerDto());
        computer.setId(1L);

        when(computerRepository.findById(1L)).thenReturn(Optional.of(computer));

        ComputerDto result = computerService.get(1L);

        assertEquals(1L, result.getId());
        verify(computerRepository).findById(1L);
    }

    @Test
    void delete_ShouldMarkComputerAsDecommissionedAndLogAction() {
        Computer computer = new Computer(createComputerDto());
        computer.setId(1L);

        when(computerRepository.findById(1L)).thenReturn(Optional.of(computer));

        computerService.delete(1L, null);

        assertEquals(EquipmentStatus.DECOMMISSIONED, computer.getEquipmentStatus());
        verify(actionLogService).create(
                any(ComputerDto.class),
                isNull(),
                eq(RequestType.DELETE),
                eq(EquipmentType.COMPUTER),
                eq(1L),
                eq(null)
        );
    }

    @Test
    void getEntity_ShouldThrowExceptionWhenNotFound() {
        when(computerRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> ComputerService.getEntity(1L)
        );

        assertEquals("Equipment with id 1 not found", exception.getMessage());
    }
}
