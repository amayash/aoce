package com.spring.aoce.service;

import com.opencsv.CSVWriter;
import com.spring.aoce.dto.ComputerDto;
import com.spring.aoce.dto.EquipmentDto;
import com.spring.aoce.dto.PrinterDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.opencsv.ICSVWriter.DEFAULT_ESCAPE_CHARACTER;
import static com.opencsv.ICSVWriter.DEFAULT_LINE_END;
import static com.opencsv.ICSVWriter.DEFAULT_QUOTE_CHARACTER;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ComputerService computerService;
    private final PrinterService printerService;

    public void exportComputerReport(HttpServletResponse response) {
        exportToCsv(
                response,
                "computer_report.csv",
                getComputerHeaders(),
                computerService.getAll(),
                this::mapComputerToData
        );
    }

    public void exportPrinterReport(HttpServletResponse response) {
        exportToCsv(
                response,
                "printer_report.csv",
                getPrinterHeaders(),
                printerService.getAll(),
                this::mapPrinterToData
        );
    }

    private <T> void exportToCsv(
            HttpServletResponse response,
            String fileName,
            ArrayList<String> headers,
            List<T> items,
            Function<T, String[]> mapper
    ) {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        try (OutputStreamWriter outputStreamWriter =
                     new OutputStreamWriter(response.getOutputStream(), Charset.forName("Windows-1251"))) {
            CSVWriter writer = new CSVWriter(outputStreamWriter, ';', DEFAULT_QUOTE_CHARACTER,
                    DEFAULT_ESCAPE_CHARACTER, DEFAULT_LINE_END);

            writer.writeNext(headers.toArray(new String[0]));

            for (T item : items) {
                writer.writeNext(mapper.apply(item));
            }

            writer.close();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при экспорте CSV-отчета", e);
        }
    }

    private ArrayList<String> getCommonHeaders() {
        ArrayList<String> headers = new ArrayList<>();
        headers.add("Идентификатор");
        headers.add("Название");
        headers.add("Серийный номер");
        headers.add("Инвентарный номер");
        headers.add("Производитель");
        headers.add("Модель");
        headers.add("Дата покупки");
        headers.add("Дата окончания гарантии");
        headers.add("Статус");
        return headers;
    }

    private ArrayList<String> getComputerHeaders() {
        ArrayList<String> headers = getCommonHeaders();
        headers.add("Процессор");
        headers.add("Оперативная память (ГБ)");
        headers.add("Хранилище (ГБ)");
        headers.add("Видеокарта");
        return headers;
    }

    private ArrayList<String> getPrinterHeaders() {
        ArrayList<String> headers = getCommonHeaders();
        headers.add("Тип принтера");
        headers.add("Цветной");
        headers.add("Размер бумаги");
        return headers;
    }

    private ArrayList<String> getCommonFields(EquipmentDto equipmentDto) {
        ArrayList<String> data = new ArrayList<>();
        data.add(String.valueOf(equipmentDto.getId()));
        data.add(equipmentDto.getName());
        data.add(equipmentDto.getSerialNumber());
        data.add(equipmentDto.getInventoryNumber());
        data.add(equipmentDto.getManufacturer());
        data.add(equipmentDto.getModel());
        data.add(equipmentDto.getPurchaseDate());
        data.add(equipmentDto.getWarrantyExpiration());
        data.add(equipmentDto.getStatus().getValue());
        return data;
    }

    private String[] mapComputerToData(ComputerDto computer) {
        ArrayList<String> data = getCommonFields(computer);

        data.add(computer.getCpu());
        data.add(String.valueOf(computer.getRam()));
        data.add(String.valueOf(computer.getStorage()));
        data.add(computer.getGpu());
        return data.toArray(new String[0]);
    }

    private String[] mapPrinterToData(PrinterDto printer) {
        ArrayList<String> data = getCommonFields(printer);

        data.add(printer.getPrinterType().getValue());
        data.add(printer.getIsColor() ? "Да" : "Нет");
        data.add(printer.getPaperSize().getValue());
        return data.toArray(new String[0]);
    }
}
