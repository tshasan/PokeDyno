package com.pokedyno;

import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;

// This class manages the DynamoDB so dynodb local that runs on maven

public class DynamoDBManager {
    private DynamoDBProxyServer server;

    public DynamoDbClient startDynamoDBLocal() {
        try {
            String port = "8000";
            String uri = "http://localhost:" + port;
            final String[] localArgs = {"-inMemory", "-port", port};
            System.out.println("Starting DynamoDB Local...");
            server = ServerRunner.createServerFromCommandLineArgs(localArgs);
            server.start();

            return DynamoDbClient.builder()
                    .endpointOverride(URI.create(uri))
                    .httpClient(UrlConnectionHttpClient.builder().build())
                    .region(Region.US_WEST_2)
                    .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("dummyKey", "dummySecret")))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to start DynamoDB Local", e);
        }
    }

    public void stopDynamoDBLocal() {
        if (server != null) {
            try {
                server.stop();
            } catch (Exception e) {
                System.err.println("Failed to stop DynamoDB Local");
            }
        }
    }
}
