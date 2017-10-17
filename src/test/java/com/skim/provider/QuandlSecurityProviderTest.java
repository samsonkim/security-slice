package com.skim.provider;

import com.skim.client.QuandlClient;
import com.skim.client.dto.QuandlTimeSeriesColumn;
import com.skim.client.dto.QuandlTimeSeriesDataset;
import com.skim.client.dto.QuandlTimeSeriesResponse;
import com.skim.configuration.QuandlConfiguration;
import com.skim.configuration.SecurityProviderConfiguration;
import com.skim.model.*;
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
    private int busyDayThreshold;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        databaseCode = "testDb";
        defaultSecurities = new HashSet<>(Arrays.asList("testA", "testB"));
        defaultStartDate = Optional.of(QUANDL_DATE_FORMAT.parseLocalDate("2017-01-01"));
        defaultEndDate = Optional.of(QUANDL_DATE_FORMAT.parseLocalDate("2017-06-30"));
        busyDayThreshold = 10;

        QuandlConfiguration quandlConfiguration = new QuandlConfiguration();
        quandlConfiguration.setDatabaseCode(databaseCode);

        SecurityProviderConfiguration securityProviderConfiguration = new SecurityProviderConfiguration();
        securityProviderConfiguration.setSecurities(defaultSecurities);
        securityProviderConfiguration.setStartDate(defaultStartDate.get());
        securityProviderConfiguration.setEndDate(defaultEndDate.get());
        securityProviderConfiguration.setBusyDayThreshold(busyDayThreshold);

        instance = new QuandlSecurityProvider(client, quandlConfiguration, securityProviderConfiguration);
    }

    /*
    Verify mapping function works correctly and resolves to adjusted columns
    (adj open, adj close, adj low, adj high, and adj volume)
     */
    @Test
    public void testToDailyPrices() {
        QuandlTimeSeriesDataset dataset = createTestDataset();

        LocalDate startDate = QUANDL_DATE_FORMAT.parseLocalDate("2017-01-01");
        LocalDate startDate2 = QUANDL_DATE_FORMAT.parseLocalDate("2017-01-02");

        int days = 2;
        float startOpen = 1;
        float startHigh = 0;
        float startLow = 0;
        float startClose = 10;
        long startVolume = 0;
        float adjOpen = 100;
        float adjHigh = 0;
        float adjLow = 0;
        float adjClose = 1000;
        long adjVolume = 0;

        List<List<String>> rows = createRows(startDate,
                days,
                startOpen,
                startHigh,
                startLow,
                startClose,
                startVolume,
                adjOpen,
                adjHigh,
                adjLow,
                adjClose,
                adjVolume);
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

        int days = 3;
        float startOpen = 1;
        float startHigh = 0;
        float startLow = 0;
        float startClose = 10;
        long startVolume = 0;
        float adjOpen = 100;
        float adjHigh = 0;
        float adjLow = 0;
        float adjClose = 1000;
        long adjVolume = 0;
        List<List<String>> rows1 = createRows(month1Date,
                days,
                startOpen,
                startHigh,
                startLow,
                startClose,
                startVolume,
                adjOpen,
                adjHigh,
                adjLow,
                adjClose,
                adjVolume);


        LocalDate month2Date = QUANDL_DATE_FORMAT.parseLocalDate("2017-03-02");
        int days2 = 3;
        float startOpen2 = 3;
        float startHigh2 = 0;
        float startLow2 = 0;
        float startClose2 = 30;
        long startVolume2 = 0;
        float adjOpen2 = 300;
        float adjHigh2 = 0;
        float adjLow2 = 0;
        float adjClose2 = 3000;
        long adjVolume2 = 0;
        List<List<String>> rows2 = createRows(month2Date,
                days2,
                startOpen2,
                startHigh2,
                startLow2,
                startClose2,
                startVolume2,
                adjOpen2,
                adjHigh2,
                adjLow2,
                adjClose2,
                adjVolume2);

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
        int days = 2;
        float startOpen = 1;
        float startHigh = 0;
        float startLow = 0;
        float startClose = 10;
        long startVolume = 0;
        float adjOpen = 100;
        float adjHigh = 0;
        float adjLow = 0;
        float adjClose = 1000;
        long adjVolume = 0;
        List<List<String>> rows1 = createRows(startDate1,
                days,
                startOpen,
                startHigh,
                startLow,
                startClose,
                startVolume,
                adjOpen,
                adjHigh,
                adjLow,
                adjClose,
                adjVolume);
        dataset1.setData(rows1);

        when(client.getTimeSeries(databaseCode, "testA", COLLAPSE, defaultStartDate, defaultEndDate))
                .thenReturn(new QuandlTimeSeriesResponse(dataset1));

        QuandlTimeSeriesDataset dataset2 = createTestDataset();
        LocalDate startDate2 = QUANDL_DATE_FORMAT.parseLocalDate("2017-05-02");
        int days2 = 5;
        float startOpen2 = 5;
        float startHigh2 = 0;
        float startLow2 = 0;
        float startClose2 = 50;
        long startVolume2 = 0;
        float adjOpen2 = 500;
        float adjHigh2 = 0;
        float adjLow2 = 0;
        float adjClose2 = 5000;
        long adjVolume2 = 0;
        List<List<String>> rows2 = createRows(startDate2,
                days2,
                startOpen2,
                startHigh2,
                startLow2,
                startClose2,
                startVolume2,
                adjOpen2,
                adjHigh2,
                adjLow2,
                adjClose2,
                adjVolume2);
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

    @Test
    public void testToMaxDailyProfit(){
        LocalDate startDate = QUANDL_DATE_FORMAT.parseLocalDate("2017-01-02");

        List<DailySecurityPrice> prices = new ArrayList<>();
        prices.add(new DailySecurityPrice(startDate, 0, 0, 2, 1, 0));
        prices.add(new DailySecurityPrice(startDate.plusDays(1), 0, 0, 4, 2, 0));
        prices.add(new DailySecurityPrice(startDate.plusDays(2), 0, 0, 32, 5, 0));
        prices.add(new DailySecurityPrice(startDate.plusDays(3), 0, 0, 16, 9, 0));
        prices.add(new DailySecurityPrice(startDate.plusDays(4), 0, 0, 8, 4, 0));

        MaxDailyProfit result = instance.toMaxDailyProfit(prices);

        assertNotNull(result);
        assertEquals(startDate.plusDays(2), result.getDate());
        assertTrue(27.0f == result.getProfit());
    }

    @Test
    public void testToBusyDayResponse(){
        LocalDate startDate = QUANDL_DATE_FORMAT.parseLocalDate("2017-01-02");

        List<DailySecurityPrice> prices = new ArrayList<>();
        prices.add(new DailySecurityPrice(startDate, 0, 0, 0, 0, 1000));
        prices.add(new DailySecurityPrice(startDate.plusDays(1), 0, 0, 0, 0, 2000));
        prices.add(new DailySecurityPrice(startDate.plusDays(2), 0, 0, 0, 0, 4000));
        prices.add(new DailySecurityPrice(startDate.plusDays(3), 0, 0, 0, 0, 6000));
        prices.add(new DailySecurityPrice(startDate.plusDays(4), 0, 0, 0, 0, 8000));

        BusyDayResponse result = instance.toBusyDayResponse(prices);

        assertNotNull(result);
        assertTrue(4200L == result.getAverageVolume());
        assertTrue(2 == result.getDays().size());
        assertThat(result.getDays()).extracting("date").containsOnly(startDate.plusDays(3), startDate.plusDays(4));
        assertThat(result.getDays()).extracting("volume").containsOnly(6000L, 8000L);
    }

    @Test
    public void testGetBiggestLoserCount(){
        LocalDate startDate = QUANDL_DATE_FORMAT.parseLocalDate("2017-01-02");

        List<DailySecurityPrice> prices = new ArrayList<>();
        prices.add(new DailySecurityPrice(startDate, 2, 2, 0, 0, 0));
        prices.add(new DailySecurityPrice(startDate.plusDays(1), 3, 2, 0, 0, 0));
        prices.add(new DailySecurityPrice(startDate.plusDays(2), 4, 6, 0, 0, 0));
        prices.add(new DailySecurityPrice(startDate.plusDays(3), 6, 5, 0, 0, 0));
        prices.add(new DailySecurityPrice(startDate.plusDays(4), 5, 4, 0, 0, 0));

        long result = instance.getBiggestLoserCount(prices);

        assertEquals(3, result);
    }

    private QuandlTimeSeriesDataset createTestDataset() {
        QuandlTimeSeriesDataset dataset = new QuandlTimeSeriesDataset();

        dataset.setColumnNames(Arrays.asList(QuandlTimeSeriesColumn.DATE.getColumn(),
                QuandlTimeSeriesColumn.OPEN.getColumn(),
                QuandlTimeSeriesColumn.HIGH.getColumn(),
                QuandlTimeSeriesColumn.LOW.getColumn(),
                QuandlTimeSeriesColumn.CLOSE.getColumn(),
                QuandlTimeSeriesColumn.VOLUME.getColumn(),
                QuandlTimeSeriesColumn.ADJ_OPEN.getColumn(),
                QuandlTimeSeriesColumn.ADJ_HIGH.getColumn(),
                QuandlTimeSeriesColumn.ADJ_LOW.getColumn(),
                QuandlTimeSeriesColumn.ADJ_CLOSE.getColumn(),
                QuandlTimeSeriesColumn.ADJ_VOLUME.getColumn()
        ));

        return dataset;
    }

    private List<List<String>> createRows(LocalDate startDate,
                                          int days,
                                          float startOpen,
                                          float startHigh,
                                          float startLow,
                                          float startClose,
                                          long startVolume,
                                          float adjOpen,
                                          float adjHigh,
                                          float adjLow,
                                          float adjClose,
                                          long adjVolume) {
        List<List<String>> rows = new ArrayList<>();

        for (int i = 0; i < days; i++) {
            List columns = Arrays.asList(QUANDL_DATE_FORMAT.print(startDate.plusDays(i)),
                    String.valueOf(startOpen + i),
                    String.valueOf(startHigh + i),
                    String.valueOf(startLow + i),
                    String.valueOf(startClose + i),
                    String.valueOf(startVolume + i),
                    String.valueOf(adjOpen + i),
                    String.valueOf(adjHigh + i),
                    String.valueOf(adjLow + i),
                    String.valueOf(adjClose + i),
                    String.valueOf(adjVolume + i));
            rows.add(columns);
        }

        return rows;
    }
}
