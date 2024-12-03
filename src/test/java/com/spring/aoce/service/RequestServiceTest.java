package com.spring.aoce.service;

import com.spring.aoce.dto.RequestDto;
import com.spring.aoce.entity.Computer;
import com.spring.aoce.entity.Printer;
import com.spring.aoce.entity.Request;
import com.spring.aoce.entity.RequestComputer;
import com.spring.aoce.entity.RequestPrinter;
import com.spring.aoce.entity.enums.EquipmentType;
import com.spring.aoce.repository.RequestComputerRepository;
import com.spring.aoce.repository.RequestPrinterRepository;
import com.spring.aoce.repository.RequestRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static com.spring.aoce.data.ComputerData.createComputerDto;
import static com.spring.aoce.data.NotificationData.createNotification;
import static com.spring.aoce.data.PrinterData.createPrinterDto;
import static com.spring.aoce.data.RequestData.createRequest;
import static com.spring.aoce.data.RequestData.createRequestComputer;
import static com.spring.aoce.data.RequestData.createRequestPrinter;
import static com.spring.aoce.data.UserData.createUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RequestServiceTest {

    @InjectMocks
    private RequestService requestService;
    @Mock
    private RequestComputerRepository requestComputerRepository;
    @Mock
    private RequestPrinterRepository requestPrinterRepository;
    @Mock
    private RequestRepository requestRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private ComputerService computerService;

    private MockedStatic<UserService> mockedStaticUserService;
    private MockedStatic<ComputerService> mockedStaticComputerService;
    private MockedStatic<PrinterService> mockedStaticPrinterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockedStaticUserService = mockStatic(UserService.class);
        mockedStaticUserService.when(UserService::findByPrincipal).thenReturn(createUser());

        mockedStaticComputerService = mockStatic(ComputerService.class);
        mockedStaticComputerService.when(() -> ComputerService.getEntity(anyLong()))
                .thenReturn(new Computer(createComputerDto()));

        mockedStaticPrinterService = mockStatic(PrinterService.class);
        mockedStaticPrinterService.when(() -> PrinterService.getEntity(anyLong()))
                .thenReturn(new Printer(createPrinterDto()));

        requestService.setRequestRepository(requestRepository);
    }

    @AfterEach
    void tearDown() {
        mockedStaticUserService.close();
        mockedStaticComputerService.close();
        mockedStaticPrinterService.close();
    }

    @Test
    void testCreateComputerRequest() {
        RequestComputer request = createRequestComputer();
        // Given
        when(requestComputerRepository.save(any(RequestComputer.class))).thenReturn(request);

        // When
        when(notificationService.create(request)).thenReturn(createNotification());
        requestService.create(createComputerDto());

        // Then
        verify(requestComputerRepository).save(any(RequestComputer.class));
        verify(notificationService).create(any(RequestComputer.class));
    }

    @Test
    void testCreatePrinterRequest() {
        RequestPrinter request = createRequestPrinter();
        // Given
        when(requestPrinterRepository.save(any(RequestPrinter.class))).thenReturn(request);

        // When
        when(notificationService.create(request)).thenReturn(createNotification());
        requestService.create(createPrinterDto());

        // Then
        verify(requestPrinterRepository).save(any(RequestPrinter.class));
        verify(notificationService).create(any(RequestPrinter.class));
    }

    @Test
    void testGetRequest() {
        // Given
        Request request = createRequest();
        when(requestRepository.findById(1L)).thenReturn(java.util.Optional.of(request));

        // When
        RequestDto result = requestService.get(1L);

        // Then
        assertNotNull(result);
    }

    @Test
    void testEditComputerRequest() {
        RequestComputer request = createRequestComputer();
        // Given
        when(requestComputerRepository.save(any(RequestComputer.class))).thenReturn(request);
        when(notificationService.create(request)).thenReturn(createNotification());

        // When
        requestService.edit(createComputerDto(), 1L);

        // Then
        verify(requestComputerRepository).save(any(RequestComputer.class));
        verify(notificationService).create(any(RequestComputer.class));
    }

    @Test
    void testEditPrinterRequest() {
        RequestPrinter request = createRequestPrinter();
        // Given
        when(requestPrinterRepository.save(any(RequestPrinter.class))).thenReturn(request);
        when(notificationService.create(request)).thenReturn(createNotification());

        // When
        requestService.edit(createPrinterDto(), 1L);

        // Then
        verify(requestPrinterRepository).save(any(RequestPrinter.class));
        verify(notificationService).create(any(RequestPrinter.class));
    }

    @Test
    void testAcceptRequest() {
        // Given
        RequestComputer request = createRequestComputer();
        when(requestRepository.findById(1L)).thenReturn(java.util.Optional.of(request));

        // When
        requestService.accept(1L);

        // Then
        verify(requestRepository).save(request);
    }

    @Test
    void testRejectRequest() {
        // Given
        RequestComputer request = createRequestComputer();
        when(requestRepository.findById(1L)).thenReturn(java.util.Optional.of(request));

        // When
        requestService.reject(1L);

        // Then
        verify(requestRepository).save(request);
    }

    @Test
    public void testGetAllComputers() {
        RequestComputer requestComputer1 = new RequestComputer();
        RequestComputer requestComputer2 = new RequestComputer();

        Page<RequestComputer> requestComputerPage = new PageImpl<>(List.of(requestComputer1, requestComputer2));

        when(requestComputerRepository.findAllByUserId(anyLong(), any(PageRequest.class)))
                .thenReturn(requestComputerPage);

        Page<RequestDto> result = requestService.getAll(EquipmentType.COMPUTER, 0, 10);

        assertEquals(2, result.getTotalElements());
        verify(requestComputerRepository).findAllByUserId(anyLong(), any(PageRequest.class));
    }

    @Test
    public void testGetAllPrinters() {
        RequestPrinter requestPrinter1 = new RequestPrinter();
        RequestPrinter requestPrinter2 = new RequestPrinter();

        Page<RequestPrinter> requestPrinterPage = new PageImpl<>(List.of(requestPrinter1, requestPrinter2));

        when(requestPrinterRepository.findAllByUserId(anyLong(), any(PageRequest.class)))
                .thenReturn(requestPrinterPage);

        Page<RequestDto> result = requestService.getAll(EquipmentType.PRINTER, 0, 10);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        verify(requestPrinterRepository).findAllByUserId(anyLong(), any(PageRequest.class));
    }
}
