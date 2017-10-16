package com.skim.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.client.JerseyClientConfiguration;

import java.net.URI;

public class QuandlConfiguration {
    @JsonProperty
    private URI uri;

    @JsonProperty
    private String apiKey;

    @JsonProperty
    private String databaseCode;

    @JsonProperty
    private JerseyClientConfiguration jerseyClient;

    public URI getUri() {
        return uri;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getDatabaseCode() {
        return databaseCode;
    }

    public JerseyClientConfiguration getJerseyClient() {
        return jerseyClient;
    }
}
