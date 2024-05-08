package com.pokedyno.controller;

import com.pokedyno.model.Pokemon;
import com.pokedyno.model.PokemonList;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class pokeController {
    private static final String POKEMON_API_URL = "https://pokeapi.co/api/v2";
    private final WebClient webClient;

    public pokeController(WebClient.Builder webClientBuilder) {
        webClient = webClientBuilder.baseUrl(POKEMON_API_URL).build();
    }

    @GetMapping("/")
    public Mono<List<Pokemon>> getPokemonWebClient() {
        int limit = 100;
        int offset = 0;

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/pokemon")
                        .queryParam("limit", limit)
                        .queryParam("offset", offset)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(PokemonList.class)
                .map(PokemonList::getPokemonList);
    }
}
