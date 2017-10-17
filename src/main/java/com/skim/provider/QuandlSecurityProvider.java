package com.skim.provider;

import com.skim.client.QuandlClient;
import com.skim.client.dto.QuandlTimeSeriesCollapse;
import com.skim.client.dto.QuandlTimeSeriesColumn;
import com.skim.client.dto.QuandlTimeSeriesDataset;
import com.skim.client.dto.QuandlTimeSeriesResponse;
import com.skim.configuration.QuandlConfiguration;
import com.skim.configuration.SecurityProviderConfiguration;
import com.skim.model.DailySecurityPrice;
import com.skim.model.MonthlySecurityPrice;
import org.apache.commons.math3.util.Precision;
import org.joda.time.LocalDate;

import java.util.*;
import java.util.stream.Collectors;

import static com.skim.utils.DateUtils.*;
import static java.util.stream.Collectors.groupingBy;

/*
    This security provider will key off of adjusted columns to factor in corporate actions
    prices(adj open, adj close, adj low, adj high, and adj volume)

    Also future refactoring will consist of adding support of being able to pass in other securities
    and start/end dates as parameters instead of basing it off of the default config
 */
public class QuandlSecurityProvider implements SecurityProvider {
    protected static final Optional<QuandlTimeSeriesCollapse> COLLAPSE = Optional.of(QuandlTimeSeriesCollapse.DAILY);
    private static final Comparator<MonthlySecurityPrice> MONTHLY_SECURITY_PRICE_COMPARATOR =
            Comparator.comparing(MonthlySecurityPrice::getMonthYearDate);

    private final QuandlClient quandlClient;
    private final String databaseCode;
    private final Set<String> defaultSecurities;
    private final Optional<LocalDate> defaultStartDate;
    private final Optional<LocalDate> defaultEndDate;
    private final int busyDayThreshold;

    public QuandlSecurityProvider(QuandlClient quandlClient,
                                  QuandlConfiguration quandlConfiguration,
                                  SecurityProviderConfiguration securityProviderConfiguration
                                  ) {
        this.quandlClient = quandlClient;
        this.databaseCode = quandlConfiguration.getDatabaseCode();
        this.defaultSecurities = securityProviderConfiguration.getSecurities();
        this.defaultStartDate = Optional.ofNullable(securityProviderConfiguration.getStartDate());
        this.defaultEndDate = Optional.ofNullable(securityProviderConfiguration.getEndDate());
        this.busyDayThreshold = securityProviderConfiguration.getBusyDayThreshold();
    }

    @Override
    public Map<String, List<MonthlySecurityPrice>> getMonthlyPrices(){
        Map<String, List<MonthlySecurityPrice>> monthlyPriceMap = new HashMap<>();

        for(String stockSymbol : defaultSecurities) {
            QuandlTimeSeriesResponse response = quandlClient.getTimeSeries(databaseCode,
                    stockSymbol, COLLAPSE, defaultStartDate, defaultEndDate);
            monthlyPriceMap.put(stockSymbol, toMonthlyPrices(toDailyPrices(response.getDataset())));
        }

        return monthlyPriceMap;
    }

    protected List<MonthlySecurityPrice> toMonthlyPrices(List<DailySecurityPrice> dailyPrices){
        //Date represented as month/year as key
        Map<LocalDate, List<DailySecurityPrice>> monthlyPriceMap = dailyPrices
                .parallelStream()
                .collect(groupingBy(dsp -> {
                    String tmp = YEAR_MONTH_DATE_FORMAT.print(dsp.getDate());
                    return YEAR_MONTH_DATE_FORMAT.parseLocalDate(tmp);
                }));

        return monthlyPriceMap.keySet()
                .parallelStream()
                .map(month -> {
                    List<DailySecurityPrice> prices = monthlyPriceMap.get(month);

                    Double averageOpen = prices.parallelStream()
                            .mapToDouble(DailySecurityPrice::getOpen)
                            .average()
                            .getAsDouble();

                    Double averageClose = prices.parallelStream()
                            .mapToDouble(DailySecurityPrice::getClose)
                            .average()
                            .getAsDouble();

                    return new MonthlySecurityPrice(month,
                            Precision.round(averageOpen.floatValue(), 2),
                            Precision.round(averageClose.floatValue(), 2));
                })
                .sorted(MONTHLY_SECURITY_PRICE_COMPARATOR)
                .collect(Collectors.toList());
    }

    /*
        Converts Quandl response to our model of daily prices
     */
    protected List<DailySecurityPrice> toDailyPrices(QuandlTimeSeriesDataset dataset){
        List<String> columns = dataset.getColumnNames();

        int dateIndex = columns.indexOf(QuandlTimeSeriesColumn.DATE.getColumn());
        int adjOpenIndex = columns.indexOf(QuandlTimeSeriesColumn.ADJ_OPEN.getColumn());
        int adjCloseIndex = columns.indexOf(QuandlTimeSeriesColumn.ADJ_CLOSE.getColumn());
        int adjHighIndex = columns.indexOf(QuandlTimeSeriesColumn.ADJ_OPEN.getColumn());
        int adjLowIndex = columns.indexOf(QuandlTimeSeriesColumn.ADJ_LOW.getColumn());
        int adjVolumeIndex = columns.indexOf(QuandlTimeSeriesColumn.ADJ_VOLUME.getColumn());

        //Date represented as month/year as key
        List<DailySecurityPrice> dailyPrices = dataset.getData()
                .parallelStream()
                .map(r -> {
                    LocalDate date = QUANDL_DATE_FORMAT.parseLocalDate(r.get(dateIndex));
                    float adjustedOpen = Float.parseFloat(r.get(adjOpenIndex));
                    float adjustedClose = Float.parseFloat(r.get(adjCloseIndex));
                    float adjustedHigh = Float.parseFloat(r.get(adjHighIndex));
                    float adjustedLow = Float.parseFloat(r.get(adjLowIndex));
                    long adjustedVolume = Long.parseLong(r.get(adjVolumeIndex));

                    return new DailySecurityPrice(date,
                            adjustedOpen,
                            adjustedClose,
                            adjustedHigh,
                            adjustedLow,
                            adjustedVolume);
                }).collect(Collectors.toList());

        return dailyPrices;
    }
}