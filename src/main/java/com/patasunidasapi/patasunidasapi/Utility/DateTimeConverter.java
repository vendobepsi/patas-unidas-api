package com.patasunidasapi.patasunidasapi.Utility;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class DateTimeConverter {
    
    //converte de millis pra instant e de instant pra localdatetime
    public static LocalDateTime convertMillisToLocalDateTime(Long timestampMillis){
        if (timestampMillis == null)
        return null;

        Instant instant = Instant.ofEpochMilli(timestampMillis);
        return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }

}
