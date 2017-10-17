package com.skim.resources;

import com.skim.api.BiggestLoserDto;
import com.skim.api.BusyDayResponseDto;
import com.skim.api.MaxDailyProfitDto;
import com.skim.api.MonthlySecurityPriceDto;
import com.skim.provider.SecurityProvider;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/api/securities")
@Produces(MediaType.APPLICATION_JSON)
public class SecurityResource {
    private final SecurityProvider securityProvider;

    public SecurityResource(SecurityProvider securityProvider) {
        this.securityProvider = securityProvider;
    }

    @GET
    public Map<String, List<MonthlySecurityPriceDto>> getMonthlyPrices(){
        return securityProvider.getMonthlyPrices()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(e -> e.getKey(),
                        e -> e.getValue()
                                .stream()
                                .map(msp -> new MonthlySecurityPriceDto(msp))
                                .collect(Collectors.toList())));
    }

    @GET
    @Path("max-daily-profit")
    public Map<String, MaxDailyProfitDto> getMaxDailyProfit(){
        return securityProvider.getMaxDailyProfit()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(e -> e.getKey(),
                        e -> new MaxDailyProfitDto(e.getValue())));
    }

    @GET
    @Path("busy-days")
    public Map<String, BusyDayResponseDto> getBusyDays(){
        return securityProvider.getBusyDays()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(e -> e.getKey(),
                        e -> new BusyDayResponseDto(e.getValue())));
    }

    @GET
    @Path("biggest-loser")
    public BiggestLoserDto getBiggestLoser(){
        return new BiggestLoserDto(securityProvider.getBiggestLoser());
    }
}
