package com.skim.client.dto;

/*
    Maps to column names returned via api.
    Note: This only maps to columns we care about and is not exhaustive
 */
public enum QuandlTimeSeriesColumn {
    DATE("Date"),
    OPEN("Open"),
    CLOSE("Close"),
    ADJ_OPEN("Adj. Open"),
    ADJ_CLOSE("Adj. Close");

    private String column;

    QuandlTimeSeriesColumn(String column) {
        this.column = column;
    }

    public String getColumn() {
        return column;
    }
}
