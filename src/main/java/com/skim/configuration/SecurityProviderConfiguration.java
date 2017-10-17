package com.skim.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.LocalDate;

import java.util.Set;

public class SecurityProviderConfiguration {
    @JsonProperty
    private Set<String> securities;

    @JsonProperty
    private LocalDate startDate;

    @JsonProperty
    private LocalDate endDate;

    @JsonProperty
    private int busyDayThreshold;


    public Set<String> getSecurities() {
        return securities;
    }

    public void setSecurities(Set<String> securities) {
        this.securities = securities;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getBusyDayThreshold() {
        return busyDayThreshold;
    }

    public void setBusyDayThreshold(int busyDayThreshold) {
        this.busyDayThreshold = busyDayThreshold;
    }
}
