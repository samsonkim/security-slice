package com.skim.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.common.io.Resources;
import com.skim.client.dto.QuandlTimeSeriesCollapse;
import com.skim.client.dto.QuandlTimeSeriesDataset;
import com.skim.client.dto.QuandlTimeSeriesResponse;
import org.hamcrest.CoreMatchers;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static com.skim.utils.DateUtils.*;

/*
    Unit test for parsing out Quandl api responses
 */
public class QuandlClientImplTest {
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        objectMapper = QuandlClientImpl.OBJECT_MAPPER.registerModule(new JodaModule());
    }

    /*
        Verify fields we care about are accessible from response
     */
    @Test
    public void testParseDailyTimeSeriesResponse() throws IOException, URISyntaxException {
        URL url = Resources.getResource("json/test-quandl-daily-time-series.json");
        QuandlTimeSeriesResponse response = objectMapper.readValue(url, QuandlTimeSeriesResponse.class);

        assertNotNull(response);

        QuandlTimeSeriesDataset dataset = response.getDataset();

        assertEquals("WIKI", dataset.getDatabaseCode());
        assertEquals("FB", dataset.getDatasetCode());

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

        assertEquals("daily", dataset.getFrequency());
        assertEquals(QuandlTimeSeriesCollapse.DAILY.getName(), dataset.getCollapse());

        LocalDate startDate = QUANDL_DATE_FORMAT.parseLocalDate("2017-01-01");
        LocalDate endDate = QUANDL_DATE_FORMAT.parseLocalDate("2017-06-30");

        assertTrue(startDate.equals(dataset.getStartDate()));
        assertTrue(endDate.equals(dataset.getEndDate()));

        //verify column size matches each row
        int columnSize = dataset.getColumnNames().size();
        for(List<String> row: dataset.getData()){
            assertEquals(columnSize, row.size());
        }

        //verify rows
        assertEquals(125, dataset.getData().size());

        //Validate 1st row
        List<String> row = dataset.getData().get(0);
        assertEquals("2017-06-30", row.get(0));
        assertEquals("151.9", row.get(1));
        assertEquals("151.92", row.get(2));
        assertEquals("150.06", row.get(3));
        assertEquals("150.98", row.get(4));
        assertEquals("14540013", row.get(5));
        assertEquals("0", row.get(6));
        assertEquals("1", row.get(7));
        assertEquals("151.9", row.get(8));
        assertEquals("151.92", row.get(9));
        assertEquals("150.06", row.get(10));
        assertEquals("150.98", row.get(11));
        assertEquals("14540013", row.get(12));
    }

    @Test
    public void testParseMonthlyTimeSeriesResponse() throws IOException, URISyntaxException {
        URL url = Resources.getResource("json/test-quandl-monthly-time-series.json");
        QuandlTimeSeriesResponse response = objectMapper.readValue(url, QuandlTimeSeriesResponse.class);

        assertNotNull(response);

        QuandlTimeSeriesDataset dataset = response.getDataset();

        assertEquals("WIKI", dataset.getDatabaseCode());
        assertEquals("FB", dataset.getDatasetCode());

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

        assertEquals("daily", dataset.getFrequency());
        assertEquals(QuandlTimeSeriesCollapse.MONTHLY.getName(), dataset.getCollapse());

        LocalDate startDate = QUANDL_DATE_FORMAT.parseLocalDate("2017-01-01");
        LocalDate endDate = QUANDL_DATE_FORMAT.parseLocalDate("2017-06-30");

        assertTrue(startDate.equals(dataset.getStartDate()));
        assertTrue(endDate.equals(dataset.getEndDate()));

        //verify column size matches each row
        int columnSize = dataset.getColumnNames().size();
        for(List<String> row: dataset.getData()){
            assertEquals(columnSize, row.size());
        }

        //verify rows
        assertEquals(6, dataset.getData().size());
    }
}
