
import com.pokedyno.DynamoDBPokemonRepository;
import com.pokedyno.Pokemon;
import com.pokedyno.PokemonRepository;
import com.pokedyno.config.DynamoDBConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ContextConfiguration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = {DynamoDBConfig.class, DynamoDBPokemonRepository.class})
@Profile("local")
public class DynamoDBPokemonRepositoryTest {

    @Autowired
    private PokemonRepository pokemonRepository;

    @Autowired
    private DynamoDbClient dynamoDbClient;

    @Autowired
    private DynamoDbEnhancedClient enhancedClient;

    @BeforeEach
    public void setup() {
        createTableIfNeeded("Pokemon", "id", ScalarAttributeType.S);
    }

    private void createTableIfNeeded(String tableName, String partitionKeyName, ScalarAttributeType partitionKeyType) {
        ListTablesResponse listTablesResponse = dynamoDbClient.listTables();
        if (!listTablesResponse.tableNames().contains(tableName)) {
            CreateTableRequest createTableRequest = CreateTableRequest.builder()
                    .tableName(tableName)
                    .keySchema(KeySchemaElement.builder()
                            .attributeName(partitionKeyName)
                            .keyType(KeyType.HASH)
                            .build())
                    .attributeDefinitions(AttributeDefinition.builder()
                            .attributeName(partitionKeyName)
                            .attributeType(partitionKeyType)
                            .build())
                    .provisionedThroughput(ProvisionedThroughput.builder()
                            .readCapacityUnits(5L)
                            .writeCapacityUnits(5L)
                            .build())
                    .build();
            dynamoDbClient.createTable(createTableRequest);
            waitForTableCreation(tableName);
        }
    }

    private void waitForTableCreation(String tableName) {
        DescribeTableRequest describeTableRequest;
        DescribeTableResponse describeTableResponse;
        boolean isTableAvailable = false;
        while (!isTableAvailable) {
            try {
                Thread.sleep(1000); // Sleep for 1 second
                describeTableRequest = DescribeTableRequest.builder()
                        .tableName(tableName)
                        .build();
                describeTableResponse = dynamoDbClient.describeTable(describeTableRequest);
                isTableAvailable = describeTableResponse.table().tableStatus().equals(TableStatus.ACTIVE);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Table creation wait interrupted", e);
            }
        }
    }

    @Test
    public void testSaveAndFindById() {
        Pokemon pikachu = new Pokemon();
        pikachu.setId("001");
        pikachu.setName("Pikachu");
        pikachu.setType("Electric");

        pokemonRepository.save(pikachu);

        Optional<Pokemon> retrieved = pokemonRepository.findById("001");
        assertTrue(retrieved.isPresent(), "Pokemon should be found");
        assertEquals("Pikachu", retrieved.get().getName(), "Pokemon name should match");
    }

    @Test
    public void testDeleteById() {
        Pokemon bulbasaur = new Pokemon();
        bulbasaur.setId("002");
        bulbasaur.setName("Bulbasaur");
        bulbasaur.setType("Grass");

        pokemonRepository.save(bulbasaur);
        pokemonRepository.deleteById("002");

        Optional<Pokemon> retrieved = pokemonRepository.findById("002");
        assertFalse(retrieved.isPresent(), "Pokemon should not be found after deletion");
    }
}

