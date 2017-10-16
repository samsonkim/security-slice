package com.skim.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skim.model.MonthlySecurityPrice;
import org.joda.time.LocalDate;


@JsonIgnoreProperties(ignoreUnknown = true)
public class MonthlySecurityPriceDto {
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM")
    private LocalDate month;

    @JsonProperty("average_open")
    private Float averageOpen;

    @JsonProperty("average_close")
    private Float averageClose;

    public MonthlySecurityPriceDto(LocalDate month, Float averageOpen, Float averageClose) {
        this.month = month;
        this.averageOpen = averageOpen;
        this.averageClose = averageClose;
    }

    public MonthlySecurityPriceDto(MonthlySecurityPrice msp) {
       this.month = msp.getMonthYearDate();
       this.averageOpen = msp.getAverageOpen();
       this.averageClose = msp.getAverageClose();
    }

    public LocalDate getMonth() {
        return month;
    }

    public void setMonth(LocalDate month) {
        this.month = month;
    }

    public Float getAverageOpen() {
        return averageOpen;
    }

    public void setAverageOpen(Float averageOpen) {
        this.averageOpen = averageOpen;
    }

    public Float getAverageClose() {
        return averageClose;
    }

    public void setAverageClose(Float averageClose) {
        this.averageClose = averageClose;
    }

    @Override
    public String toString() {
        return "MonthlySecurityPriceDto{" +
                "month=" + month +
                ", averageOpen=" + averageOpen +
                ", averageClose=" + averageClose +
                '}';
    }
}
