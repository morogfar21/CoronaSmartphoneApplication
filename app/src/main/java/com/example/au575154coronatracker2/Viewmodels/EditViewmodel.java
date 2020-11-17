package com.example.au575154coronatracker2.Viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.au575154coronatracker2.Models.Country;
import com.example.au575154coronatracker2.Models.CountryRepository;

import java.net.MalformedURLException;
import java.util.List;
import java.util.zip.CheckedOutputStream;

public class EditViewmodel extends AndroidViewModel {

    private CountryRepository repository;
    private MutableLiveData<List<Country>> countries;

    public EditViewmodel(@NonNull Application application) {
        super(application);
        repository = CountryRepository.getInstance(application);
    }

    public void updateNoteAndRating(String notes, String rating, int id) {
        Country country = repository.getCountryAsync(id);
        country.setNotes(notes);
        country.setRating(rating);

        repository.update(country);
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
