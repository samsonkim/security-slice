package com.skim.model;

import org.joda.time.LocalDate;

public class DailySecurityPrice {
    private final LocalDate date;
    private final float open;
    private final float close;
    private final float high;
    private final float low;
    private final long volume;

    public DailySecurityPrice(LocalDate date, float open, float close, float high, float low, long volume) {
        this.date = date;
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
        this.volume = volume;
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

    public float getHigh() {
        return high;
    }

    public float getLow() {
        return low;
    }

    public long getVolume() {
        return volume;
    }

    @Override
    public String toString() {
        return "DailySecurityPrice{" +
                "date=" + date +
                ", open=" + open +
                ", close=" + close +
                ", high=" + high +
                ", low=" + low +
                ", volume=" + volume +
                '}';
    }
}
