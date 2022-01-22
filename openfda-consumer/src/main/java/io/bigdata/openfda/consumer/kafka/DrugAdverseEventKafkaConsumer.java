package io.bigdata.openfda.consumer.kafka;

import io.bigdata.openfda.consumer.mongo.DrugAdverseEventMapper;
import io.bigdata.openfda.consumer.mongo.DrugAdverseEventRepository;
import io.bigdata.openfda.producer.kafka.DrugAdverseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class DrugAdverseEventKafkaConsumer {

    private final DrugAdverseEventRepository drugAdverseEventRepository;

    @KafkaListener(
            topics = {"${spring.kafka.topic-name}"},
            groupId = "${spring.kafka.consumer-group-id}",
            concurrency = "${spring.kafka.number-of-consumers}",
            containerFactory="drugAdverseEventKafkaListenerContainerFactory")
    void handleMessage(@Payload List<DrugAdverseEvent> payload,
                       @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                       @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp) {

        var documents = drugAdverseEventRepository.saveAll(DrugAdverseEventMapper.INSTANCE.map(payload));

        log.debug("Read {} DrugAdverseEvents from Kafka partition {}, timestamp {}. Saved {} documents in Mongo",
                payload.size(), partition, timestamp, documents.size());
    }
}