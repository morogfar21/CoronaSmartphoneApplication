package com.example.au575154coronatracker2.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "country_table")
public class Country {

    @PrimaryKey(autoGenerate = true)
    public int id;
    @SerializedName("Country")
    @Expose
    public String name;
    public String imageResourceId;
    @SerializedName("NewConfirmed")
    @Expose
    public String cases;
    @SerializedName("TotalDeaths")
    @Expose
    public String deaths;
    @SerializedName("Rating")
    @Expose
    public String rating;
    @SerializedName("Notes")
    @Expose
    public String notes;
    @SerializedName("CountryCode")
    @Expose
    public String countryCode;

    public Country(String name, String cases, String deaths, String imageResourceId, String notes,
                   String rating, String countryCode){
        this.name = name;
        this.cases = cases;
        this.deaths = deaths;
        this.imageResourceId = imageResourceId;
        this.notes = notes;
        this.rating = rating;
        this.countryCode = countryCode;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {return  id;}

    public String getName() {
        return name;
    }

    public String getCases() {
        return cases;
    }

    public String getDeaths() {
        return deaths;
    }

    public String getRating() {
        return rating;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNotes() { return notes; }

    public String getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(String imageResourceId) {
        this.imageResourceId = imageResourceId;
    }
}