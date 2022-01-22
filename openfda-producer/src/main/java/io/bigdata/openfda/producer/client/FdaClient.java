package io.bigdata.openfda.producer.client;

import io.bigdata.openfda.producer.response.FdaEventResponse;
import io.bigdata.openfda.producer.response.FdaEventResponsePage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.SocketTimeoutException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Component
public class FdaClient {
    private final RestTemplate restTemplate;
    private final Integer batchSize;

    public FdaClient(RestTemplate restTemplate,
                     @Value("${openfda.api.request.batch-size}") Integer batchSize) {

        this.restTemplate = restTemplate;
        this.batchSize = batchSize;
    }

    public FdaEventResponsePage retrieveEvents(LocalDate dateFrom, LocalDate dateTo) {
        final ResponseEntity<FdaEventResponse> responseEntity =
                restTemplate.getForEntity("/drug/event.json?search=receivedate:[{dateFrom}+TO+{dateTo}]&limit={batchSize}", FdaEventResponse.class,
                        dateFrom, dateTo, batchSize);

        return getFdaEventResponsePage(responseEntity);
    }

    @Retryable(value = SocketTimeoutException.class, backoff = @Backoff(delay = 5000))
    public FdaEventResponsePage retrieveEvents(String nextPageUrl) {

        log.debug("Started reading page {}", nextPageUrl);
        final ResponseEntity<FdaEventResponse> responseEntity = restTemplate.getForEntity(nextPageUrl, FdaEventResponse.class);
        log.debug("Finished reading page {} with result {}", nextPageUrl, responseEntity.getStatusCode().name());

        return getFdaEventResponsePage(responseEntity);
    }

    @SneakyThrows
    private FdaEventResponsePage getFdaEventResponsePage(ResponseEntity<FdaEventResponse> responseEntity) {
        var link = processLinkHeader(responseEntity);

        if (link.isPresent()) {
            var nextResultPageUrl = URLDecoder.decode(StringUtils.substringBetween(link.get(), "<", ">"), StandardCharsets.UTF_8 );
            return FdaEventResponsePage.builder()
                    .response(responseEntity.getBody())
                    .nextPageLink(nextResultPageUrl)
                    .build();
        } else {
            return FdaEventResponsePage.builder()
                    .response(responseEntity.getBody())
                    .build();
        }
    }

    private Optional<String> processLinkHeader(ResponseEntity<FdaEventResponse> responseEntity) {
        return responseEntity.getHeaders().entrySet().stream()
                .filter(entry -> entry.getKey().equalsIgnoreCase("Link"))
                .map(entry -> entry.getValue().get(0))
                .findFirst();
    }
}