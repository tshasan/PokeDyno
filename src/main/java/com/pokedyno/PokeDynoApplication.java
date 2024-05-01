package com.pokedyno;

import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PokeDynoApplication {

    private static DynamoDBProxyServer server;

    public static String createTable(DynamoDbClient ddb, String tableName, String key) {
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

        String newTable = "";
        try {
            CreateTableResponse response = ddb.createTable(request);
            DescribeTableRequest tableRequest = DescribeTableRequest.builder()
                    .tableName(tableName)
                    .build();

            // Wait until the Amazon DynamoDB table is created
            WaiterResponse<DescribeTableResponse> waiterResponse = ddb.waiter().waitUntilTableExists(tableRequest);
            waiterResponse.matched().response().ifPresent(System.out::println);

            newTable = response.tableDescription().tableName();
            return newTable;

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return "";
    }

    public static void putItemInTable(DynamoDbClient ddb, String tableName, String keyName, String keyValue, String typeKeyName, String typeValue) {
        HashMap<String, AttributeValue> itemValues = new HashMap<>();

        // Add all content to the table
        itemValues.put(keyName, AttributeValue.builder().s(keyValue).build());
        itemValues.put(typeKeyName, AttributeValue.builder().s(typeValue).build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(itemValues)
                .build();

        try {
            ddb.putItem(request);
        } catch (ResourceNotFoundException e) {
            System.err.format("Error: The Amazon DynamoDB table \"%s\" can't be found.\n", tableName);
            System.err.println("Be sure that it exists and that you've typed its name correctly!");
            System.exit(1);
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static Map<String, AttributeValue> getDynamoDBItem(DynamoDbClient ddb, String tableName, String keyName, String keyValue) {
        HashMap<String, AttributeValue> keyToGet = new HashMap<>();
        keyToGet.put(keyName, AttributeValue.builder().s(keyValue).build());

        GetItemRequest request = GetItemRequest.builder()
                .key(keyToGet)
                .tableName(tableName)
                .consistentRead(true)
                .build();

        try {
            GetItemResponse response = ddb.getItem(request);
            return response.item();
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public static void deleteDynamoDBItem(DynamoDbClient ddb, String tableName, String keyName, String keyValue) {
        HashMap<String, AttributeValue> keyToDelete = new HashMap<>();
        keyToDelete.put(keyName, AttributeValue.builder().s(keyValue).build());

        DeleteItemRequest deleteReq = DeleteItemRequest.builder()
                .tableName(tableName)
                .key(keyToDelete)
                .build();

        try {
            ddb.deleteItem(deleteReq);
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            String port = "8000";
            String uri = "http://localhost:" + port;
            // Create an in-memory and in-process instance of DynamoDB Local that runs over HTTP
            final String[] localArgs = {"-inMemory", "-port", port};
            System.out.println("Starting DynamoDB Local...");
            server = ServerRunner.createServerFromCommandLineArgs(localArgs);
            server.start();

            //  Create a client and connect to DynamoDB Local
            DynamoDbClient ddbClient = DynamoDbClient.builder()
                    .endpointOverride(URI.create(uri))
                    .httpClient(UrlConnectionHttpClient.builder().build())
                    .region(Region.US_WEST_2)
                    .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("dummyKey", "dummySecret")))
                    .build();

            String tableName = "Pokemon";
            String keyName = "Name";
            String typeKeyName = "Type";

            // Create a table in DynamoDB Local with table name "Pokemon" and primary key "Name"
            createTable(ddbClient, tableName, keyName);

            // Sample HTML forms interaction
            // For simplicity, this example assumes these values are passed from the HTML form
            String pokemonName = "Pikachu";
            String pokemonType = "Electric";

            // Insert Pokemon into the table
            putItemInTable(ddbClient, tableName, keyName, pokemonName, typeKeyName, pokemonType);

            // Retrieve Pokemon from the table
            Map<String, AttributeValue> retrievedPokemon = getDynamoDBItem(ddbClient, tableName, keyName, pokemonName);
            if (retrievedPokemon != null) {
                System.out.println("Retrieved Pokemon: " + retrievedPokemon);
            } else {
                System.out.println("Pokemon not found!");
            }

            // Delete Pokemon from the table
            deleteDynamoDBItem(ddbClient, tableName, keyName, pokemonName);

            // Retrieve the deleted Pokemon (should return null)
            retrievedPokemon = getDynamoDBItem(ddbClient, tableName, keyName, pokemonName);
            if (retrievedPokemon != null) {
                System.out.println("Retrieved Deleted Pokemon: " + retrievedPokemon);
            } else {
                System.out.println("Pokemon not found after deletion!");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
