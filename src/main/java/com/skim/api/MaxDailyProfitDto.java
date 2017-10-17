package com.skim.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skim.model.MaxDailyProfit;
import org.joda.time.LocalDate;


@JsonIgnoreProperties(ignoreUnknown = true)
public class MaxDailyProfitDto {
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    @JsonProperty
    private Float profit;

    public MaxDailyProfitDto(MaxDailyProfit mdp) {
        this.date = mdp.getDate();
        this.profit = mdp.getProfit();
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Float getProfit() {
        return profit;
    }

    public void setProfit(Float profit) {
        this.profit = profit;
    }
}
