package com.skim.client;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.skim.client.dto.QuandlCacheTimeSeriesKey;
import com.skim.client.dto.QuandlTimeSeriesCollapse;
import com.skim.client.dto.QuandlTimeSeriesResponse;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class QuandlCacheClient implements QuandlClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuandlCacheClient.class);

    private final Cache<QuandlCacheTimeSeriesKey, QuandlTimeSeriesResponse> cache;
    private final QuandlClient quandlClient;

    public QuandlCacheClient(QuandlClient quandlClient) {
        this.quandlClient = quandlClient;
        this.cache = CacheBuilder.newBuilder()
                //expire cache after day since dataset is based on daily interval
                .expireAfterWrite(1, TimeUnit.DAYS)
                .build();
    }


    @Override
    public QuandlTimeSeriesResponse getTimeSeries(String databaseCode,
                                                  String stockSymbol,
                                                  Optional<QuandlTimeSeriesCollapse> collapse,
                                                  Optional<LocalDate> startDate,
                                                  Optional<LocalDate> endDate) {
        final QuandlCacheTimeSeriesKey key = new QuandlCacheTimeSeriesKey(databaseCode,
                stockSymbol, collapse, startDate, endDate);

        try {
            return cache.get(key, () -> quandlClient.getTimeSeries(key.getDatabaseCode(),
                    key.getStockSymbol(),
                    key.getCollapse(),
                    key.getStartDate(),
                    key.getEndDate()));
        } catch (ExecutionException e) {
            LOGGER.error("Unable to get time series for " + key, e);
            //Have it bubble up to main app since no recovery rules set for now
            throw new RuntimeException("Unable to get time series for " + key, e);
        }
    }
}
