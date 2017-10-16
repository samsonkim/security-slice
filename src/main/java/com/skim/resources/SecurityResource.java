package com.skim.resources;

import com.skim.model.MonthlySecurityPrice;
import com.skim.provider.SecurityProvider;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

@Path("/api/securities")
@Produces(MediaType.APPLICATION_JSON)
public class SecurityResource {
    private final SecurityProvider securityProvider;

    public SecurityResource(SecurityProvider securityProvider) {
        this.securityProvider = securityProvider;
    }

    @GET
    public Map<String, List<MonthlySecurityPrice>> getMonthlyPrices(){
        return securityProvider.getMonthlyPrices();
    }
}
