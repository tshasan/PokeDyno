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
            document.getElementById('pokemonDescription').textContent = 'Failed to fetch data. Error: ' + error.message;
        });
}


function displayPokemon(pokemon) {
    // Check if 'sprites' and 'front_default' exist before attempting to access
    if (pokemon.imageUrl) {
        document.getElementById('pokemonImage').src = pokemon.imageUrl;
    } else {
        // Provide a placeholder or a default image if no sprite is available
        document.getElementById('pokemonImage').src = 'https://placehold.co/150x100';
    }
    // Setting other properties
    document.getElementById('pokemonId').textContent = pokemon.id || 'N/A';
    document.getElementById('pokemonName').textContent = pokemon.name || 'N/A';
    document.getElementById('baseExperience').textContent = pokemon.baseExperience || 'N/A';
    document.getElementById('height').textContent = pokemon.height || 'N/A';
    document.getElementById('isDefault').textContent = pokemon.isDefault ? 'Yes' : 'No';
    document.getElementById('weight').textContent = pokemon.weight || 'N/A';
    document.getElementById('pokemonDescription').textContent = 'Data retrieved from PokeAPI.';
}


function clearPokemon() {
    document.getElementById('pokemonImage').src = '';
    document.querySelectorAll('#pokemon-container span').forEach(span => span.textContent = '');
    document.getElementById('pokemonDescription').textContent = 'Description goes here...';
}
