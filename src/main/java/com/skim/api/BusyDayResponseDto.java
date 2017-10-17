package com.skim.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.skim.model.BusyDayResponse;

import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"average_volume", "days"})
public class BusyDayResponseDto {
    @JsonProperty("average_volume")
    private Long averageVolume;

    @JsonProperty
    private List<BusyDayDto> days;

    public BusyDayResponseDto(BusyDayResponse bdr) {
        this.averageVolume = bdr.getAverageVolume();
        this.days = bdr.getDays()
                .parallelStream()
                .map(d -> new BusyDayDto(d))
                .collect(Collectors.toList());
    }

    public Long getAverageVolume() {
        return averageVolume;
    }

    public void setAverageVolume(Long averageVolume) {
        this.averageVolume = averageVolume;
    }
}
