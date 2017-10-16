package com.skim.client;

import com.codahale.metrics.MetricRegistry;
import com.skim.client.dto.QuandlTimeSeriesCollapse;
import com.skim.client.dto.QuandlTimeSeriesDataset;
import com.skim.client.dto.QuandlTimeSeriesResponse;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.util.Duration;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

/*
    Integration test for calling out to Quandl API
 */
public class QuandlClientImplIntegrationTest {
    private QuandlClientImpl instance;
    private String databaseCode;
    private String stockSymbol;
    private SimpleDateFormat dateFormat;

    @Before
    public void setUp() throws Exception {
        URI uri = new URI("https://www.quandl.com");
        String apiKey = "s-GMZ_xkw6CrkGYUWs1p";
        databaseCode = "WIKI";
        stockSymbol = "COF";

        dateFormat = QuandlClientImpl.DATE_FORMAT;
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        JerseyClientConfiguration jerseyClientConfiguration = new JerseyClientConfiguration();
        jerseyClientConfiguration.setTimeout(Duration.seconds(5));

        Client client = new JerseyClientBuilder(new MetricRegistry())
                .using(jerseyClientConfiguration)
                .using(executorService, QuandlClientImpl.OBJECT_MAPPER)
                .build("testClient");

        instance = new QuandlClientImpl(uri, client, apiKey);
    }

    @Test
    public void testWithOptions() throws ParseException {
        Date startDate = dateFormat.parse("2017-01-01");
        Date endDate = dateFormat.parse("2017-06-30");

        QuandlTimeSeriesResponse response = instance.getTimeSeries(databaseCode,
                stockSymbol,
                Optional.of(QuandlTimeSeriesCollapse.DAILY),
                Optional.of(startDate),
                Optional.of(endDate));

        assertNotNull(response);

        QuandlTimeSeriesDataset dataset = response.getDataset();
        assertNotNull(dataset);
        assertEquals(databaseCode, dataset.getDatabaseCode());
        assertEquals(stockSymbol, dataset.getDatasetCode());
        assertEquals(QuandlTimeSeriesCollapse.DAILY.getName(), dataset.getCollapse());

        assertThat(dataset.getColumnNames(), CoreMatchers.hasItems(
                "Date",
                "Open",
                "High",
                "Low",
                "Close",
                "Volume",
                "Ex-Dividend",
                "Split Ratio",
                "Adj. Open",
                "Adj. High",
                "Adj. Low",
                "Adj. Close",
                "Adj. Volume"
        ));

        assertTrue(startDate.equals(dataset.getStartDate()));
        assertTrue(endDate.equals(dataset.getEndDate()));

        //verify column size matches each row
        int columnSize = dataset.getColumnNames().size();
        for (List<String> row : dataset.getData()) {
            assertEquals(columnSize, row.size());
        }
    }

    @Test
    public void testEmptyOptions() {
        QuandlTimeSeriesResponse response = instance.getTimeSeries(databaseCode,
                stockSymbol,
                Optional.empty(),
                Optional.empty(),
                Optional.empty());

        assertNotNull(response);

        QuandlTimeSeriesDataset dataset = response.getDataset();
        assertNotNull(dataset);
        assertEquals(databaseCode, dataset.getDatabaseCode());
        assertEquals(stockSymbol, dataset.getDatasetCode());

        assertThat(dataset.getColumnNames(), CoreMatchers.hasItems(
                "Date",
                "Open",
                "High",
                "Low",
                "Close",
                "Volume",
                "Ex-Dividend",
                "Split Ratio",
                "Adj. Open",
                "Adj. High",
                "Adj. Low",
                "Adj. Close",
                "Adj. Volume"
        ));

        //verify column size matches each row
        int columnSize = dataset.getColumnNames().size();
        for (List<String> row : dataset.getData()) {
            assertEquals(columnSize, row.size());
        }
    }
}