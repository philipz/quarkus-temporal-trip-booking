# Trip Booking Demo using Temporal and Quarkus

## *Triporal* Project Goals

1. Create a demo application (called Triporal) for booking a trip 
1. Implement a saga that calls microservices 
1. Build with Temporal and Quarkus in Java
1. Support native executables with Quarkus and GraalVM
1. Create a basic user interface to drive the demo

Code used in this demo is derived from the original Temporal booking saga demo https://github.com/temporalio/samples-java/tree/master/src/main/java/io/temporal/samples/bookingsaga.


## *Triporal* Project Rationale

This project is a proof of concept on how Temporal can be used with Quarkus in an effective way.

Temporal is forked from the original Cadence project, and offers many features for building distrubuted applications using a workflow API. Because Temporal has a Java API, it's a good pairing with Quarkus for building reliable applications with Java microservices.

Here is the original description of Temporal:

> Temporal is a microservice orchestration platform which enables developers to build scalable applications without sacrificing productivity or reliability. Temporal server executes units of application logic, Workflows, in a resilient manner that automatically handles intermittent failures, and retries failed operations.

## Running Temporal Locally with Docker

Please see https://github.com/temporalio/docker-compose

```
docker-compose up
```

* Temporal Web UI **http://localhost:8080**

## Running *Triporal* Application Locally with Quarkus

For purely development purposes, you should run `./mvnw quarkus:dev` for each of the 4 underlying projects. The ports have been preconfigured for development mode:

* TODO: triporal-service UI: **http://localhost:8000**
* car-booking-service: **http://localhost:8081**
* flight-booking-service: **http://localhost:8082**
* hotel-booking-service: **http://localhost:8083**

## Building *Triporal* Application Native Image

**Important: This project currently requires a custom build of Temporal to enable Native Image in Quarkus!**

Please see the official docs: https://quarkus.io/guides/building-native-image

You should run for each of the underlying projects:

### With GraalVM Locally Installed
```
./mvnw clean install -Pnative
```

## With GraalVM in Docker

This will produce a Linux executable and a container that can be run locally in Docker:
```
./mvnw clean install -Pnative -Dquarkus.native.container-build=true -Dquarkus.container-image.build=true
```