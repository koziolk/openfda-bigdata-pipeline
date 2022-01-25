# OpenFDA Bigdata Pipeline

**OpenFDA Bigdata Pipeline** is a project that processes and visualizes data from drug adverse events' reports provided by [openFDA](https://open.fda.gov/apis/drug/) research project.
It exposes a live monitoring dashboard where users can see continuously incoming reports in an aggregated fashion.

The solution uses [Apache Kafka](https://kafka.apache.org/) for integration and [Mongo DB](https://www.mongodb.com) as a storage.

## Contents

This repository contains the code for the openFDA bigdata pipeline solution

* [openfda-producer](openfda-producer) it's a microservice build with [Spring Boot](https://spring.io/projects/spring-boot) and written in [Java](https://www.java.com)
* [openfda-consumer](openfda-consumer) it's a microservice build with [Spring Boot](https://spring.io/projects/spring-boot) and written in [Java](https://www.java.com)
* [openfda-live-dashboard](openfda-live-dashboard) it's a web application build with [Flask](https://flask.palletsprojects.com/), [Dash](https://github.com/plotly/dash) and written in [Python](https://www.python.org/)

##  Pipeline Architecture


![Pipeline Architecture](pipeline-architecture.svg)


##  Configuration

The project runs with the default configuration defined in each of services and in `pipeline.yml`. For more details refer directly to:

* [openfda-producer](openfda-producer/README.md)
* [openfda-consumer](openfda-consumer/README.md)
* [openfda-live-dashboard](openfda-live-dashboard/README.md)
* [pipeline](pipeline.yml)

##  Running solution locally in Docker

If you intend to try running project yourself, I have put together a `pipeline.yml` configuration that can help you get started. 

Calling the following command

``
docker-compose -f pipeline.yml up
``

will:

* Start `openfda-producer` container 
* Start `kafka` container
* Start `openfda-consumer` container
* Start `mongodb` container
* Start `openfda-live-dashboard` container which will expose port `8050`
* Start `jupyter-notebook` container which will expose port `8888`


## Accessing the application

Once all your Docker containers are up and running you can access `openfda-live-dashaboard` web dashboard via a browser under the following URL:

[http://localhost:8050](http://localhost:8050) 

In addition, you can access Jupyter Notebook `jupyter-notebook` via a browser under the following URL:

[http://localhost:8888](http://localhost:8888) 

## Issues and contribution

Bug reports and pull requests are welcome on GitHub at https://github.com/koziolk/openfda-bigdata-pipeline

## License

**OpenFDA Bigdata Pipeline** is published under [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).


