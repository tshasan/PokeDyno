package com.pokedyno;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokedyno.DynamoDBPokemonRepository;
import com.pokedyno.Pokemon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Optional;

@Service
public class PokeAPIService {
    private final RestTemplate restTemplate;
    private final DynamoDBPokemonRepository pokemonRepository;

    @Autowired
    public PokeAPIService(RestTemplate restTemplate, DynamoDBPokemonRepository pokemonRepository) {
        this.restTemplate = restTemplate;
        this.pokemonRepository = pokemonRepository;
    }

    public Pokemon fetchPokemon(String pokemonId) {
        // First check if Pokémon is in DynamoDB
        Optional<Pokemon> maybePokemon = pokemonRepository.findById(pokemonId);
        if (maybePokemon.isPresent()) {
            return maybePokemon.get();
        }

        // If not, fetch from PokeAPI and store in DynamoDB
        String url = "https://pokeapi.co/api/v2/pokemon/" + pokemonId;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        Pokemon pokemon = parsePokemon(response.getBody());
        pokemonRepository.save(pokemon);
        return pokemon;
    }

    private Pokemon parsePokemon(String json) {
        // Existing parsing logic
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, Pokemon.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Pokémon data", e);
        }
    }
}
