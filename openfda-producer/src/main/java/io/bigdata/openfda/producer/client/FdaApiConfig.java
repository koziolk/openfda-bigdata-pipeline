package io.bigdata.openfda.producer.client;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BASIC;

@Configuration
public class FdaApiConfig {

    @Value("${openfda.api.key}")
    private String apiKey;

    @Value("${openfda.api.base-url}")
    private String baseUrl;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder,
                                     RestTemplateResponseErrorHandler responseErrorHandler) {

        var loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(BASIC);

        return builder
                .rootUri(baseUrl)
                .errorHandler(responseErrorHandler)
                .defaultHeader("Authorization", "Basic " + this.apiKey)
                .requestFactory(() -> new OkHttp3ClientHttpRequestFactory(new OkHttpClient.Builder()
                        .connectionPool(new ConnectionPool(50, 30, TimeUnit.SECONDS))
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(false)
                        .addInterceptor(loggingInterceptor)
                        .build())
                ).build();
    }
}