package com.pokedyno;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Optional;

@Service
public class PokeAPIService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PokeAPIService.class);
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
            LOGGER.info("Pokemon with ID {} found in the database.", pokemonId);
            return maybePokemon.get();
        }

        // If not, fetch from PokeAPI and store in DynamoDB
        String url = "https://pokeapi.co/api/v2/pokemon/" + pokemonId;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        Pokemon pokemon = parsePokemon(response.getBody());
        pokemonRepository.save(pokemon);
        LOGGER.info("Pokemon with ID {} fetched from PokeAPI and stored in the database.", pokemonId);
        return pokemon;
    }


    private Pokemon parsePokemon(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(json);
            String id = root.get("id").asText();
            String name = root.get("name").asText();
            boolean isDefault = root.get("is_default").asBoolean();
            int baseExperience = root.get("base_experience").asInt();
            int height = root.get("height").asInt();
            int weight = root.get("weight").asInt();
            String imageUrl = root.get("sprites").get("other").get("official-artwork").get("front_default").asText();

            // Create Pokémon instance
            Pokemon pokemon = new Pokemon();
            pokemon.setId(id);
            pokemon.setName(name);
            pokemon.setIsDefault(isDefault);
            pokemon.setBaseExperience(baseExperience);
            pokemon.setHeight(height);
            pokemon.setWeight(weight);
            pokemon.setImageUrl(imageUrl);

            return pokemon;
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Pokémon data", e);
        }
    }

}
