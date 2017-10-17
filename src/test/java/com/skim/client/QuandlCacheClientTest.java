package com.skim.client;

import com.skim.client.dto.QuandlTimeSeriesCollapse;
import com.skim.client.dto.QuandlTimeSeriesResponse;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static com.skim.utils.DateUtils.QUANDL_DATE_FORMAT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QuandlCacheClientTest {

    @Mock
    private QuandlClient client;

    private QuandlCacheClient instance;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        instance = new QuandlCacheClient(client);
    }

    @Test
    public void testTimeSeriesCacheSameKey() {
        String databaseCode = "testCode";
        String stockSymbol = "testStock";
        Optional<QuandlTimeSeriesCollapse> collapse = Optional.of(QuandlTimeSeriesCollapse.DAILY);
        Optional<LocalDate> startDate = Optional.of(QUANDL_DATE_FORMAT.parseLocalDate("2017-01-01"));
        Optional<LocalDate> endDate = Optional.of(QUANDL_DATE_FORMAT.parseLocalDate("2017-06-30"));

        QuandlTimeSeriesResponse mockResponse = new QuandlTimeSeriesResponse();
        when(client.getTimeSeries(databaseCode, stockSymbol, collapse, startDate, endDate))
                .thenReturn(mockResponse);

        QuandlTimeSeriesResponse response = instance.getTimeSeries(databaseCode, stockSymbol, collapse, startDate, endDate);

        assertNotNull(response);
        assertEquals(mockResponse, response);

        //call it again
        response = instance.getTimeSeries(databaseCode, stockSymbol, collapse, startDate, endDate);

        assertNotNull(response);
        assertEquals(mockResponse, response);

        verify(client).getTimeSeries(eq(databaseCode),
                eq(stockSymbol),
                eq(collapse),
                eq(startDate),
                eq(endDate)
        );
    }

    @Test
    public void testTimeSeriesCacheWithEmptyOptionalParams(){
        String databaseCode = "testCode";
        String stockSymbol = "testStock";
        Optional<QuandlTimeSeriesCollapse> collapse = Optional.of(QuandlTimeSeriesCollapse.DAILY);
        Optional<LocalDate> startDate = Optional.of(QUANDL_DATE_FORMAT.parseLocalDate("2017-01-01"));
        Optional<LocalDate> endDate = Optional.of(QUANDL_DATE_FORMAT.parseLocalDate("2017-06-30"));

        QuandlTimeSeriesResponse mockResponse1 = new QuandlTimeSeriesResponse();
        when(client.getTimeSeries(databaseCode, stockSymbol, collapse, startDate, endDate))
                .thenReturn(mockResponse1);

        QuandlTimeSeriesResponse response = instance.getTimeSeries(databaseCode, stockSymbol, collapse, startDate, endDate);

        assertNotNull(response);
        assertEquals(mockResponse1, response);

        //call with empty optional params - should not get cached value
        QuandlTimeSeriesResponse mockResponse2 = new QuandlTimeSeriesResponse();
        when(client.getTimeSeries(databaseCode, stockSymbol, Optional.empty(), Optional.empty(), Optional.empty()))
                .thenReturn(mockResponse2);

        response = instance.getTimeSeries(databaseCode, stockSymbol, Optional.empty(), Optional.empty(), Optional.empty());

        assertNotNull(response);
        assertEquals(mockResponse2, response);

        verify(client, times(2)).getTimeSeries(eq(databaseCode),
                eq(stockSymbol),
                any(Optional.class),
                any(Optional.class),
                any(Optional.class)
        );
    }

    @Test
    public void testTimeSeriesCacheForDifferentKeys() {
        String databaseCode = "testCode";
        String stockSymbol1 = "testStock";
        Optional<QuandlTimeSeriesCollapse> collapse = Optional.of(QuandlTimeSeriesCollapse.DAILY);
        Optional<LocalDate> startDate = Optional.of(QUANDL_DATE_FORMAT.parseLocalDate("2017-01-01"));
        Optional<LocalDate> endDate = Optional.of(QUANDL_DATE_FORMAT.parseLocalDate("2017-06-30"));

        QuandlTimeSeriesResponse mockResponse1 = new QuandlTimeSeriesResponse();
        when(client.getTimeSeries(databaseCode, stockSymbol1, collapse, startDate, endDate))
                .thenReturn(mockResponse1);

        QuandlTimeSeriesResponse response = instance.getTimeSeries(databaseCode, stockSymbol1, collapse, startDate, endDate);

        assertNotNull(response);
        assertEquals(mockResponse1, response);

        //call with different stockSymbol
        String stockSymbol2 = "testSymbol2";
        QuandlTimeSeriesResponse mockResponse2 = new QuandlTimeSeriesResponse();
        when(client.getTimeSeries(databaseCode, stockSymbol2, collapse, startDate, endDate))
                .thenReturn(mockResponse2);

        response = instance.getTimeSeries(databaseCode, stockSymbol2, collapse, startDate, endDate);

        assertNotNull(response);
        assertEquals(mockResponse2, response);

        verify(client, times(2)).getTimeSeries(eq(databaseCode),
                anyString(),
                eq(collapse),
                eq(startDate),
                eq(endDate)
        );
    }
}
