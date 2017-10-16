package com.skim.utils;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class DateUtils {
    public static final DateTimeFormatter QUANDL_DATE_FORMAT = ISODateTimeFormat.date();;
    public static final DateTimeFormatter YEAR_MONTH_DATE_FORMAT = DateTimeFormat.forPattern("yyyy-MM");
}
