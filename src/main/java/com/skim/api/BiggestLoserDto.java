package com.skim.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.skim.model.BiggestLoser;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"ticker_symbol", "days"})
public class BiggestLoserDto {
    @JsonProperty("ticker_symbol")
    private String tickerSymbol;

    @JsonProperty
    private Integer days;

    public BiggestLoserDto(BiggestLoser bl) {
        this.tickerSymbol = bl.getTickerSymbol();
        this.days = bl.getDays();
    }

    public String getTickerSymbol() {
        return tickerSymbol;
    }

    public void setTickerSymbol(String tickerSymbol) {
        this.tickerSymbol = tickerSymbol;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }
}
