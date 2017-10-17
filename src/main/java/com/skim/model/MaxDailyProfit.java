package com.skim.model;

import org.joda.time.LocalDate;

public class MaxDailyProfit {
    private final LocalDate date;
    private final Float profit;

    public MaxDailyProfit(LocalDate date, Float profit) {
        this.date = date;
        this.profit = profit;
    }

    public LocalDate getDate() {
        return date;
    }

    public Float getProfit() {
        return profit;
    }

    @Override
    public String toString() {
        return "MaxDailyProfit{" +
                "date=" + date +
                ", profit=" + profit +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MaxDailyProfit that = (MaxDailyProfit) o;

        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        return profit != null ? profit.equals(that.profit) : that.profit == null;
    }

    @Override
    public int hashCode() {
        int result = date != null ? date.hashCode() : 0;
        result = 31 * result + (profit != null ? profit.hashCode() : 0);
        return result;
    }
}
