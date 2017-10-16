package com.skim.client;

import com.skim.client.dto.QuandlTimeSeriesCollapse;
import com.skim.client.dto.QuandlTimeSeriesResponse;

import java.util.Date;
import java.util.Optional;

public interface QuandlClient {
    QuandlTimeSeriesResponse getTimeSeries(String databaseCode,
                                           String stockSymbol,
                                           Optional<QuandlTimeSeriesCollapse> collapse,
                                           Optional<Date> startDate,
                                           Optional<Date> endDate);
}
