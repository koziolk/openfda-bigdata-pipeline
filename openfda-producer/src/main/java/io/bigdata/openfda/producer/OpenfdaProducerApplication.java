package io.bigdata.openfda.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableRetry
@SpringBootApplication
public class OpenfdaProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenfdaProducerApplication.class, args);
	}
}