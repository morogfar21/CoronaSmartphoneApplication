package com.example.au575154coronatracker2.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.au575154coronatracker2.Models.Country;

import java.util.List;

@Dao
public interface CountryDAO {

    @Query("SELECT * FROM country_table")
    LiveData<List<Country>> getAll();

    @Query("SELECT * FROM country_table")
    List<Country> getAllCountriesAsList();

    @Insert
    void insertCountry(Country country);

    @Query("SELECT * FROM country_table WHERE name LIKE :countryName LIMIT 1") //Second 'country' is the name
    Country findCountry(String countryName);

    //Find country by uid and name
    @Query("SELECT * FROM country_table WHERE id LIKE :id")
    Country findCountry(int id);

    @Update
    void updateCountries(Country country);

    @Delete
    void deletecountry(Country country);

    @Query("DELETE FROM country_table")
    void deleteAllCountries();

}
