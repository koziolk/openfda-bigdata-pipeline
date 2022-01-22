package io.bigdata.openfda.producer.job;


import io.bigdata.openfda.producer.service.AdverseEventsReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class FdaSynchronizationJob {

    private final AdverseEventsReader adverseEventsReader;
    private static final long DELAY_2_MINUTES = 12000;

    @Scheduled(initialDelay = DELAY_2_MINUTES, fixedDelay=Long.MAX_VALUE)
    public void fetchEvents() {
        log.info("Started synchronizing drug adverse events");

        try {
            adverseEventsReader.readEvents();
        } catch (RuntimeException ex) {
            log.error("The following error occurred when reading events", ex);
        } finally {
            log.info("Finished synchronizing drug adverse events");
        }
    }
}