package com.skim.provider;

import com.skim.model.BiggestLoser;
import com.skim.model.BusyDayResponse;
import com.skim.model.MaxDailyProfit;
import com.skim.model.MonthlySecurityPrice;

import java.util.List;
import java.util.Map;

/*
    Also future refactoring will consist of adding support of being able to pass in other securities
    and start/end dates as parameters instead of basing it off of the default config
 */
public interface SecurityProvider {
    //get monthly prices for default securities
    Map<String, List<MonthlySecurityPrice>> getMonthlyPrices();

    //get max daily profit for default securities
    Map<String, MaxDailyProfit> getMaxDailyProfit();

    //get busiest days for default securities
    Map<String, BusyDayResponse> getBusyDays();

    //get the security with the most days where closing price is lower than open price
    BiggestLoser getBiggestLoser();
}
