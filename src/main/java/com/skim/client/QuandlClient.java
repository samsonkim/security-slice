package com.skim.client;

import com.skim.client.dto.QuandlTimeSeriesCollapse;
import com.skim.client.dto.QuandlTimeSeriesResponse;
import org.joda.time.LocalDate;

import java.util.Optional;

public interface QuandlClient {
    QuandlTimeSeriesResponse getTimeSeries(String databaseCode,
                                           String stockSymbol,
                                           Optional<QuandlTimeSeriesCollapse> collapse,
                                           Optional<LocalDate> startDate,
                                           Optional<LocalDate> endDate);

}
