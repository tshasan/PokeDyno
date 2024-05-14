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
    pokemonList.innerHTML = ''; // Clear existing entries
    pokemons.forEach(pokemon => {
        const card = document.createElement('div');
        card.className = 'col';
        card.innerHTML = `
            <div class="card">
                <img src="${pokemon.imageUrl || 'https://placehold.co/150x100'}" class="card-img-top" alt="${pokemon.name}">
                <div class="card-body">
                    <h5 class="card-title">
                        <span>${pokemon.id}</span> &colon; <span>${pokemon.name}</span>
                    </h5>
                </div>
                
                <ul class="list-group list-group-flush">
                    <li class="list-group-item">
                        <span class="font-weight-bold">Base Experience:</span>
                        <span>${pokemon.baseExperience || 'N/A'}</span>
                    </li>
                    <li class="list-group-item">
                        <span class="font-weight-bold">Height:</span>
                        <span>${pokemon.height || 'N/A'}</span>
                    </li>
                    <li class="list-group-item">
                        <span class="font-weight-bold">Is Default:</span>
                        <span>${pokemon.isDefault ? 'Yes' : 'No'}</span>
                    </li>
                    <li class="list-group-item">
                        <span class="font-weight-bold">Weight:</span>
                        <span>${pokemon.weight || 'N/A'}</span>
                    </li>
                </ul>
                
                 <div class="card-body">
                     <button class="btn btn-danger" onclick="deletePokemon('${pokemon.id}')">Delete</button>
                </div>
            </div>
        `;
        pokemonList.appendChild(card);
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
