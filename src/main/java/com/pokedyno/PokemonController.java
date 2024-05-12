package com.pokedyno;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PokemonController {

    private final PokeAPIService pokeAPIService;

    @Autowired
    public PokemonController(PokeAPIService pokeAPIService) {
        this.pokeAPIService = pokeAPIService;
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
}


