package io.bigdata.openfda.producer.service;

import io.bigdata.openfda.producer.client.FdaClient;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
public class AdverseEventsReader {
    private final FdaClient fdaClient;
    private final Long batchReadDelay;
    private final ApplicationEventPublisher publisher;
    private final LocalDate syncDateFrom;
    private final LocalDate syncDateTo;

    public AdverseEventsReader(FdaClient fdaClient,
                               ApplicationEventPublisher publisher,
                               @Value("${openfda.api.request.batch-read-delay}") Long batchReadDelay,
                               @Value("${openfda.api.request.sync-date-from}") String syncDateFrom,
                               @Value("${openfda.api.request.sync-date-to}") String syncDateTo) {
        this.fdaClient = fdaClient;
        this.publisher = publisher;
        this.batchReadDelay = batchReadDelay;
        this.syncDateFrom = LocalDate.parse(syncDateFrom);
        this.syncDateTo = LocalDate.parse(syncDateTo);
    }

    @SneakyThrows
    public void readEvents() {
        log.info("Started FDA drug adverse events processing");

        var fdaEventResponsePage = fdaClient.retrieveEvents(syncDateFrom, syncDateTo);

        log.info("Total results to fetch {} ", fdaEventResponsePage.getResponse().getMeta().getResult().getTotal());

        while (fdaEventResponsePage.hasNextPage()) {
            log.debug("Fetched {} results for page {}", fdaEventResponsePage.getResponse().getResults().size(), fdaEventResponsePage.getNextPageLink());

            fdaEventResponsePage.getResponse().getResults()
                    .forEach(publisher::publishEvent);

            Thread.sleep(batchReadDelay);

            fdaEventResponsePage = fdaClient.retrieveEvents(fdaEventResponsePage.getNextPageLink());
        }

        log.info("Finished FDA drug adverse events processing");
    }
}