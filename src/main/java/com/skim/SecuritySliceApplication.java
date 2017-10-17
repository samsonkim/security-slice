package com.skim;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.skim.client.QuandlCacheClient;
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

        ObjectMapper objectMapper = environment.getObjectMapper();
        objectMapper.registerModule(new JodaModule());

        /*
            Formats the json to be human readable instead of compact view.
            Remove this for real production use
         */
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        QuandlConfiguration quandlConfiguration = configuration.getQuandl();
        SecurityProviderConfiguration securityProviderConfiguration = configuration.getSecurityProvider();

        //Use cached client to improve performance and to prevent redundant calls to Quandl
        QuandlClient quandlClient = new QuandlCacheClient(new QuandlClientImpl(quandlConfiguration, environment));

        SecurityProvider securityProvider = new QuandlSecurityProvider(quandlClient,
                quandlConfiguration,
                securityProviderConfiguration
                );

        SecurityResource securityResource = new SecurityResource(securityProvider);

        environment.jersey().register(securityResource);
    }
}
