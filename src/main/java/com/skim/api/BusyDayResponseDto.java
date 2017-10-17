package com.skim.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BusyDayResponseDto {
    @JsonProperty("ticker_symbol")
    private String tickerSymbol;

    @JsonProperty("average_volume")
    private Long averageVolume;

    @JsonProperty
    private List<BusyDayDto> days;


}
