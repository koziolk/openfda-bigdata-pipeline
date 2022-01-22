package io.bigdata.openfda.consumer.mongo;

import com.mongodb.WriteConcern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.WriteConcernResolver;

@Slf4j
@Configuration
public class MongoConfig {

    @Bean
    public WriteConcernResolver writeConcernResolver() {
        return action -> {
            log.info("Using MongoDB Write Concern of Acknowledged");
            return WriteConcern.ACKNOWLEDGED;
        };
    }
}
