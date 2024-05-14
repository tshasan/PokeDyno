document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('refreshButton').addEventListener('click', refreshPokemonList);

    // Initial load of Pokémon list
    refreshPokemonList();
});

function refreshPokemonList() {
    fetch('/pokemon')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok: ' + response.status);
            }
            return response.json();
        })
        .then(pokemons => {
            const pokemonList = document.getElementById('pokemonList');
            pokemonList.innerHTML = '';  // Clear existing entries
            pokemons.forEach(pokemon => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${pokemon.id}</td>
                    <td>${pokemon.name}</td>
                    <td>
                        <button class="btn btn-danger" onclick="deletePokemon('${pokemon.id}')">Delete</button>
                    </td>
                `;
                pokemonList.appendChild(row);
            });
        })
        .catch(error => {
            console.error('Error fetching Pokémon list:', error);
        });
}

function deletePokemon(pokemonId) {
    fetch(`/pokemon/${pokemonId}`, {method: 'DELETE'})
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok: ' + response.status);
            }
            console.log(`Pokemon with ID ${pokemonId} deleted successfully`);
            refreshPokemonList();  // Refresh the list after deletion
        })
        .catch(error => {
            console.error('Error deleting Pokémon:', error);
        });
}
