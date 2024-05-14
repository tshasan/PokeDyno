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
    form.addEventListener('submit', function (event) {
        event.preventDefault();
        const pokemonId = document.getElementById('pokemonIdInput').value;
        fetchPokemon(pokemonId);
    });
});

function fetchPokemon(pokemonId) {
    fetch(`/pokemon/${pokemonId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok: ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            displayPokemon(data);
        })
        .catch(error => {
            console.error('Error fetching data:', error);
            description.textContent = 'Failed to fetch data. Error: ' + error.message;
        });
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
    description.textContent = `Data retrieved from ${pokemon.dataSource === 'database' ? 'Database' : 'PokeAPI'}.`;
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
