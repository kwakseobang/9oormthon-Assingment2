package com.kwakmunsu.diary.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TimeConverter {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
        DateTimeFormatter.ofPattern("yyyy. M. d. a h:mm").withLocale(Locale.KOREAN);

    public static String datetimeToString(LocalDateTime localDateTime) {
        return localDateTime.format(DATE_TIME_FORMATTER);
    }

}