package io.bigdata.openfda.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@EnableMongoRepositories
@SpringBootApplication
public class OpenfdaConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenfdaConsumerApplication.class, args);
	}
}