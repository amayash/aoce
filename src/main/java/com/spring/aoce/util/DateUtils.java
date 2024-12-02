package com.spring.aoce.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class DateUtils {
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static String formatLocalDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(dateFormatter);
    }

    public static String formatLocalDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(dateTimeFormatter);
    }

    public static LocalDate parseToLocalDate(String dateString) {
        if (dateString == null) {
            return null;
        }
        return LocalDate.parse(dateString, dateFormatter);
    }

    public static LocalDateTime parseToLocalDateTime(String dateTimeString) {
        if (dateTimeString == null) {
            return null;
        }
        return LocalDateTime.parse(dateTimeString, dateTimeFormatter);
    }
}
