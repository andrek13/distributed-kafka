# Distributed Kafka

A sample distributed Kafka setup using Docker and Java, intended as a playground / demo for Kafka clusters, producers, and consumers in a distributed environment.

## Table of Contents

- [Overview](#overview)  
- [Architecture](#architecture)  
- [Features](#features)  
- [Prerequisites](#prerequisites)  
- [Getting Started](#getting-started)  
- [Project Structure](#project-structure)  

## Overview

This project demonstrates how to build and run a distributed Kafka system. It includes a multi-node Kafka cluster via Docker Compose, and a Java client for producing and consuming messages.

## Architecture

- **Kafka Cluster**: Multiple broker nodes, Zookeeper (if applicable), topics, partitions, replication.
- **Java Client**: Sample producer and consumer applications that interact with the Kafka cluster.
- **Docker Compose**: Orchestrates all Kafka nodes and necessary dependencies.

## Features

- Multi-node Kafka setup (via Docker Compose)  
- Sample Java producer & consumer clients  
- Customizable topic configuration (partitions, replication)  
- Logging for debugging and observability  

## Prerequisites

- Docker & Docker Compose installed  
- Java JDK (version matching `pom.xml`)  
- Maven (for building the Java parts)  

## Getting Started

1. **Clone the repository**

   ```sh
   git clone https://github.com/andrek13/distributed-kafka.git
   cd distributed-kafka

2. **Start Kafka cluster**

    ```sh
    docker-compose up -d

This brings up the Kafka brokers.

3. **Run producer / consumer**

Use the generated JARs (or run via your IDE) to start the producer or consumer, pointing them to the Kafka cluster endpoints.

4. **Stop cluster**
    ```sh
    docker-compose down
## Project structure
    ```sh
    .
    ├── docker-compose.yaml
    ├── pom.xml
    ├── mvnw
    ├── mvnw.cmd
    ├── .mvn/
    ├── src/
    │   ├── main/
    │   │   ├── java/
    │   │   └── resources/
    │   └── test/
    └── .gitignore

`docker-compose.yaml`: Defines Kafka cluster services

`pom.xml`: Maven configuration

`src/`: Java source code and resources

`.mvn/`, `mvnw`, `mvnw.cmd`: Maven wrapper files

