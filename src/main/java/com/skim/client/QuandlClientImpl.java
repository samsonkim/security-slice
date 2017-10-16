package com.skim.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skim.client.dto.QuandlTimeSeriesCollapse;
import com.skim.client.dto.QuandlTimeSeriesResponse;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Environment;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.util.Date;
import java.util.Optional;

import static com.skim.utils.DateUtils.*;

public class QuandlClientImpl implements QuandlClient {
    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
                                                            .setDateFormat(QUANDL_DATE_FORMAT);
    private static final String BASE_PATH = "/api/v3/datasets";
    private static final String FORMAT = ".json";
    private static final String API_KEY_PARAM = "api_key";

    private final String apiKey;
    private final WebTarget timeSeriesWebTarget;

    // Used by Dropwizard app
    public QuandlClientImpl(URI baseUri, Environment environment, String apiKey) {
        Client client = new JerseyClientBuilder(environment)
                .using(OBJECT_MAPPER)
                .build("quandl-client");
        this.timeSeriesWebTarget = client.target(baseUri).path(BASE_PATH);
        this.apiKey = apiKey;
    }

    // Used for integration tests
    public QuandlClientImpl(URI baseUri, Client client, String apiKey) {
        this.timeSeriesWebTarget = client.target(baseUri).path(BASE_PATH);
        this.apiKey = apiKey;
    }

    @Override
    public QuandlTimeSeriesResponse getTimeSeries(String databaseCode,
                                                  String stockSymbol,
                                                  Optional<QuandlTimeSeriesCollapse> collapse,
                                                  Optional<Date> startDate,
                                                  Optional<Date> endDate) {

        final WebTarget webTarget = timeSeriesWebTarget.path(databaseCode)
                .path(stockSymbol + FORMAT)
                .queryParam(API_KEY_PARAM, apiKey)
                .queryParam("collapse", collapse.map(c -> c.getName()).orElse(""))
                .queryParam("start_date", startDate.map(d -> QUANDL_DATE_FORMAT.format(d)).orElse(""))
                .queryParam("end_date", endDate.map(d -> QUANDL_DATE_FORMAT.format(d)).orElse(""));

        return webTarget.request(MediaType.APPLICATION_JSON_TYPE)
                        .get(QuandlTimeSeriesResponse.class);
    }
}
