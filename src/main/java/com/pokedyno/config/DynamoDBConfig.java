package com.pokedyno.config;

import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.URI;

@Configuration
public class DynamoDBConfig {
    private DynamoDBProxyServer server;

    @Value("${amazon.dynamodb.endpoint}")
    private String dynamoDbEndpoint;

    @PostConstruct
    public void startDynamoDBLocal() throws Exception {
        System.setProperty("sqlite4java.library.path", "native-libs");
        server = ServerRunner.createServerFromCommandLineArgs(new String[]{
                "-inMemory",
                "-port", "8000"
        });
        server.start();
    }

    @PreDestroy
    public void stopDynamoDBLocal() throws Exception {
        if (server != null) {
            server.stop();
        }
    }

    @Bean
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
                .endpointOverride(URI.create("http://localhost:8000"))
                .region(Region.US_WEST_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }

}


