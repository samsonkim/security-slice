package com.skim.provider;

import com.skim.client.QuandlClient;
import com.skim.client.dto.QuandlTimeSeriesColumn;
import com.skim.client.dto.QuandlTimeSeriesDataset;
import com.skim.client.dto.QuandlTimeSeriesResponse;
import com.skim.model.DailySecurityPrice;
import com.skim.model.MonthlySecurityPrice;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.skim.utils.DateUtils.QUANDL_DATE_FORMAT;
import static com.skim.utils.DateUtils.YEAR_MONTH_DATE_FORMAT;
import static com.skim.provider.QuandlSecurityProvider.COLLAPSE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QuandlSecurityProviderTest {

    @Mock
    private QuandlClient client;

    private QuandlSecurityProvider instance;
    private String databaseCode;
    private Set<String> defaultSecurities;
    private Optional<LocalDate> defaultStartDate;
    private Optional<LocalDate> defaultEndDate;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        databaseCode = "testDb";
        defaultSecurities = new HashSet<>(Arrays.asList("testA", "testB"));
        defaultStartDate = Optional.of(QUANDL_DATE_FORMAT.parseLocalDate("2017-01-01"));
        defaultEndDate = Optional.of(QUANDL_DATE_FORMAT.parseLocalDate("2017-06-30"));

        instance = new QuandlSecurityProvider(client,
                databaseCode, defaultSecurities, defaultStartDate, defaultEndDate);
    }

    /*
    Verify mapping function works correctly and resolves to adjustedOpen
    and adjustedClose not open or close
     */
    @Test
    public void testToDailyPrices() {
        QuandlTimeSeriesDataset dataset = createTestDataset();

        LocalDate startDate = QUANDL_DATE_FORMAT.parseLocalDate("2017-01-01");
        LocalDate startDate2 = QUANDL_DATE_FORMAT.parseLocalDate("2017-01-02");
        List<List<String>> rows = createRows(startDate, 2, 1, 10, 100, 1000);
        dataset.setData(rows);

        List<DailySecurityPrice> dailyPrices = instance.toDailyPrices(dataset);

        assertNotNull(dailyPrices);
        assertEquals(2, dailyPrices.size());

        assertThat(dailyPrices).extracting("date").containsOnly(startDate, startDate2);
        assertThat(dailyPrices).extracting("open").containsOnly(100f, 101f);
        assertThat(dailyPrices).extracting("close").containsOnly(1000f, 1001f);
    }

    /*
        Verify we can consolidate daily dataset to 2 months
     */
    @Test
    public void testToMonthlyPrices() {
        QuandlTimeSeriesDataset dataset = createTestDataset();

        LocalDate month1Date = QUANDL_DATE_FORMAT.parseLocalDate("2017-01-02");

        List<List<String>> rows1 = createRows(month1Date, 3, 1, 10, 100, 1000);

        LocalDate month2Date = QUANDL_DATE_FORMAT.parseLocalDate("2017-03-02");
        List<List<String>> rows2 = createRows(month2Date, 3, 3, 30, 300, 3000);

        List<List<String>> data = Stream.concat(rows1.stream(), rows2.stream())
                .collect(Collectors.toList());
        dataset.setData(data);

        List<DailySecurityPrice> dailyPrices = instance.toDailyPrices(dataset);
        assertEquals(6, dailyPrices.size());

        List<MonthlySecurityPrice> monthlyPrices = instance.toMonthlyPrices(dailyPrices);

        assertNotNull(monthlyPrices);
        assertEquals(2, monthlyPrices.size());

        LocalDate expectedMonth1 = YEAR_MONTH_DATE_FORMAT.parseLocalDate(YEAR_MONTH_DATE_FORMAT.print(month1Date));
        LocalDate expectedMonth2 = YEAR_MONTH_DATE_FORMAT.parseLocalDate(YEAR_MONTH_DATE_FORMAT.print(month2Date));

        assertThat(monthlyPrices).extracting("monthYearDate").containsOnly(expectedMonth1, expectedMonth2);
        assertThat(monthlyPrices).extracting("averageOpen").containsOnly(301f, 101f);
        assertThat(monthlyPrices).extracting("averageClose").containsOnly(3001f, 1001f);
    }

    /*
        End to end integration testing we can process multiple securities
     */
    @Test
    public void testGetMonthlyPrices() {
        QuandlTimeSeriesDataset dataset1 = createTestDataset();
        LocalDate startDate1 = QUANDL_DATE_FORMAT.parseLocalDate("2017-01-02");
        List<List<String>> rows1 = createRows(startDate1, 2, 1, 10, 100, 1000);
        dataset1.setData(rows1);

        when(client.getTimeSeries(databaseCode, "testA", COLLAPSE, defaultStartDate, defaultEndDate))
                .thenReturn(new QuandlTimeSeriesResponse(dataset1));

        QuandlTimeSeriesDataset dataset2 = createTestDataset();
        LocalDate startDate2 = QUANDL_DATE_FORMAT.parseLocalDate("2017-05-02");
        List<List<String>> rows2 = createRows(startDate2, 5, 5, 50, 500, 5000);
        dataset2.setData(rows2);

        when(client.getTimeSeries(databaseCode, "testB", COLLAPSE, defaultStartDate, defaultEndDate))
                .thenReturn(new QuandlTimeSeriesResponse(dataset2));

        Map<String, List<MonthlySecurityPrice>> response = instance.getMonthlyPrices();
        assertNotNull(response);
        assertEquals(2, response.keySet().size());

        for(String sec : response.keySet()){
            assertTrue(defaultSecurities.contains(sec));
        }

        verify(client, times(2))
                .getTimeSeries(eq(databaseCode),
                        anyString(),
                        eq(COLLAPSE),
                        eq(defaultStartDate),
                        eq(defaultEndDate));

        List<MonthlySecurityPrice> monthlyPrices = response.get("testA");
        assertEquals(1, monthlyPrices.get(0).getMonthYearDate().getMonthOfYear());

        monthlyPrices = response.get("testB");
        assertEquals(5, monthlyPrices.get(0).getMonthYearDate().getMonthOfYear());
    }

    private QuandlTimeSeriesDataset createTestDataset() {
        QuandlTimeSeriesDataset dataset = new QuandlTimeSeriesDataset();

        dataset.setColumnNames(Arrays.asList(QuandlTimeSeriesColumn.DATE.getColumn(),
                QuandlTimeSeriesColumn.OPEN.getColumn(),
                QuandlTimeSeriesColumn.CLOSE.getColumn(),
                QuandlTimeSeriesColumn.ADJ_OPEN.getColumn(),
                QuandlTimeSeriesColumn.ADJ_CLOSE.getColumn()));

        return dataset;
    }

    private List<List<String>> createRows(LocalDate startDate,
                                          int days,
                                          float startOpen,
                                          float startClose,
                                          float adjOpen,
                                          float adjClose) {
        List<List<String>> rows = new ArrayList<>();

        for (int i = 0; i < days; i++) {
            List columns = Arrays.asList(QUANDL_DATE_FORMAT.print(startDate.plusDays(i)),
                    String.valueOf(startOpen + i),
                    String.valueOf(startClose + i),
                    String.valueOf(adjOpen + i),
                    String.valueOf(adjClose + i));
            rows.add(columns);
        }

        return rows;
    }
}
