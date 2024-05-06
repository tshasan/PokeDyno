package com.pokedyno;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;

// Simple Driver class

public class PokeDynoApplication {
    public static void main(String[] args) {
        DynamoDBManager dynamoDBManager = new DynamoDBManager();
        DynamoDbClient ddbClient = dynamoDBManager.startDynamoDBLocal();

        try {
            PokemonTable pokemonService = new PokemonTable(ddbClient, "Pokemon");
            pokemonService.createTable("Name");

            String pokemonName = "Pikachu";
            String pokemonType = "Electric";

            pokemonService.putItem("Name", pokemonName, "Type", pokemonType);
            System.out.println("Inserted Pokemon: " + pokemonName);

            Map<String, AttributeValue> retrievedPokemon = pokemonService.getItem("Name", pokemonName);
            System.out.println("Retrieved Pokemon: " + retrievedPokemon);

            pokemonService.deleteItem("Name", pokemonName);
            System.out.println("Deleted Pokemon: " + pokemonName);
        } finally {
            dynamoDBManager.stopDynamoDBLocal();
        }
    }
}
