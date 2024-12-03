package com.spring.aoce.data;

import com.spring.aoce.dto.RequestComputerDto;
import com.spring.aoce.dto.RequestPrinterDto;
import com.spring.aoce.entity.Request;
import com.spring.aoce.entity.RequestComputer;
import com.spring.aoce.entity.RequestPrinter;
import com.spring.aoce.entity.enums.RequestStatus;
import com.spring.aoce.entity.enums.RequestType;

import java.time.LocalDateTime;

import static com.spring.aoce.data.ComputerData.createComputerDto;
import static com.spring.aoce.data.PrinterData.createPrinterDto;
import static com.spring.aoce.data.UserData.createUser;

public class RequestData {
    public static Request createRequest() {
        Request request = new Request();
        request.setId(1L);
        request.setUser(createUser());
        request.setEquipment(null);
        request.setRequestType(RequestType.INSERT);
        request.setStatus(RequestStatus.PENDING_REVIEW);
        request.setCreatedAt(LocalDateTime.now());
        request.setProcessedAt(null);
        return request;
    }

    public static RequestPrinter createRequestPrinter() {
        return new RequestPrinter(new RequestPrinterDto(createRequest()), createPrinterDto());
    }

    public static RequestComputer createRequestComputer() {
        return new RequestComputer(new RequestComputerDto(createRequest()), createComputerDto());
    }
}
