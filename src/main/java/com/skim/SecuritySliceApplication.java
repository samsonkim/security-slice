package com.skim;

import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.skim.client.QuandlClient;
import com.skim.client.QuandlClientImpl;
import com.skim.configuration.QuandlConfiguration;
import com.skim.configuration.SecurityProviderConfiguration;
import com.skim.provider.QuandlSecurityProvider;
import com.skim.provider.SecurityProvider;
import com.skim.resources.SecurityResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.net.URI;
import java.util.Optional;

public class SecuritySliceApplication extends Application<SecuritySliceConfiguration> {

    public static void main(final String[] args) throws Exception {
        new SecuritySliceApplication().run(args);
    }

    @Override
    public String getName() {
        return "SecuritySlice";
    }

    @Override
    public void initialize(final Bootstrap<SecuritySliceConfiguration> bootstrap) {
    }

    @Override
    public void run(final SecuritySliceConfiguration configuration,
                    final Environment environment) {

        environment.getObjectMapper().registerModule(new JodaModule());

        QuandlConfiguration quandlConfiguration = configuration.getQuandl();
        SecurityProviderConfiguration securityProviderConfiguration = configuration.getSecurityProvider();

        QuandlClient quandlClient = new QuandlClientImpl(quandlConfiguration, environment);

        SecurityProvider securityProvider = new QuandlSecurityProvider(quandlClient,
                quandlConfiguration.getDatabaseCode(),
                securityProviderConfiguration.getSecurities(),
                Optional.of(securityProviderConfiguration.getStartDate()),
                Optional.of(securityProviderConfiguration.getEndDate())
                );

        SecurityResource securityResource = new SecurityResource(securityProvider);

        environment.jersey().register(securityResource);
    }
}
