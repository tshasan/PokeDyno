document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('refreshButton').addEventListener('click', refreshPokemonList);

    // Initial load of Pokémon list
    refreshPokemonList();
});

async function fetchPokemonList() {
    try {
        const response = await fetch('/pokemon');
        if (!response.ok) {
            throw new Error('Network response was not ok: ' + response.status);
        }
        return response.json();
    } catch {
        console.error('Error fetching Pokémon list:', error);
    }
}

function renderPokemonList(pokemons) {
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
}

async function refreshPokemonList() {
    try {
        const pokemons = await fetchPokemonList();
        renderPokemonList(pokemons);
    } catch (error) {
        console.error('Error refreshing Pokémon list:', error);
    }
}

async function deletePokemon(pokemonId) {
    try {
        const response = await fetch(`/pokemon/${pokemonId}`, {method: 'DELETE'});
        if (!response.ok) {
            throw new Error('Network response was not ok: ' + response.status);
        }
        console.log(`Pokemon with ID ${pokemonId} deleted successfully`);
        await refreshPokemonList();  // Refresh the list after deletion
    } catch {
        console.error('Error deleting Pokémon:', error);
    }
}

/**
 * This function is called when the "Go to Index" button is clicked.
 * It redirects the user to the index page.
 */
document.getElementById('goToIndex').addEventListener('click', function () {
    window.location.href = 'index.html';
});
