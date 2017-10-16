package com.skim.model;

import java.util.Date;

public class MonthlySecurityPrice {
    private final Date month;
    private final float averageOpen;
    private final float averageClose;

    public MonthlySecurityPrice(Date month, float averageOpen, float averageClose) {
        this.month = month;
        this.averageOpen = averageOpen;
        this.averageClose = averageClose;
    }

    public Date getMonth() {
        return month;
    }

    public float getAverageOpen() {
        return averageOpen;
    }

    public float getAverageClose() {
        return averageClose;
    }

    @Override
    public String toString() {
        return "MonthlySecurityPrice{" +
                "month=" + month +
                ", averageOpen=" + averageOpen +
                ", averageClose=" + averageClose +
                '}';
    }
}
