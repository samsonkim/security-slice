package com.skim;

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
        // TODO: application initialization
    }

    @Override
    public void run(final SecuritySliceConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
