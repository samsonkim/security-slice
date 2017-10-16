package com.skim.model;

import java.util.Date;

public class DailySecurityPrice {
    private final Date date;
    private final float open;
    private final float close;

    public DailySecurityPrice(Date date, float open, float close) {
        this.date = date;
        this.open = open;
        this.close = close;
    }

    public Date getDate() {
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
