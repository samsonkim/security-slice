package com.skim.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BiggestLoserDto {
    @JsonProperty("ticker_symbol")
    private String tickerSymbol;

    @JsonProperty
    private Integer days;

}
