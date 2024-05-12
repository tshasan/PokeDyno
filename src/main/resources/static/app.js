document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('pokemonSearchForm');
    form.addEventListener('submit', function(event) {
        event.preventDefault();
        const pokemonId = document.getElementById('pokemonIdInput').value;
        fetchPokemon(pokemonId);
    });
});

function fetchPokemon(pokemonId) {
    fetch(`https://pokeapi.co/api/v2/pokemon/${pokemonId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok: ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            console.log('JSON Response:', data); // Log the JSON data structure
            // Map data to fit the structure of PokeAPI v2
            const pokemon = {
                id: data.id,
                name: data.name,
                base_experience: data.base_experience,
                height: data.height,
                weight: data.weight,
                is_default: data.is_default,
                order: data.order,
                sprites: {
                    front_default: data.sprites.front_default
                }
            };
            displayPokemon(pokemon);
        })
        .catch(error => {
            console.error('Error fetching data:', error);
            document.getElementById('pokemonDescription').textContent = 'Failed to fetch data. Error: ' + error.message;
        });
}


function displayPokemon(pokemon) {
    // Check if 'sprites' and 'front_default' exist before attempting to access
    if (pokemon.sprites && pokemon.sprites.front_default) {
        document.getElementById('pokemonImage').src = pokemon.sprites.front_default;
    } else {
        // Provide a placeholder or a default image if no sprite is available
        document.getElementById('pokemonImage').src = '/placeholder.png';
    }

    // Setting other properties
    document.getElementById('pokemonId').textContent = pokemon.id || 'N/A';
    document.getElementById('pokemonName').textContent = pokemon.name || 'N/A';
    document.getElementById('baseExperience').textContent = pokemon.base_experience || 'N/A';
    document.getElementById('height').textContent = pokemon.height || 'N/A';
    document.getElementById('isDefault').textContent = pokemon.is_default ? 'Yes' : 'No';
    document.getElementById('order').textContent = pokemon.order || 'N/A';
    document.getElementById('weight').textContent = pokemon.weight || 'N/A';
    document.getElementById('pokemonDescription').textContent = 'Data retrieved from PokeAPI.';
}


function clearPokemon() {
    document.getElementById('pokemonImage').src = '/placeholder.jpg';
    document.querySelectorAll('#pokemon-container span').forEach(span => span.textContent = '');
    document.getElementById('pokemonDescription').textContent = 'Description goes here...';
}
