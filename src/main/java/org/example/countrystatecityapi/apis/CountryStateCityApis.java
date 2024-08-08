package org.example.countrystatecityapi.apis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * CountryStateCityApis.java
 *
 * @author Mohamed Subaideen Imran A (mohamedsubaideenimran@nmsworks.co.in)
 * @module org.example.countrystatecityapi.apis
 * @created Jul 31, 2024
 */
@RestController
@CrossOrigin
public class CountryStateCityApis
{
    @Autowired
    CountryStateCityApiUtils countryStateCityApiUtils;


    @GetMapping("/continents")
    public Map<String,Map<String,String>> getContinents()
    {
        return countryStateCityApiUtils.getContinents();
    }

    @GetMapping("/continents/names")
    public List<String> getContinentsNames()
    {
        return countryStateCityApiUtils.getContinentsNames();
    }

    @GetMapping("/countries")
    public Map<String, Map<String, String>> getAllCountries()
    {
        return countryStateCityApiUtils.getAllCountries();
    }

    @GetMapping("/countries/names")
    public List<String> getAllCountriesNames()
    {
        return countryStateCityApiUtils.getAllCountriesName();
    }

    @GetMapping("/{continentName}/countries")
    public Map<String,Map<String,String>> getCountriesByContinent(@PathVariable String continentName)
    {
        return countryStateCityApiUtils.getAllCountriesByContinent(continentName);
    }

    @GetMapping("/{continentName}/countries/names")
    public List<String> getCountriesByContinentNames(@PathVariable String continentName)
    {
        return countryStateCityApiUtils.getAllCountriesNamesByContinent(continentName);
    }

    @GetMapping("/{countryName}/states")
    public Map<String, Map<String, String>> getAllStatesInACountry(@PathVariable String countryName) {
       return countryStateCityApiUtils.getAllStatesInACountry(countryName);
    }

    @GetMapping("/{countryName}/states/names")
    public List<String> getAllStatesInACountryNames(@PathVariable String countryName) {
        return countryStateCityApiUtils.getAllStatesNameInACountry(countryName);    }

    @GetMapping("/{countryName}/{stateName}/cities")
    public Map<String, Map<String, String>> getAllCitiesInAState(@PathVariable String countryName, @PathVariable String stateName) {
        return countryStateCityApiUtils.getCitiesByCountryAndState(countryName,stateName);
    }

    @GetMapping("/{countryName}/{stateName}/cities/names")
    public List<String> getCitiesByCountryAndState(
            @PathVariable String countryName,
            @PathVariable String stateName) {

       return countryStateCityApiUtils.getCitiesNamesByCountryAndState(countryName,stateName);
    }
}

