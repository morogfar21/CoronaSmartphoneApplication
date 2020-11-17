package com.example.au575154coronatracker2.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.au575154coronatracker2.Models.Country;

@Database(entities = {Country.class}, version = 3)

public abstract class CountryDatabase extends RoomDatabase {

    public abstract CountryDAO countryDAO();
    private static CountryDatabase instance;

    public static CountryDatabase getDatabase(final Context context){
        if (instance == null){
            synchronized (CountryDatabase.class){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    CountryDatabase.class, "country_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();
            }
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };


    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{
        private CountryDAO countryDAO;

        private PopulateDbAsyncTask(CountryDatabase db){
            countryDAO = db.countryDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //countryDAO.updateCountries((Country) countryDAO);

            countryDAO.insertCountry(new Country("Moldavia", "20", "40",
                    "1","Small country", "1","mk"));
            countryDAO.insertCountry(new Country("Hungary", "200", "54",
                    "2","Small country as well", "2","hu"));
            countryDAO.insertCountry(new Country("Cyprus", "26", "12",
                    "4","island", "4","cy"));
            return null;
        }
    }

}
