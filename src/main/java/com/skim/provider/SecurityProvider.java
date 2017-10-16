package com.skim.provider;

import com.skim.model.MonthlySecurityPrice;

import java.util.List;
import java.util.Map;

/*
    Also future refactoring will consist of adding support of being able to pass in other securities
    and start/end dates as parameters instead of basing it off of the default config
 */
public interface SecurityProvider {
    Map<String, List<MonthlySecurityPrice>> getMonthlyPrices();
}
