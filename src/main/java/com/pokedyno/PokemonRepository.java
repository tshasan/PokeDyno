package com.pokedyno;

import java.util.Optional;

public interface PokemonRepository {
    void save(Pokemon pokemon);

    Optional<Pokemon> findById(String id);

    void deleteById(String id);

    Iterable<Pokemon> findAll();
}
