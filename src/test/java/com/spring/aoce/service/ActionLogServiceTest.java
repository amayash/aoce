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
import com.spring.aoce.entity.enums.EquipmentType;
import com.spring.aoce.entity.enums.RequestType;
import com.spring.aoce.repository.ActionLogRepository;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static com.spring.aoce.data.ActionLogData.createActionLog;
import static com.spring.aoce.data.UserData.createUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ActionLogServiceTest {

    @Mock
    private ActionLogRepository actionLogRepository;

    @InjectMocks
    private ActionLogService actionLogService;

    private final MockedStatic<UserService> mockedStaticUserService = mockStatic(UserService.class);

    private final MockedStatic<ComputerService> mockedStaticComputerService = mockStatic(ComputerService.class);

    @AfterEach
    void tearDown() {
        mockedStaticUserService.close();
        mockedStaticComputerService.close();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        actionLogService.setActionLogRepository(actionLogRepository);
    }

    @Test
    void getAll_ShouldReturnPagedResults() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.asc("timestamp")));

        Page<ActionLog> actionLogPage = new PageImpl<>(List.of(createActionLog()), pageable, 1);

        when(actionLogRepository.findAll(pageable)).thenReturn(actionLogPage);

        Page<ActionLogDto> result = actionLogService.getAll(0, 10);
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(actionLogRepository, times(1)).findAll(pageable);
    }

    @Test
    void create_ShouldSaveActionLog() {
        EquipmentDto oldModel = new ComputerDto();
        EquipmentDto newModel = new PrinterDto();

        when(UserService.findByPrincipal()).thenReturn(createUser());
        actionLogService.create(oldModel, newModel, RequestType.INSERT, EquipmentType.COMPUTER, 1L, null);

        verify(actionLogRepository, times(1)).save(any(ActionLog.class));
    }

    @Test
    void getOldEquipmentByRequestId_ShouldReturnOldModel() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        ActionLog actionLog = new ActionLog();
        actionLog.setOldModel(objectMapper.writeValueAsString(new ComputerDto()));
        Computer computer = new Computer();
        actionLog.setEquipment(computer);

        when(actionLogRepository.findByRequestId(1L)).thenReturn(actionLog);

        EquipmentDto result = ActionLogService.getOldEquipmentByRequestId(1L, EquipmentType.COMPUTER, 1L);

        assertInstanceOf(ComputerDto.class, result);
        verify(actionLogRepository, times(1)).findByRequestId(1L);
    }
}
