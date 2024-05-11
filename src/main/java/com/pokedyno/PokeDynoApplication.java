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
            String pokemonName = "Pikachu";
        } finally {
            dynamoDBManager.stopDynamoDBLocal();
        }
    }
}
