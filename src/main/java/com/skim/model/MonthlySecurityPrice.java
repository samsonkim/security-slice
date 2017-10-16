package com.skim.model;

import org.joda.time.LocalDate;

public class MonthlySecurityPrice {
    private final LocalDate monthYearDate;
    private final float averageOpen;
    private final float averageClose;

    public MonthlySecurityPrice(LocalDate monthYearDate, float averageOpen, float averageClose) {
        this.monthYearDate = monthYearDate;
        this.averageOpen = averageOpen;
        this.averageClose = averageClose;
    }

    public LocalDate getMonthYearDate() {
        return monthYearDate;
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
                "monthYearDate=" + monthYearDate +
                ", averageOpen=" + averageOpen +
                ", averageClose=" + averageClose +
                '}';
    }
}
