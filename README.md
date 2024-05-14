# PokeDyno

## Overview
PokeDyno is a Spring Boot application that demonstrates the integration of DynamoDB interfaces with the Pokémon API, utilizing a local DynamoDB instance to retrieve and store Pokémon data. This application enables users to perform CRUD operations on Pokémon data, directly fetching details from the Pokémon API when not already available in the local DynamoDB, effectively using it as a caching server. With minor modifications, this project can be deployed on AWS cloud services, transitioning from the local Maven-managed DynamoDB instance to a fully managed AWS environment.

## Features
- **Fetch Pokémon:** Get details of any Pokémon by ID.
- **List Pokémon:** View all Pokémon currently stored in the DynamoDB.
- **Delete Pokémon:** Remove a Pokémon entry from the database by its ID.

## Setup and Installation
1. ### Install and configure AWS CLI
   - Official installation guide [here](https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html)
   - You must configure the aws cli before proceeding and set the region to us-west-1 this can be configured in `DynamoDBConfig.java`
   ```bash
   aws configure
   ``` 
2. ### Clone the Repository:
   ```bash
   git clone git@github.com:tshasan/PokeDyno.git
   cd PokeDyno
   ```
3. ### Build Application
   ```bash
   ./mvnw clean install
   ```
4. ### Run the application
   ```bash
   ./mvnw spring-boot:run
   ```
5. ### Accessing the application
   Navigate to http://localhost:8080 to access the application

## Frameworks used
* Springboot
* DynamoDB local
* [PokeAPI](https://pokeapi.co/)

## Configuration
Configuration settings are specified in `application.properties`, allowing for customization of application parameters. DynamoDB local setup is managed in `DynamoDBConfig.java`, which can be adjusted to match different deployment environments.

## References
- [aws-dynamodb-examples](https://github.com/aws-samples/aws-dynamodb-examples/tree/master)
- [awesome-dynamodb](https://github.com/alexdebrie/awesome-dynamodb)
- [DynamoDB in a Spring Boot Application Using Spring Data](https://www.baeldung.com/spring-data-dynamodb)
- [dynamodb-spring-boot](https://github.com/JavatoDev-com/dynamodb-spring-boot)



