package com.skim.model;

import org.joda.time.LocalDate;

public class DailySecurityPrice {
    private final LocalDate date;
    private final float open;
    private final float close;

    public DailySecurityPrice(LocalDate date, float open, float close) {
        this.date = date;
        this.open = open;
        this.close = close;
    }

    public LocalDate getDate() {
        return date;
    }

    public float getOpen() {
        return open;
    }

    public float getClose() {
        return close;
    }

    @Override
    public String toString() {
        return "DailySecurityPrice{" +
                "date=" + date +
                ", open=" + open +
                ", close=" + close +
                '}';
    }
}
