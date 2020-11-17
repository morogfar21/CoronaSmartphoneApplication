package com.example.au575154coronatracker2.Views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.example.au575154coronatracker2.Models.Country;
import com.example.au575154coronatracker2.R;
import com.example.au575154coronatracker2.Viewmodels.EditViewmodel;
import com.google.gson.Gson;

public class EditActivity extends AppCompatActivity {

    public static final String SERVICE_TASK_RESULT_COMPLETE = "SERVICE_TASK_RESULT_COMPLETE";
    public static final String EDIT_TO_DETAILS = "EditToDetails";
    public static final String DETAILS_TO_EDIT = "DetailsToEdit";
    private TextView txtviewcntryname;
    private EditText editText;
    private Button btnOk, btnCancel;
    private SeekBar seekBarRating;
    private ImageView imgviewCountry;
    private int rating;
    Country cntryobject;
    private EditViewmodel editViewmodel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // setting up variables
        editText = (EditText) findViewById(R.id.editTextUserinfo);
        btnCancel = (Button) findViewById(R.id.btncanceledit);
        btnOk = (Button) findViewById(R.id.buttonOk);
        seekBarRating = (SeekBar) findViewById(R.id.seekBarrating);
        txtviewcntryname = findViewById(R.id.txtviewcntrynameedit);
        imgviewCountry = findViewById(R.id.imgeViewCountryDetails);

        //Assignment 2
        // Setup repository and livedata for country
        Intent intent = getIntent();
        int id = intent.getIntExtra(DETAILS_TO_EDIT, -1);
        editViewmodel = new ViewModelProvider(this).get(EditViewmodel.class);

        editViewmodel.getCountry(id).observe(this, new Observer<Country>() {
            @Override
            public void onChanged(Country countries) {
                AssignCountryValues(countries);
                cntryobject = countries;
            }
        });

    }

    public void AssignCountryValues(final Country currentCountry){
        String countryCode = currentCountry.getCountryCode();
        Glide.with(imgviewCountry.getContext())
                .load("https://www.countryflags.io/" + countryCode + "/flat/64.png")
                .placeholder(R.drawable.ic_launcher_background)
                .into(imgviewCountry);

        editText.setText(currentCountry.getNotes());
        txtviewcntryname.setText(currentCountry.getName());


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GotoMainActivity();
                finish();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save(currentCountry);
            }
        });

        //Seekbar setup
        seekBarRating.setMax(10);
        final TextView seekBarValue = (TextView) findViewById(R.id.seekbarvaluetxtview);
        seekBarValue.setText(currentCountry.getRating());
        seekBarRating.setProgress(Integer.parseInt(currentCountry.getRating()));

        seekBarRating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rating  = progress;
                seekBarValue.setText(String.valueOf(rating));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }


    private void GotoMainActivity() {
        setResult(RESULT_CANCELED);
    }

    private void Save(Country country) {
        String countryRating = String.valueOf(rating);
        String countryNotes = "" + editText.getText().toString();

        editViewmodel.updateNoteAndRating(countryNotes, countryRating, country.getId());
        Intent intent = new Intent();
        intent.putExtra(EDIT_TO_DETAILS, country.getName());

        setResult(RESULT_OK, intent);
        finish();
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