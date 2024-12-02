package com.spring.aoce.service;

import com.opencsv.CSVWriter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
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
                new String[]{
                        "Идентификатор", "Название", "Серийный номер", "Инвентарный номер", "Производитель", "Модель",
                        "Дата покупки", "Дата окончания гарантии", "Статус", "Процессор",
                        "Оперативная память (ГБ)", "Хранилище (ГБ)", "Видеокарта"
                },
                computerService.getAll(),
                computer -> new String[]{
                        String.valueOf(computer.getId()), computer.getName(), computer.getSerialNumber(),
                        computer.getInventoryNumber(), computer.getManufacturer(), computer.getModel(),
                        computer.getPurchaseDate(), computer.getWarrantyExpiration(),
                        computer.getStatus().getValue(), computer.getCpu(),
                        String.valueOf(computer.getRam()), String.valueOf(computer.getStorage()), computer.getGpu()
                }
        );
    }

    public void exportPrinterReport(HttpServletResponse response) {
        exportToCsv(
                response,
                "printer_report.csv",
                new String[]{
                        "Идентификатор", "Название", "Серийный номер", "Инвентарный номер", "Производитель", "Модель",
                        "Дата покупки", "Дата окончания гарантии", "Статус", "Тип принтера",
                        "Цветной", "Размер бумаги"
                },
                printerService.getAll(),
                printer -> new String[]{
                        String.valueOf(printer.getId()), printer.getName(), printer.getSerialNumber(),
                        printer.getInventoryNumber(), printer.getManufacturer(), printer.getModel(),
                        printer.getPurchaseDate(), printer.getWarrantyExpiration(),
                        printer.getStatus().getValue(), printer.getPrinterType().getValue(),
                        printer.getIsColor() ? "Да" : "Нет", printer.getPaperSize().getValue()
                }
        );
    }

    private <T> void exportToCsv(
            HttpServletResponse response,
            String fileName,
            String[] headers,
            List<T> items,
            Function<T, String[]> mapper
    ) {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        try (OutputStreamWriter outputStreamWriter =
                     new OutputStreamWriter(response.getOutputStream(), Charset.forName("Windows-1251"))) {
            CSVWriter writer = new CSVWriter(outputStreamWriter, ';', DEFAULT_QUOTE_CHARACTER,
                    DEFAULT_ESCAPE_CHARACTER, DEFAULT_LINE_END);

            writer.writeNext(headers);

            for (T item : items) {
                writer.writeNext(mapper.apply(item));
            }

            writer.close();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при экспорте CSV-отчета", e);
        }
    }
}
