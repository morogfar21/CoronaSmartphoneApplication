package com.example.au575154coronatracker2.Viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.au575154coronatracker2.Models.Country;
import com.example.au575154coronatracker2.Models.CountryRepository;

import java.util.ArrayList;
import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {
    private CountryRepository repository;
    private LiveData<List<Country>> country;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        repository = CountryRepository.getInstance(application);
    }

    public LiveData<List<Country>> getCountry() {
        if (country == null){
            country = repository.getCountries();
        }
        return country;
    }

    public int search(String query){
        if (repository.search(query) != null){
            Country cntry = repository.search(query);

            if(repository.findCountry(query) != null){
                return 1;
            }
            cntry.setNotes("");
            cntry.setRating("0");
            repository.insert(cntry);
            country = repository.getCountries();

            return 2;
        }
        //No similar data in list.
        return 3;
    }

    public Country getCountry(int index) {

        List<Country> countryList = repository.getCountriesAsList();

        ArrayList<Country> ctyArrList = (ArrayList) countryList;
        Country cty = ctyArrList.get(index);
        return cty;
    }

}
