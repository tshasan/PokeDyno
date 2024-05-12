package com.pokedyno;

import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.Optional;

@Repository
public class DynamoDBPokemonRepository implements PokemonRepository {
    private final DynamoDbTable<Pokemon> pokemonTable;

    public DynamoDBPokemonRepository(DynamoDbEnhancedClient enhancedClient) {
        this.pokemonTable = enhancedClient.table("Pokemon", TableSchema.fromBean(Pokemon.class));
    }

    @Override
    public void save(Pokemon pokemon) {
        pokemonTable.putItem(pokemon);
    }

    @Override
    public Optional<Pokemon> findById(String id) {
        Pokemon result = pokemonTable.getItem(Key.builder().partitionValue(id).build());
        return Optional.ofNullable(result);
    }

    @Override
    public void deleteById(String id) {
        pokemonTable.deleteItem(Key.builder().partitionValue(id).build());
    }

    @Override
    public Iterable<Pokemon> findAll() {
        return pokemonTable.scan().items();
    }
}

