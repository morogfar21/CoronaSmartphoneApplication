package com.example.au575154coronatracker2.Models;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.au575154coronatracker2.Database.CountryDAO;
import com.example.au575154coronatracker2.Database.CountryDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CountryRepository {

    private CountryDatabase db;
    private ExecutorService executorService;
    private LiveData<List<Country>> countries;
    private List<Country> servicedata;
    private CountryDAO countryDAO;
    RequestQueue queue;
    private final String APIurl = "https://api.covid19api.com/summary ";
    private static CountryRepository instance;

    public static CountryRepository getInstance(Application app) {
        if (instance == null){
            instance = new CountryRepository(app);
        }
        return instance;
    }

    private CountryRepository (Application app) {
        db = CountryDatabase.getDatabase(app);
        executorService = Executors.newSingleThreadExecutor();
        countryDAO = db.countryDAO();
        countries = countryDAO.getAll();
        queue = Volley.newRequestQueue(app.getApplicationContext());
    }

    public void insert(Country country){
        new InsertCountryAsyncTask(countryDAO).execute(country);
    }

    public void update(Country country){
        new UpdateCountryAsyncTask(countryDAO).execute(country);
    }

    public void delete(Country country){
        new DeleteCountryAsyncTask(countryDAO).execute(country);
    }


    public LiveData<List<Country>> getCountries() {

        return countries;
    }

    public Country search(String searchdata) {
        Log.d("Search", "Searching Countries");
        for(Country country : servicedata)
        {
            if(country.getName().equalsIgnoreCase(searchdata)){
                return country;
            }

        }

        return null;
    }

    public List<Country> service(){
        getApiData();
        return servicedata;
    }

    public List<Country> getCountriesAsList(){
        Future<List<Country>> cntrys = executorService.submit(new Callable<List<Country>>() {
            @Override
            public List<Country> call() { return countryDAO.getAllCountriesAsList(); }
        });

        try {
            return cntrys.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Country findCountry(final String countryName){
        final Future<Country> country = executorService.submit(new Callable<Country>() {
            @Override
            public Country call() {
                Log.d("findCountry", " Finding country with name specified by" +
                        "user");
                return countryDAO.findCountry(countryName); }
            });

            try {
                return country.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (
            ExecutionException e) {
                e.printStackTrace();
            }
        return null;
    }

    public Country getCountryAsync(final int id) {
        Future<Country> cty = executorService.submit(new Callable<Country>() {
            @Override
            public Country call() { return countryDAO.findCountry(id); }
        });

        try {
            return cty.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static class InsertCountryAsyncTask extends AsyncTask<Country, Void, Void> {
        private CountryDAO countryDAO;

        private InsertCountryAsyncTask(CountryDAO countryDAO){
            this.countryDAO = countryDAO;
        }

        @Override
        protected Void doInBackground(Country... countries) {
            countryDAO.insertCountry(countries[0]);
            return null;
        }
    }

    private static class UpdateCountryAsyncTask extends AsyncTask<Country, Void, Void> {
        private CountryDAO countryDAO;

        private UpdateCountryAsyncTask(CountryDAO countryDAO){
            this.countryDAO = countryDAO;
        }

        @Override
        protected Void doInBackground(Country... countries) {
            countryDAO.updateCountries(countries[0]);
            return null;
        }
    }

    private static class DeleteCountryAsyncTask extends AsyncTask<Country, Void, Void> {
        private CountryDAO countryDAO;

        private DeleteCountryAsyncTask(CountryDAO countryDAO){
            this.countryDAO = countryDAO;
        }

        @Override
        protected Void doInBackground(Country... countries) {
            countryDAO.deletecountry(countries[0]);
            return null;
        }
    }

    //Web API Section
    private void getApiData(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIurl,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // display response
                        Log.d("API", "Making API request:");
                        Log.d("Response", response);
                        parseJson(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        );
        queue.add(stringRequest);
    }

    private void parseJson(String response){
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Countries  countryResponse = gson.fromJson(response, Countries.class);
        servicedata = countryResponse.getCountries();
    }

}
