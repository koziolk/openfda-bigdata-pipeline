# Open FDA producer

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
docker pull koziolk/openfda-producer
```

Available env variables (see default values in application.yaml)

```
- Kafka configuration ralted
KAFKA_BOOTSTRAP_SERVERS
KAFKA_WAIT_FOR_SERVERS
KAFKA_NUMBER_OF_TOPIC_PARTITIONS
KAFKA_NUMBER_OF_TOPIC_REPLICAS
KAFKA_TOPIC_NAME

- Open FDA API configuration ralted
OPENFDA_API_BATCH_SIZE
OPENFDA_API_BATCH_READ_DELAY
OPENFDA_API_SYNC_DATE_FROM
OPENFDA_API_SYNC_DATE_TO
```
