package com.skim;

import com.skim.client.QuandlClientImpl;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.net.URI;

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
        // TODO: application initialization
    }

    @Override
    public void run(final SecuritySliceConfiguration configuration,
                    final Environment environment) {

        String quandlApiKey = null;
        URI quandlUri = null;
        QuandlClientImpl quandlClient = new QuandlClientImpl(quandlUri, environment, quandlApiKey);

        // TODO: implement application
    }

}
