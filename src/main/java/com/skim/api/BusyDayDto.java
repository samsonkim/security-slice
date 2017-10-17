package com.skim.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skim.model.BusyDay;
import org.joda.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BusyDayDto {
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    @JsonProperty
    private Long volume;

    public BusyDayDto(BusyDay bd) {
        this.date = bd.getDate();
        this.volume = bd.getVolume();
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }
}
