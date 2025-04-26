package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeInfo {
    private String timezone;
    private String localTime;  // текущее местное время
    private String utcTime;    // текущее UTC время в формате UFC (ISO 8601)
    private String timeDescription;

    public String getTimeDescription() {
        String city = timezone.substring(timezone.indexOf('/') + 1);
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of(getTimezone()));

        return city + ": " + now.format(DateTimeFormatter.ofPattern("HH:mm '('X' UTC)'"));
    }
}
