package com.spring.aoce.service;

import com.spring.aoce.dto.PrinterDto;
import com.spring.aoce.entity.Printer;
import com.spring.aoce.entity.enums.EquipmentStatus;
import com.spring.aoce.entity.enums.EquipmentType;
import com.spring.aoce.entity.enums.RequestType;
import com.spring.aoce.repository.PrinterRepository;
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

import static com.spring.aoce.data.PrinterData.createPrinterDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PrinterServiceTest {

    @Mock
    private PrinterRepository printerRepository;

    @Mock
    private ActionLogService actionLogService;

    @InjectMocks
    private PrinterService printerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        printerService.setPrinterRepository(printerRepository);
    }

    @Test
    void create_ShouldSavePrinterAndLogAction() {
        PrinterDto printerDto = createPrinterDto();

        Printer savedPrinter = new Printer(printerDto);
        savedPrinter.setId(1L);

        when(printerRepository.save(any(Printer.class))).thenReturn(savedPrinter);

        printerService.create(printerDto, null);

        ArgumentCaptor<Printer> printerCaptor = ArgumentCaptor.forClass(Printer.class);
        verify(printerRepository).save(printerCaptor.capture());
        assertEquals("Test Printer", printerCaptor.getValue().getName());
        assertEquals(EquipmentType.PRINTER, printerCaptor.getValue().getEquipmentType());

        verify(actionLogService).create(
                isNull(),
                eq(printerDto),
                eq(RequestType.INSERT),
                eq(EquipmentType.PRINTER),
                eq(1L),
                eq(null)
        );
    }

    @Test
    void edit_ShouldUpdatePrinterAndLogAction() {
        Printer existingPrinter = new Printer(createPrinterDto());
        existingPrinter.setId(1L);
        existingPrinter.setName("Old Printer");

        PrinterDto updatedPrinter = createPrinterDto();
        updatedPrinter.setName("Updated Printer");

        when(printerRepository.findById(1L)).thenReturn(Optional.of(existingPrinter));

        printerService.edit(updatedPrinter, 1L, null);

        assertEquals("Updated Printer", existingPrinter.getName());
        verify(printerRepository).save(existingPrinter);
        verify(actionLogService).create(
                any(PrinterDto.class),
                eq(updatedPrinter),
                eq(RequestType.UPDATE),
                eq(EquipmentType.PRINTER),
                eq(1L),
                eq(null)
        );
    }

    @Test
    void getAll_ShouldReturnPagedPrinters() {
        Printer printer1 = new Printer(createPrinterDto());
        Printer printer2 = new Printer(createPrinterDto());
        List<Printer> printers = List.of(printer1, printer2);

        Page<Printer> printerPage = new PageImpl<>(printers);
        when(printerRepository.findAll(any(PageRequest.class))).thenReturn(printerPage);

        Page<PrinterDto> result = printerService.getAll(0, 2);

        assertEquals(2, result.getTotalElements());
        verify(printerRepository).findAll(any(PageRequest.class));
    }

    @Test
    void get_ShouldReturnPrinterDto() {
        Printer printer = new Printer(createPrinterDto());
        printer.setId(1L);

        when(printerRepository.findById(1L)).thenReturn(Optional.of(printer));

        PrinterDto result = printerService.get(1L);

        assertEquals(1L, result.getId());
        verify(printerRepository).findById(1L);
    }

    @Test
    void delete_ShouldMarkPrinterAsDecommissionedAndLogAction() {
        Printer printer = new Printer(createPrinterDto());
        printer.setId(1L);

        when(printerRepository.findById(1L)).thenReturn(Optional.of(printer));

        printerService.delete(1L, null);

        assertEquals(EquipmentStatus.DECOMMISSIONED, printer.getEquipmentStatus());
        verify(actionLogService).create(
                any(PrinterDto.class),
                isNull(),
                eq(RequestType.DELETE),
                eq(EquipmentType.PRINTER),
                eq(1L),
                eq(null)
        );
    }

    @Test
    void getEntity_ShouldThrowExceptionWhenNotFound() {
        when(printerRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> PrinterService.getEntity(1L)
        );

        assertEquals("Equipment with id 1 not found", exception.getMessage());
    }
}
