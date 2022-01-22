package io.bigdata.openfda.producer.config;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@Slf4j
public class KafkaTopicConfig {
    @Value("${spring.kafka.wait-for-servers}")
    private long waitForServers;

    @Value("${spring.kafka.number-of-topic-partitions}")
    private int numberOfTopicPartitions;

    @Value("${spring.kafka.number-of-topic-replicas}")
    private int numberOfTopicReplicas;

    @Value("${spring.kafka.topic-name}")
    private String topicName;

    @Bean
    @SneakyThrows
    public NewTopic topic(){
        log.info("Waiting for KAFKA resources to be ready");

        Thread.sleep(waitForServers);

        return TopicBuilder
                .name(topicName)
                .partitions(numberOfTopicPartitions)
                .replicas(numberOfTopicReplicas)
                .build();
    }
}