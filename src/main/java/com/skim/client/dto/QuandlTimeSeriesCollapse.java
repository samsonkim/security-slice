package com.skim.client.dto;

public enum QuandlTimeSeriesCollapse {
    NONE("none"),
    DAILY("daily"),
    MONTHLY("monthly");

    private String name;

    QuandlTimeSeriesCollapse(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
