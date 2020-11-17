package com.example.au575154coronatracker2.Views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.example.au575154coronatracker2.Models.Country;
import com.example.au575154coronatracker2.R;
import com.example.au575154coronatracker2.Viewmodels.DetailsViewmodel;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {

    public static final String SERVICE_TASK_RESULT_COMPLETE = "SERVICE_TASK_RESULT_COMPLETE";
    public static final String EDIT_TO_DETAILS = "EditToDetails";
    public static final String DETAILS_TO_MAIN = "DetailsToMain";
    public static final String MAIN_TO_DETAILS = "MAIN_TO_DETAILS";

    private TextView txtviewcases, txtviewdeaths, txtviewuserrating,
            txtviewnotes, txtviewcntryname;
    private ImageView imgviewCountry;
    private Button btnback, btnedit, btndelete;
    private Country cntryobject;
    private static final int R_C_edit = 2;
    private ArrayList<Country> Allcountries;
    private DetailsViewmodel detailsactivityViewmodel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //setup references
        txtviewcases = (TextView) findViewById(R.id.txtviewCasesdetails);
        txtviewdeaths = (TextView) findViewById(R.id.txtviewdeathsdetails);
        txtviewuserrating = (TextView) findViewById(R.id.txtviewuserratingdetails);
        txtviewnotes = (TextView) findViewById(R.id.txtviewusernotesdetails);
        txtviewcntryname = findViewById(R.id.txtviewcntrynamedetails);

        imgviewCountry = (ImageView) findViewById(R.id.imgeViewCountryDetails);
        btnback = (Button) findViewById(R.id.btnbackdetails);
        btnedit = (Button) findViewById(R.id.btneditdetails);
        btndelete = findViewById(R.id.btndeletedetails);

        //Assignment 2
        // Setup livedata for country
        Intent intent = getIntent();
        int id = intent.getIntExtra(MAIN_TO_DETAILS, -1);
        detailsactivityViewmodel = new ViewModelProvider(this).get(DetailsViewmodel.class);

        detailsactivityViewmodel.getCountry(id).observe(this, new Observer<Country>() {
            @Override
            public void onChanged(Country country) {
                AssignCountryValues(country);
                cntryobject = country;
            }
        });


        if (savedInstanceState != null)        {
            cntryobject.notes = savedInstanceState.getString("Notes");
            cntryobject.rating = savedInstanceState.getString("Ratings");
        }

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gobacktoMain();
                finish();
            }
        });


    }

    public void AssignCountryValues(final Country currentCountry){
        String countryCode = currentCountry.getCountryCode();
        Glide.with(imgviewCountry.getContext())
                .load("https://www.countryflags.io/" + countryCode + "/flat/64.png")
                .placeholder(R.drawable.ic_launcher_background)
                .into(imgviewCountry);

        txtviewcases.setText("Cases: " + currentCountry.getCases());
        txtviewdeaths.setText("Deaths: " + currentCountry.getDeaths());
        txtviewuserrating.setText("Rating: "+ currentCountry.getRating());
        txtviewcntryname.setText(currentCountry.getName());
        txtviewnotes.setText(currentCountry.getNotes());

        btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(currentCountry);
            }
        });

        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailsactivityViewmodel.delete(currentCountry);
                Intent intent = new Intent(DetailsActivity.this, MainActivity.class);
                intent.putExtra(DETAILS_TO_MAIN, currentCountry.getName());
                setResult(5,intent);
                finish();
            }
        });

    }

    private void startActivityForResult(Country country) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("DetailsToEdit", country.getId());
        startActivityForResult(intent, R_C_edit);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == R_C_edit){
            if (resultCode == RESULT_OK){
                String countryUpdate = data.getStringExtra(EDIT_TO_DETAILS);
                Intent intent = new Intent(DetailsActivity.this, MainActivity.class);
                intent.putExtra(DETAILS_TO_MAIN, countryUpdate);
                setResult(RESULT_OK, intent);
                finish();
            }
        }

    }

    public void gobacktoMain(){
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle bundle) {
        //bundle.putString("Notes", country.notes);
        //bundle.putString("Ratings", country.rating);
        super.onSaveInstanceState(bundle);
    }

    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter();
        filter.addAction(SERVICE_TASK_RESULT_COMPLETE);
        LocalBroadcastManager.getInstance(this).registerReceiver(foregroundServiceResult, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(foregroundServiceResult);
    }

    private void handleResult(String result){
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
    }

    private BroadcastReceiver foregroundServiceResult = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra("EXTRA_BROADCAST_RESULT");
            if(result != null){
                handleResult(result);
            }
        }
    };
}