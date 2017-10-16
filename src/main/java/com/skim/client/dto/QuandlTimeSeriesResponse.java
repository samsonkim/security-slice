package com.skim.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QuandlTimeSeriesResponse {
    @JsonProperty
    private QuandlTimeSeriesDataset dataset;

    public QuandlTimeSeriesDataset getDataset() {
        return dataset;
    }

    public void setDataset(QuandlTimeSeriesDataset dataset) {
        this.dataset = dataset;
    }

    @Override
    public String toString() {
        return "QuandlTimeSeriesResponse{" +
                "dataset=" + dataset +
                '}';
    }
}
