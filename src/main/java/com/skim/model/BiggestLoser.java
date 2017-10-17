package com.skim.model;

public class BiggestLoser {
    private final String tickerSymbol;
    private final Integer days;

    public BiggestLoser(String tickerSymbol, Integer days) {
        this.tickerSymbol = tickerSymbol;
        this.days = days;
    }

    public String getTickerSymbol() {
        return tickerSymbol;
    }

    public Integer getDays() {
        return days;
    }

    @Override
    public String toString() {
        return "BiggestLoser{" +
                "tickerSymbol='" + tickerSymbol + '\'' +
                ", days=" + days +
                '}';
    }
}
