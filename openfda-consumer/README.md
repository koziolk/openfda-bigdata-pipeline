# Open FDA consumer

Application built with SpringBoot for the backend solution.  
Solution is dockerized.

## Build and run the project

* Building application with tests from command line
```
./mvnw clean install
```

* Running application

```
./mvnw spring-boot:run
```

## Docker image

* Building docker image
```
./mvnw spring-boot:build-image
```

You can pull the latest docker image from docker.io registry
```
docker pull koziolk/openfda-consumer
```

Available env variables (see default values in application.yaml)

```
- Kafka configuration ralted
KAFKA_BOOTSTRAP_SERVERS
KAFKA_WAIT_FOR_SERVERS
KAFKA_TOPIC_NAME
KAFKA_NUMBER_OF_CONSUMERS
KAFKA_CONSUMER_GROUP_ID
```