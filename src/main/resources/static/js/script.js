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

$(document).ready(async () => {
    const countries = await fetchFromApi(`/countries/names`);
    populateSelect('#country-select', countries);

    $('#country-select').select2().on('change', async function () {
        const country = $(this).val();
        console.log("Country = ",country)
        if (country) {
            const states = await fetchFromApi(`/${country}/states/names`);
            populateSelect('#state-select', states);
            $('#state-select').prop('disabled', false);
            // Reset city dropdown
            $('#city-select').val(null).trigger('change').prop('disabled', true).empty().append('<option value="">Search Cities...</option>');
        } else {
            // Reset both state and city dropdowns
            $('#state-select').val(null).trigger('change').prop('disabled', true).empty().append('<option value="">Search States...</option>');
            $('#city-select').val(null).trigger('change').prop('disabled', true).empty().append('<option value="">Search Cities...</option>');
        }
    });

    $('#state-select').select2().on('change', async function () {
        const state = $(this).val();
        console.log("States = ",state)
        const country = $('#country-select').val();
        if (state) {
            const cities = await fetchFromApi(`/${country}/${state}/cities/names`);
            populateSelect('#city-select', cities);
            $('#city-select').prop('disabled', false);
        } else {
            $('#city-select').val(null).trigger('change').prop('disabled', true).empty().append('<option value="">Search Cities...</option>');
        }
    });

    $('#city-select').select2();
});

function populateSelect(selectId, options) {
    const select = $(selectId);
    select.empty().append('<option value="">Select...</option>');
    options.forEach(option => {
        select.append(new Option(option, option));
    });
}
