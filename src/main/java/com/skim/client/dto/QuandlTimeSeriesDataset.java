package com.skim.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.LocalDate;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QuandlTimeSeriesDataset {

    @JsonProperty("database_code")
    private String databaseCode;    //should be WIKI db

    @JsonProperty("dataset_code")
    private String datasetCode;     //Stock symbol

    @JsonProperty
    private String name;

    @JsonProperty("column_names")   //lists columns used in data block
    private List<String> columnNames;

    @JsonProperty
    private String frequency;

    @JsonProperty("start_date")
    private LocalDate startDate;

    @JsonProperty("end_date")
    private LocalDate endDate;

    @JsonProperty
    private List<List<String>> data;

    @JsonProperty
    private String collapse;

    public String getDatasetCode() {
        return datasetCode;
    }

    public void setDatasetCode(String datasetCode) {
        this.datasetCode = datasetCode;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCollapse() {
        return collapse;
    }

    public void setCollapse(String collapse) {
        this.collapse = collapse;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
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

    public List<List<String>> getData() {
        return data;
    }

    public void setData(List<List<String>> data) {
        this.data = data;
    }


    public String getDatabaseCode() {
        return databaseCode;
    }

    public void setDatabaseCode(String databaseCode) {
        this.databaseCode = databaseCode;
    }

    @Override
    public String toString() {
        return "QuandlTimeSeriesDataset{" +
                "databaseCode='" + databaseCode + '\'' +
                ", datasetCode='" + datasetCode + '\'' +
                ", name='" + name + '\'' +
                ", columnNames=" + columnNames +
                ", frequency='" + frequency + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", data=" + data +
                ", collapse='" + collapse + '\'' +
                '}';
    }
}
