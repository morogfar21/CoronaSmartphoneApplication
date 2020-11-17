package com.example.au575154coronatracker2.Viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.au575154coronatracker2.Models.Country;
import com.example.au575154coronatracker2.Models.CountryRepository;

import java.util.List;

public class DetailsViewmodel extends AndroidViewModel {

    private CountryRepository repository;
    private MutableLiveData<List<Country>> countries;


    public DetailsViewmodel(@NonNull Application application) {
        super(application);
        repository = CountryRepository.getInstance(application);
    }

    public void delete(Country country){
        repository.delete(country);
    }

    public LiveData<Country> getCountry(int id){
        if (countries == null) {
            countries = new MutableLiveData<List<Country>>();
            // Mutable is needed here to setvalue of country by id
            MutableLiveData<Country> liveData = new MutableLiveData<Country>();

            Country country = repository.getCountryAsync(id);
            liveData.setValue(country);
            return liveData;
        }

        return null;
    }

}
