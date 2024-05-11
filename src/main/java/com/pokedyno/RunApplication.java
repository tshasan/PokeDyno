package com.pokedyno;

import com.pokedyno.controller.pokeController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@SpringBootApplication
public class RunApplication {

    public static void main(String[] args) {
        SpringApplication.run(RunApplication.class, args);
        System.out.println("App Running!");
    }

    @Bean
    public PokemonTable pokemonTable() {
        DynamoDbClient ddbClient = DynamoDbClient.create();
        return new PokemonTable(ddbClient, "YourTableName");
    }

    @Bean
    public pokeController pokeController(WebClient.Builder webClientBuilder, PokemonTable pokemonTable) {
        return new pokeController(webClientBuilder, pokemonTable);
    }
}
