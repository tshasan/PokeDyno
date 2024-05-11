import com.pokedyno.PokemonTable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

class PokemonTableTest {
    // Mocked DynamoDbClient
    private DynamoDbClient mockDdbClient = mock(DynamoDbClient.class);
    private PokemonTable pokemonTable = new PokemonTable(mockDdbClient, "testTableName");

    @Test
    void testStorePokemonData() {
        // Mock putItem method
        when(mockDdbClient.putItem(any(PutItemRequest.class))).thenReturn(PutItemResponse.builder().build());

        String id = "1";
        String name = "Bulbasaur";
        String imageUrl = "https://pokeapi.co/api/v2/pokemon/1/";

        pokemonTable.storePokemonData(id, name, imageUrl);

        verify(mockDdbClient, times(1)).putItem(any(PutItemRequest.class));
    }

    @Test
    void testCreateTable() {
        // Mock createTable method
        when(mockDdbClient.createTable(any(CreateTableRequest.class))).thenReturn(CreateTableResponse.builder().build());
        pokemonTable.createTable("id");
        verify(mockDdbClient, times(1)).createTable(any(CreateTableRequest.class));
    }

    @Test
    void testErrorHandling() {
        when(mockDdbClient.putItem(any(PutItemRequest.class))).thenThrow(DynamoDbException.builder().message("Mocked exception").build());
        pokemonTable.storePokemonData("1", "Bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/");
        // eh idk if this is right
    }

    @Test
    void testGetItem() {
        Map<String, AttributeValue> expectedItem = new HashMap<>();
        expectedItem.put("id", AttributeValue.builder().s("1").build());
        when(mockDdbClient.getItem(any(GetItemRequest.class))).thenReturn(GetItemResponse.builder().item(expectedItem).build());
        Map<String, AttributeValue> item = pokemonTable.getItem("id", "1");
        verify(mockDdbClient, times(1)).getItem(any(GetItemRequest.class));
        assertEquals(expectedItem, item);
    }

    @Test
    void testUpdateItem() {
        when(mockDdbClient.updateItem(any(UpdateItemRequest.class))).thenReturn(UpdateItemResponse.builder().build());
        pokemonTable.updateItem("id", "1", "name", "Ivysaur");
        verify(mockDdbClient, times(1)).updateItem(any(UpdateItemRequest.class));
    }

    @Test
    void testDeleteItem() {
        when(mockDdbClient.deleteItem(any(DeleteItemRequest.class))).thenReturn(DeleteItemResponse.builder().build());
        pokemonTable.deleteItem("id", "1");
        verify(mockDdbClient, times(1)).deleteItem(any(DeleteItemRequest.class));
    }
}
