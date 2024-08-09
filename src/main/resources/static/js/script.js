let map;

function initGeoMap() {
    map = L.map('map').setView([51.505, -0.09], 2); // Initialize map with a default view

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(map);
}

function showOnGeoMap(lat, lng) {
    map.setView([lat, lng], 10); // Set map view to the selected location
    L.marker([lat, lng]).addTo(map).bindPopup('Selected Location').openPopup();
}

$(document).ready(async () => {
    initGeoMap(); // Initialize the GeoMap

    try {
        const countries = await fetchFromApi(`/countries/names`);
        populateSelect('#country-select', countries);

        $('#country-select').select2().on('change', async function () {
            const country = $(this).val();
            if (country) {
                const states = await fetchFromApi(`/${country}/states/names`);
                populateSelect('#state-select', states);
                $('#state-select').prop('disabled', false);
                $('#city-select').prop('disabled', true).empty().append('<option value="">Search Cities...</option>');
            } else {
                $('#state-select').prop('disabled', true).empty().append('<option value="">Search States...</option>');
                $('#city-select').prop('disabled', true).empty().append('<option value="">Search Cities...</option>');
            }
        });

        $('#state-select').select2().on('change', async function () {
            const state = $(this).val();
            const country = $('#country-select').val();
            if (state) {
                const cities = await fetchFromApi(`/${country}/${state}/cities/names`);
                populateSelect('#city-select', cities);
                $('#city-select').prop('disabled', false);
            } else {
                $('#city-select').prop('disabled', true).empty().append('<option value="">Search Cities...</option>');
            }
        });

        $('#city-select').select2().on('change', function () {
            const city = $(this).val();
            $('#show-map-button').prop('disabled', !city);
        });

        $('#show-map-button').on('click', async function () {
            const country = $('#country-select').val();
            const state = $('#state-select').val();
            const city = $('#city-select').val();
            try {
                const locationData = await fetchFromApi(`/${country}/${state}/${city}/location`);
                if (locationData && locationData.lat && locationData.lng) {
                    showOnGeoMap(locationData.lat, locationData.lng);
                } else {
                    console.error('Could not retrieve location data for the selected city.');
                }
            } catch (error) {
                console.error('Error fetching location data:', error);
            }
        });

    } catch (error) {
        console.error('Error initializing app:', error);
    }
});

function populateSelect(selectId, options) {
    const select = $(selectId);
    select.empty().append('<option value="">Select...</option>');
    options.forEach(option => {
        select.append(new Option(option, option));
    });
}

async function fetchFromApi(url) {
    try {
        const response = await fetch(url);
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return await response.json();
    } catch (error) {
        console.error('Error fetching data:', error);
    }
}
