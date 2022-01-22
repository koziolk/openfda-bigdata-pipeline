package io.bigdata.openfda.producer.kafka;

import io.bigdata.openfda.producer.response.FdaDrug;
import io.bigdata.openfda.producer.response.FdaReaction;
import io.bigdata.openfda.producer.response.FdaResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;


@Slf4j
@Component
public class DrugAdverseEventKafkaPublisher {

    private final KafkaTemplate<String, DrugAdverseEvent> kafkaTemplate;
    private final String topicName;

    public DrugAdverseEventKafkaPublisher(KafkaTemplate<String, DrugAdverseEvent> kafkaTemplate,
                                          @Value("${spring.kafka.topic-name}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    @EventListener
    public void processFdaResult(FdaResult fdaResult) {

        try {
            var country = StringUtils.firstNonEmpty(fdaResult.getOccurrenceCountry(), Objects.nonNull(fdaResult.getPrimarySource())
                    ? fdaResult.getPrimarySource().getReportedCountry() : null);
            var patientAge = Objects.nonNull(fdaResult.getPatient().getPatientAge()) ? fdaResult.getPatient().getPatientAge()  : -1;
            var patientSex = Objects.nonNull(fdaResult.getPatient().getPatientSex()) ? fdaResult.getPatient().getPatientSex() : -1;
            var receiveDate = fdaResult.getReceiveDate();
            var serious = Objects.nonNull(fdaResult.getSerious()) ? fdaResult.getSerious() : -1;

            kafkaTemplate.send(topicName,  DrugAdverseEvent.builder()
                    .country(country)
                    .serious(serious)
                    .receiveDate(receiveDate)
                    .patientAge(patientAge)
                    .patientSex(patientSex)

                    .patientReactions(CollectionUtils.isNotEmpty(fdaResult.getPatient().getReactions()) ? fdaResult.getPatient().getReactions().stream()
                            .map(FdaReaction::getReactionMedRapt)
                            .filter(Objects::nonNull)
                            .map(StringUtils::trim)
                            .distinct()
                            .collect(Collectors.toUnmodifiableList()) : null)

                    .medicinalProduct(CollectionUtils.isNotEmpty(fdaResult.getPatient().getDrugs()) ? fdaResult.getPatient().getDrugs().stream()
                            .map(FdaDrug::getMedicinalProduct)
                            .filter(Objects::nonNull)
                            .map(StringUtils::trim)
                            .distinct()
                            .collect(Collectors.toUnmodifiableList()) : null)

                    .drugManufacturerNames(CollectionUtils.isNotEmpty(fdaResult.getPatient().getDrugs()) ? fdaResult.getPatient().getDrugs().stream()
                            .map(FdaDrug::getOpenFda)
                            .filter(Objects::nonNull)
                            .filter(openFda -> CollectionUtils.isNotEmpty(openFda.getManufacturerNames()))
                            .flatMap(openFda -> openFda.getManufacturerNames().stream())
                            .filter(Objects::nonNull)
                            .map(StringUtils::trim)
                            .distinct()
                            .collect(Collectors.toUnmodifiableList()) : null)

                    .drugBrandNames(CollectionUtils.isNotEmpty(fdaResult.getPatient().getDrugs()) ? fdaResult.getPatient().getDrugs().stream()
                            .map(FdaDrug::getOpenFda)
                            .filter(Objects::nonNull)
                            .filter(openFda -> CollectionUtils.isNotEmpty(openFda.getBrandNames()))
                            .flatMap(openFda -> openFda.getBrandNames().stream())
                            .filter(Objects::nonNull)
                            .map(StringUtils::trim)
                            .distinct()
                            .collect(Collectors.toUnmodifiableList()) : null)

                    .drugGenericNames(CollectionUtils.isNotEmpty(fdaResult.getPatient().getDrugs()) ? fdaResult.getPatient().getDrugs().stream()
                            .map(FdaDrug::getOpenFda)
                            .filter(Objects::nonNull)
                            .filter(openFda -> CollectionUtils.isNotEmpty(openFda.getGenericNames()))
                            .flatMap(openFda -> openFda.getGenericNames().stream())
                            .filter(Objects::nonNull)
                            .map(StringUtils::trim)
                            .distinct()
                            .collect(Collectors.toUnmodifiableList()) : null)

                    .drugSubstanceNames(CollectionUtils.isNotEmpty(fdaResult.getPatient().getDrugs()) ? fdaResult.getPatient().getDrugs().stream()
                            .map(FdaDrug::getOpenFda)
                            .filter(Objects::nonNull)
                            .filter(openFda -> CollectionUtils.isNotEmpty(openFda.getSubstanceNames()))
                            .flatMap(openFda -> openFda.getSubstanceNames().stream())
                            .filter(Objects::nonNull)
                            .map(StringUtils::trim)
                            .distinct()
                            .collect(Collectors.toUnmodifiableList()) : null)

                    .build());
        } catch (RuntimeException ex) {
            log.error("Exception occurred when sending FDA result to Kafka", ex);
            log.error("FDA result {}", fdaResult);
        }
    }
}