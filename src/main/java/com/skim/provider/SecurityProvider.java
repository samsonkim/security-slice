package com.skim.provider;

import com.skim.model.MonthlySecurityPrice;

import java.util.List;
import java.util.Map;

public interface SecurityProvider {
    Map<String, List<MonthlySecurityPrice>> getMonthlyPrices();
}
