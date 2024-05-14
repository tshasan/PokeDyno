package com.pokedyno;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class PokemonController {

    private final PokeAPIService pokeAPIService;
    private final PokemonRepository pokemonRepository;

    @Autowired
    public PokemonController(PokeAPIService pokeAPIService, PokemonRepository pokemonRepository) {
        this.pokeAPIService = pokeAPIService;
        this.pokemonRepository = pokemonRepository;
    }

    @GetMapping("/pokemon/{pokemonId}")
    public ResponseEntity<?> getPokemon(@PathVariable String pokemonId) {
        try {
            Pokemon pokemon = pokeAPIService.fetchPokemon(pokemonId);
            return ResponseEntity.ok(pokemon);
        } catch (Exception e) {
            e.printStackTrace(); //logging
            return ResponseEntity.badRequest().body("Failed to fetch Pokémon: " + e.getMessage());
        }
    }

    @GetMapping("/pokemon")
    public ResponseEntity<?> listAllPokemon() {
        try {
            Iterable<Pokemon> items = pokemonRepository.findAll();
            List<Pokemon> pokemonList = StreamSupport.stream(items.spliterator(), false)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(pokemonList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to list Pokémon: " + e.getMessage());
        }
    }

    @DeleteMapping("/pokemon/{pokemonId}")
    public ResponseEntity<?> deletePokemon(@PathVariable String pokemonId) {
        try {
            pokemonRepository.deleteById(pokemonId);
            return ResponseEntity.ok().body("Pokemon with ID " + pokemonId + " deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to delete Pokémon: " + e.getMessage());
        }
    }
}
