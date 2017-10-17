package com.skim.client.dto;

import org.joda.time.LocalDate;

import java.util.Optional;

public class QuandlCacheTimeSeriesKey {
    private String databaseCode;
    private String stockSymbol;
    private Optional<QuandlTimeSeriesCollapse> collapse;
    private Optional<LocalDate> startDate;
    private Optional<LocalDate> endDate;

    public QuandlCacheTimeSeriesKey(String databaseCode, String stockSymbol, Optional<QuandlTimeSeriesCollapse> collapse, Optional<LocalDate> startDate, Optional<LocalDate> endDate) {
        this.databaseCode = databaseCode;
        this.stockSymbol = stockSymbol;
        this.collapse = collapse;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getDatabaseCode() {
        return databaseCode;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public Optional<QuandlTimeSeriesCollapse> getCollapse() {
        return collapse;
    }

    public Optional<LocalDate> getStartDate() {
        return startDate;
    }

    public Optional<LocalDate> getEndDate() {
        return endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuandlCacheTimeSeriesKey that = (QuandlCacheTimeSeriesKey) o;

        if (databaseCode != null ? !databaseCode.equals(that.databaseCode) : that.databaseCode != null) return false;
        if (stockSymbol != null ? !stockSymbol.equals(that.stockSymbol) : that.stockSymbol != null) return false;
        if (collapse != null ? !collapse.equals(that.collapse) : that.collapse != null) return false;
        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null) return false;
        return endDate != null ? endDate.equals(that.endDate) : that.endDate == null;
    }

    @Override
    public int hashCode() {
        int result = databaseCode != null ? databaseCode.hashCode() : 0;
        result = 31 * result + (stockSymbol != null ? stockSymbol.hashCode() : 0);
        result = 31 * result + (collapse != null ? collapse.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        return result;
    }
}
