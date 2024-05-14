const id = document.getElementById('pokemonId');
const name = document.getElementById('pokemonName');
const baseExperience = document.getElementById('baseExperience');
const height = document.getElementById('height');
const isDefault = document.getElementById('isDefault');
const weight = document.getElementById('weight');
const imageUrl = document.getElementById('pokemonImage');
const description = document.getElementById('pokemonDescription');

document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('pokemonSearchForm');
    form.addEventListener('submit', async function (event) {
        event.preventDefault();
        const pokemonId = document.getElementById('pokemonIdInput').value;
        await fetchPokemon(pokemonId);
    });
});

async function fetchPokemon(pokemonId) {
    try {
        const response = await fetch(`/pokemon/${pokemonId}`);
        if (!response.ok) {
            throw new Error('Network response was not ok: ' + response.status);
        }
        const data = await response.json();
        displayPokemon(data);
    } catch {
        console.error('Error fetching data:', error);
        description.textContent = 'Failed to fetch data. Error: ' + error.message;
    }
}

function displayPokemon(pokemon) {
    // Setting properties
    id.textContent = pokemon.id || 'N/A';
    name.textContent = pokemon.name || 'N/A';
    baseExperience.textContent = pokemon.baseExperience || 'N/A';
    height.textContent = pokemon.height || 'N/A';
    isDefault.textContent = pokemon.isDefault ? 'Yes' : 'No';
    weight.textContent = pokemon.weight || 'N/A';
    // Check if 'sprites' and 'front_default' exist before attempting to access
    if (pokemon.imageUrl) {
        imageUrl.src = pokemon.imageUrl;
    } else {
        // Provide a placeholder or a default image if no sprite is available
        imageUrl.src = 'https://placehold.co/150x100';
    }
    description.textContent = `Data retrieved from ${pokemon.isDataFromDb ? 'Database' : 'PokeAPI'}.`;
}

function clearPokemon() {
    id.textContent = "ID";
    name.textContent = "Pokemon Name";
    baseExperience.textContent = "";
    height.textContent = "";
    isDefault.textContent = "";
    weight.textContent = "";
    imageUrl.src = "";
    description.textContent = "Description goes here...";
}

/**
 * This function is called when the "Go to Index" button is clicked.
 * It redirects the user to the index page.
 */
document.getElementById('goToSettings').addEventListener('click', function () {
    window.location.href = 'settings.html';
});