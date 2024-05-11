package com.pokedyno.controller;

import com.pokedyno.model.Pokemon;
import com.pokedyno.model.PokemonDetail;
import com.pokedyno.model.PokemonList;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
public class pokeController {
    private static final String POKEMON_API_URL = "https://pokeapi.co/api/v2";
    private final WebClient webClient;

    public pokeController(WebClient.Builder webClientBuilder) {
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024)) // 10MB
                .build();

        webClient = webClientBuilder
                .baseUrl(POKEMON_API_URL)
                .exchangeStrategies(strategies)
                .build();
    }

    /**
     * Get a list of Pokemon from the Pokemon API and fetch details for each Pokemon
     *
     * @param model Model to add the list of Pokemon to
     * @return Thymeleaf template name
     */
    @GetMapping("/")
    public Mono<String> getPokemonWebClient(Model model) {
        int limit = 50;
        int offset = 0;

        Mono<List<Pokemon>> pokemonMono = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/pokemon")
                        .queryParam("limit", limit)
                        .queryParam("offset", offset)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(PokemonList.class)
                .map(PokemonList::getPokemonList);

        // Fetch details for each Pokemon
        return pokemonMono.flatMapMany(Flux::fromIterable)
                .concatMap(this::fetchPokemonDetails)
                .collectList()
                .doOnNext(pokemons -> model.addAttribute("pokemons", pokemons))
                .thenReturn("pokemon");
    }

    /**
     * Send a request to the Pokemon API to fetch details for a Pokemon
     *
     * @param pokemon Pokemon to fetch details for
     * @return Pokemon with details
     */
    private Mono<Pokemon> fetchPokemonDetails(Pokemon pokemon) {
        return webClient.get()
                .uri(pokemon.getUrl())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(PokemonDetail.class)
                .map(pokemonDetail -> {
                    pokemon.setId(pokemonDetail.getId());
                    pokemon.setImage(pokemonDetail.getSprites().getOther().getOfficialArtwork().getFrontDefault());
                    return pokemon;
                });
    }
}
