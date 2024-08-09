package org.example.countrystatecityapi.apis;

import org.example.countrystatecityapi.models.LocationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * CountryStateCityApiUtils.java
 *
 * @author Mohamed Subaideen Imran A (mohamedsubaideenimran@nmsworks.co.in)
 * @module org.example.countrystatecityapi.apis
 * @created Aug 02, 2024
 */
@Component
public class CountryStateCityApiUtils
{

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public Map<String, Map<String, String>> getContinents() {
        String query = "SELECT * FROM regions ORDER BY name ASC";
        return jdbcTemplate.query(query, rs -> {
            Map<String, Map<String, String>> result = new LinkedHashMap<>();
            while (rs.next()) {
                Map<String, String> columnData = new LinkedHashMap<>();

                columnData.put("id", rs.getString("id"));
                columnData.put("name", rs.getString("name"));
                columnData.put("created_at", rs.getString("created_at"));
                columnData.put("updated_at", rs.getString("updated_at"));
                columnData.put("flag", rs.getString("flag"));
                columnData.put("wikiDataId", rs.getString("wikiDataId"));

                result.put(rs.getString("name"), columnData);
            }
            return result;
        });

    }

    public List<String> getContinentsNames() {
        String query = "SELECT name FROM regions ORDER BY name ASC";

        return jdbcTemplate.query(query,
                (rs, rowNum) -> rs.getString("name"));
    }


    public Map<String, Map<String, String>> getAllCountries() {
        String query = "SELECT * FROM countries ORDER BY name ASC";

        return jdbcTemplate.query(query, rs -> {
            Map<String, Map<String, String>> result = new LinkedHashMap<>();
            while (rs.next()) {
                Map<String, String> columnData = getColumnDataMapForCountries(rs);
                result.put(rs.getString("name"), columnData);
            }
            return result;
        });
    }
    
    public List<String> getAllCountriesName() {
        String query = "SELECT name FROM countries ORDER BY name ASC";

        return jdbcTemplate.query(query,
                (rs, rowNum) -> rs.getString("name"));
    }

    public Map<String, Map<String, String>> getAllCountriesByContinent(String continentName) {
        String query = "SELECT * FROM countries c JOIN regions r ON c.region_id = r.id WHERE" +
                " r.name = ? ORDER BY c.name ASC";

        return jdbcTemplate.query(query,new Object[]{continentName}, rs -> {
            Map<String, Map<String, String>> result = new LinkedHashMap<>();
            while (rs.next()) {
                Map<String, String> columnData = getColumnDataMapForCountries(rs);
                result.put(rs.getString("name"), columnData);
            }
            return result;
        });
    }

    public List<String> getAllCountriesNamesByContinent(String continentName) 
    {
        String query = "SELECT c.name FROM countries c JOIN regions r ON c.region_id = r.id WHERE" +
                " r.name = ? ORDER BY c.name ASC";

        return jdbcTemplate.query(query,new Object[]{continentName}, rs ->
        {
            List<String> result = new ArrayList<>();
            while (rs.next()) {
                result.add(rs.getString("name"));
            }
            return result;
        });
    }

    public Map<String, Map<String, String>> getAllStatesInACountry(@RequestParam String countryName) {
        String query = "SELECT * " +
                "FROM states  s " +
                "JOIN countries co ON s.country_id = co.id " +
                "WHERE co.name = ?" + " OR co.iso2 = ?" +  " OR s.country_id = ?" +
                "ORDER BY s.name ASC";

        return jdbcTemplate.query(query, new Object[]{countryName,countryName,countryName}, rs -> {
            Map<String, Map<String, String>> result = new LinkedHashMap<>();
            while (rs.next()) {
                Map<String, String> columnData = new LinkedHashMap<>();

                getColumnDataForStateMap(rs, columnData);

                result.put(rs.getString("name"), columnData);
            }
            return result;
        });
    }

    public List<String> getAllStatesNameInACountry(String country)
    {
        String query = "SELECT s.name " +
                "FROM states s " +
                "JOIN countries co ON s.country_id = co.id " +
                "WHERE co.name = ? OR co.iso2 = ?" +
                "ORDER BY s.name ASC";

        return jdbcTemplate.query(query, new Object[]{country,country},
                (rs, rowNum) -> rs.getString("name"));
    }

    public Map<String, Map<String, String>> getCitiesByCountryAndState(@RequestParam String countryName, String stateName) {
        String query = "SELECT * " +
                "FROM cities  c " +
                "JOIN states s ON c.state_id = s.id " +
                "JOIN countries co ON s.country_id = co.id " +
                "WHERE co.name = ? AND s.name = ? " + " OR co.iso2 = ? AND s.iso2 = ?" +
                " OR co.name = ? AND s.iso2 = ? OR co.iso2 = ? AND s.name = ?" +
                " OR s.country_id = ? AND c.state_id = ?" +
                " OR s.country_id = ? AND s.name = ?" +
                " OR s.country_id = ? AND s.iso2 = ?" +
                " OR co.name = ? AND c.state_id = ?" +
                " OR co.iso2 = ? AND c.state_id = ?" +
                "ORDER BY c.name ASC";
        return jdbcTemplate.query(query, new Object[]{countryName,stateName,countryName,stateName,countryName,stateName,countryName,stateName,countryName,stateName,countryName,stateName,countryName,stateName,countryName,stateName,countryName,stateName}, rs -> {
            Map<String, Map<String, String>> result = new LinkedHashMap<>();
            while (rs.next()) {
                Map<String, String> columnData = new LinkedHashMap<>();

                columnData.put("id", rs.getString("id"));
                columnData.put("name", rs.getString("name"));
                columnData.put("state_id", rs.getString("state_id"));
                columnData.put("state_code", rs.getString("state_code"));
                columnData.put("country_id", rs.getString("country_id"));
                columnData.put("country_code", rs.getString("country_code"));
                columnData.put("latitude", rs.getString("latitude"));
                columnData.put("longitude", rs.getString("longitude"));
                columnData.put("created_at", rs.getString("created_at"));
                columnData.put("updated_at", rs.getString("updated_at"));
                columnData.put("flag", rs.getString("flag"));
                columnData.put("wikiDataId", rs.getString("wikiDataId"));

                result.put(rs.getString("name"), columnData);
            }
            return result;
        });
    }

    public List<String> getCitiesNamesByCountryAndState(String country, String state)
    {
        String query = "SELECT c.name " +
                "FROM cities c " +
                "JOIN states s ON c.state_id = s.id " +
                "JOIN countries co ON s.country_id = co.id " +
                "WHERE co.name = ? AND s.name = ? " + " OR co.iso2 = ? AND s.iso2 = ?" +
                " OR co.name = ? AND s.iso2 = ? OR co.iso2 = ? AND s.name = ?" +
                "ORDER BY c.name ASC";

        return jdbcTemplate.query(query, new Object[]{country, state, country, state,country,state,country,state},
                (rs, rowNum) -> rs.getString("name"));
    }



    private static Map<String, String> getColumnDataMapForCountries(ResultSet rs) throws SQLException {
        Map<String, String> columnData = new LinkedHashMap<>();

        // Populate the inner map with column names and their values
        columnData.put("id", rs.getString("id"));
        columnData.put("name", rs.getString("name"));
        columnData.put("iso3", rs.getString("iso3"));
        columnData.put("numeric_code", rs.getString("numeric_code"));
        columnData.put("iso2", rs.getString("iso2"));
        columnData.put("phonecode", rs.getString("phonecode"));
        columnData.put("capital", rs.getString("capital"));
        columnData.put("currency", rs.getString("currency"));
        columnData.put("currency_name", rs.getString("currency_name"));
        columnData.put("currency_symbol", rs.getString("currency_symbol"));
        columnData.put("tld", rs.getString("tld"));
        columnData.put("native", rs.getString("native"));
        columnData.put("region", rs.getString("region"));
        columnData.put("region_id", rs.getString("region_id"));
        columnData.put("subregion", rs.getString("subregion"));
        columnData.put("subregion_id", rs.getString("subregion_id"));
        columnData.put("nationality", rs.getString("nationality"));
        columnData.put("timezones", rs.getString("timezones"));
        columnData.put("translations", rs.getString("translations"));
        columnData.put("latitude", rs.getString("latitude"));
        columnData.put("longitude", rs.getString("longitude"));
        columnData.put("emoji", rs.getString("emoji"));
        columnData.put("emojiU", rs.getString("emojiU"));
        columnData.put("created_at", rs.getString("created_at"));
        columnData.put("updated_at", rs.getString("updated_at"));
        columnData.put("flag", rs.getString("flag"));
        columnData.put("wikiDataId", rs.getString("wikiDataId"));
        return columnData;
    }

    private static void getColumnDataForStateMap(ResultSet rs, Map<String, String> columnData) throws SQLException {
        columnData.put("id", rs.getString("id"));
        columnData.put("name", rs.getString("name"));
        columnData.put("country_id", rs.getString("country_id"));
        columnData.put("country_code", rs.getString("country_code"));
        columnData.put("fips_code", rs.getString("fips_code"));
        columnData.put("iso2", rs.getString("iso2"));
        columnData.put("type", rs.getString("type"));
        columnData.put("latitude", rs.getString("latitude"));
        columnData.put("longitude", rs.getString("longitude"));
        columnData.put("created_at", rs.getString("created_at"));
        columnData.put("updated_at", rs.getString("updated_at"));
        columnData.put("flag", rs.getString("flag"));
        columnData.put("wikiDataId", rs.getString("wikiDataId"));
    }

    public LocationData getLocation(String countryName, String stateName, String cityName)
    {
        String query = "SELECT c.latitude, c.longitude " +
                "FROM cities c " +
                "JOIN states s ON c.state_id = s.id " +
                "JOIN countries co ON s.country_id = co.id " +
                "WHERE (co.name = ? AND s.name = ? AND c.name = ?) " +
                "   OR (co.iso2 = ? AND s.iso2 = ? AND c.name = ?) " +
                "ORDER BY c.name ASC";

        return jdbcTemplate.queryForObject(query,
                new Object[]{countryName, stateName, cityName, countryName, stateName, cityName},
                (rs, rowNum) -> {
                    LocationData locationData = new LocationData();
                    locationData.setLat(rs.getString("latitude"));
                    locationData.setLng(rs.getString("longitude"));
                    return locationData;
                });
    }

}
