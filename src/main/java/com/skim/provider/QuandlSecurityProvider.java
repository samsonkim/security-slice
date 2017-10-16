package com.skim.provider;

import com.skim.client.QuandlClient;
import com.skim.client.dto.QuandlTimeSeriesCollapse;
import com.skim.client.dto.QuandlTimeSeriesColumn;
import com.skim.client.dto.QuandlTimeSeriesDataset;
import com.skim.client.dto.QuandlTimeSeriesResponse;
import com.skim.model.DailySecurityPrice;
import com.skim.model.MonthlySecurityPrice;
import com.skim.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.skim.utils.DateUtils.*;
import static java.util.stream.Collectors.groupingBy;

/*
    This security provider will key off of adjusted prices(adj open, adj close) to factor in corporate actions
 */
public class QuandlSecurityProvider implements SecurityProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuandlSecurityProvider.class);
    private static final Optional<QuandlTimeSeriesCollapse> COLLAPSE = Optional.of(QuandlTimeSeriesCollapse.DAILY);

    private final QuandlClient quandlClient;
    private final String databaseCode;
    private final Set<String> defaultSecurities;
    private final Optional<Date> defaultStartDate;
    private final Optional<Date> defaultEndDate;

    public QuandlSecurityProvider(QuandlClient quandlClient, String databaseCode, Set<String> defaultSecurities, Optional<Date> defaultStartDate, Optional<Date> defaultEndDate) {
        this.quandlClient = quandlClient;
        this.databaseCode = databaseCode;
        this.defaultSecurities = defaultSecurities;
        this.defaultStartDate = defaultStartDate;
        this.defaultEndDate = defaultEndDate;
    }

    @Override
    public Map<String, List<MonthlySecurityPrice>> getMonthlyPrices(){
        Map<String, List<MonthlySecurityPrice>> monthlyPriceMap = new HashMap<>();

        for(String stockSymbol : defaultSecurities) {
            QuandlTimeSeriesResponse response = quandlClient.getTimeSeries(databaseCode,
                    stockSymbol, COLLAPSE, defaultStartDate, defaultEndDate);

            //calculate aggregates

            monthlyPriceMap.put(stockSymbol, toMonthlyPrices(response.getDataset()));
        }

        return monthlyPriceMap;
    }

    protected static List<MonthlySecurityPrice> toMonthlyPrices(QuandlTimeSeriesDataset dataset){
        List<MonthlySecurityPrice> monthlyPrices = new ArrayList<>();

        Map<Date, List<DailySecurityPrice>> dailyPriceMap = toDailyPrices(dataset);

        for(Date monthDate: dailyPriceMap.keySet()){
            List<DailySecurityPrice> dailyPrices = dailyPriceMap.get(monthDate);

            
        }


        return monthlyPrices;
    }

    /*
        Return daily prices grouped by month
     */
    protected static Map<Date, List<DailySecurityPrice>> toDailyPrices(QuandlTimeSeriesDataset dataset){
        List<String> columns = dataset.getColumnNames();

        int dateIndex = columns.indexOf(QuandlTimeSeriesColumn.DATE.getColumn());
        int adjOpenIndex = columns.indexOf(QuandlTimeSeriesColumn.ADJ_OPEN.getColumn());
        int adjCloseIndex = columns.indexOf(QuandlTimeSeriesColumn.ADJ_CLOSE.getColumn());

        //Date represented as month/year as key
        Map<Date, List<DailySecurityPrice>> dailyPrices = dataset.getData()
                .stream()
                .map(r -> {
                    try {
                        Date date = QUANDL_DATE_FORMAT.parse(r.get(dateIndex));
                        float adjustedOpen = Float.parseFloat(r.get(adjOpenIndex));
                        float adjustedClose = Float.parseFloat(r.get(adjCloseIndex));
                        return new DailySecurityPrice(date, adjustedOpen, adjustedClose);
                    } catch (ParseException e) {
                        LOGGER.warn("Unable to parse record " + r, e);
                    }
                    return null;
                })
                .filter(dsp -> dsp != null)
                .collect(groupingBy(dsp -> {
                    String tmp = QUANDL_DATE_FORMAT.format(dsp.getDate());
                    try {
                        return YEAR_MONTH_DATE_FORMAT.parse(tmp.substring(0, tmp.length()-4));
                    } catch (ParseException e) {
                        LOGGER.warn("Unable to group by month year for " + dsp, e);
                        return dsp.getDate();          //shouldn't happen but return original date
                    }
                }));

        return dailyPrices;
    }
}
