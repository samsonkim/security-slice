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

    public Set<String> getSecurities() {
        return securities;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
