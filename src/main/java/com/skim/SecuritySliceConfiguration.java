package com.skim;

import com.skim.configuration.QuandlConfiguration;
import com.skim.configuration.SecurityProviderConfiguration;
import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.*;
import javax.validation.constraints.*;

public class SecuritySliceConfiguration extends Configuration {
    @JsonProperty
    private SecurityProviderConfiguration securityProvider;

    @JsonProperty
    private QuandlConfiguration quandl;

    public SecurityProviderConfiguration getSecurityProvider() {
        return securityProvider;
    }

    public QuandlConfiguration getQuandl() {
        return quandl;
    }
}
