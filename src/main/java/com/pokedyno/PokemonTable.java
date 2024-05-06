package com.pokedyno;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.Map;


//just makes the dynodb with full crud functionality.

public class PokemonTable {
    private final DynamoDbClient ddbClient;
    private final String tableName;

    public PokemonTable(DynamoDbClient ddbClient, String tableName) {
        this.ddbClient = ddbClient;
        this.tableName = tableName;
    }

    // Create a table with a specified key
    public void createTable(String key) {
        CreateTableRequest request = CreateTableRequest.builder()
                .attributeDefinitions(AttributeDefinition.builder()
                        .attributeName(key)
                        .attributeType(ScalarAttributeType.S)
                        .build())
                .keySchema(KeySchemaElement.builder()
                        .attributeName(key)
                        .keyType(KeyType.HASH)
                        .build())
                .provisionedThroughput(ProvisionedThroughput.builder()
                        .readCapacityUnits(5L)
                        .writeCapacityUnits(5L)
                        .build())
                .tableName(tableName)
                .build();

        try {
            CreateTableResponse response = ddbClient.createTable(request);
        } catch (DynamoDbException e) {
            System.err.println("Failed to create table: " + e.getMessage());
        }
    }

    // Add a new item or update an existing item
    public void putItem(String keyName, String keyValue, String typeKeyName, String typeValue) {
        HashMap<String, AttributeValue> itemValues = new HashMap<>();
        itemValues.put(keyName, AttributeValue.builder().s(keyValue).build());
        itemValues.put(typeKeyName, AttributeValue.builder().s(typeValue).build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(itemValues)
                .build();

        try {
            ddbClient.putItem(request);
        } catch (DynamoDbException e) {
            System.err.println("Failed to put item: " + e.getMessage());
        }
    }

    // Update specific attributes of an item
    public void updateItem(String keyName, String keyValue, String typeKeyName, String newValue) {
        Map<String, AttributeValueUpdate> updates = new HashMap<>();
        updates.put(typeKeyName, AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(newValue).build())
                .action(AttributeAction.PUT)
                .build());

        UpdateItemRequest request = UpdateItemRequest.builder()
                .tableName(tableName)
                .key(Map.of(keyName, AttributeValue.builder().s(keyValue).build()))
                .attributeUpdates(updates)
                .build();

        try {
            ddbClient.updateItem(request);
        } catch (DynamoDbException e) {
            System.err.println("Failed to update item: " + e.getMessage());
        }
    }

    // Retrieve an item
    public Map<String, AttributeValue> getItem(String keyName, String keyValue) {
        HashMap<String, AttributeValue> keyToGet = new HashMap<>();
        keyToGet.put(keyName, AttributeValue.builder().s(keyValue).build());

        GetItemRequest request = GetItemRequest.builder()
                .tableName(tableName)
                .key(keyToGet)
                .consistentRead(true)
                .build();

        try {
            GetItemResponse response = ddbClient.getItem(request);
            return response.item();
        } catch (DynamoDbException e) {
            System.err.println("Failed to get item: " + e.getMessage());
            return null;
        }
    }

    // Delete an item
    public void deleteItem(String keyName, String keyValue) {
        HashMap<String, AttributeValue> keyToDelete = new HashMap<>();
        keyToDelete.put(keyName, AttributeValue.builder().s(keyValue).build());

        DeleteItemRequest request = DeleteItemRequest.builder()
                .tableName(tableName)
                .key(keyToDelete)
                .build();

        try {
            ddbClient.deleteItem(request);
        } catch (DynamoDbException e) {
            System.err.println("Failed to delete item: " + e.getMessage());
        }
    }
}
