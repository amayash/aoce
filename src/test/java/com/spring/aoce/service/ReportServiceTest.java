package com.spring.aoce.service;

import com.spring.aoce.dto.ComputerDto;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.DelegatingServletOutputStream;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static com.spring.aoce.data.ComputerData.createComputerDto;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReportServiceTest {
    @InjectMocks
    private ReportService reportService;
    @Mock
    private ComputerService computerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExportComputerReport() throws Exception {
        // Arrange
        HttpServletResponse response = mock(HttpServletResponse.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        when(response.getOutputStream()).thenReturn(new DelegatingServletOutputStream(outputStream));

        List<ComputerDto> computers = List.of(createComputerDto());

        when(computerService.getAll()).thenReturn(computers);

        // Act
        reportService.exportComputerReport(response);

        // Assert
        String output = outputStream.toString("Windows-1251");
        assertTrue(output.contains("Test Computer"));
        assertTrue(output.contains("2023-01-01"));

        verify(response).setContentType("text/csv");
        verify(response).setHeader("Content-Disposition", "attachment; filename=\"computer_report.csv\"");
    }
}
