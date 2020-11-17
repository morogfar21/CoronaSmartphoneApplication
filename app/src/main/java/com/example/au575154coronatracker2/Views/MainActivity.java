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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.au575154coronatracker2.CSVFile;
import com.example.au575154coronatracker2.FileAdapter;
import com.example.au575154coronatracker2.Models.Country;
import com.example.au575154coronatracker2.Models.CountryRepository;
import com.example.au575154coronatracker2.R;
import com.example.au575154coronatracker2.Viewmodels.MainActivityViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements FileAdapter.ICountryItemClickedListener {

    public static final String SERVICE_TASK_RESULT_COMPLETE = "SERVICE_TASK_RESULT_COMPLETE";
    public static final String KEY_SLEEP_TIME = "KEY_SLEEP_TIME";

    public static final String MAIN_TO_DETAILS = "MAIN_TO_DETAILS";

    //Assignment2 adds
    private MainActivityViewModel mainactivityViewmodel;

    private Button exitBtn, addbtn;
    private EditText search;
    FileAdapter fileAdapter;

    private static final int R_C_details = 1;
    private CountryRepository repository;

    //Code inspiration:
    //https://google-developer-training.github.io/android-developer-advanced-course-practicals/unit-6-working-with-architecture-components/lesson-14-room,-livedata,-viewmodel/14-1-a-room-livedata-viewmodel/14-1-a-room-livedata-viewmodel.html#task10intro

    //Youtube guide playlist:
    //https://www.youtube.com/watch?v=JLwW5HivZg4&list=PLrnPJCHvNZuDihTpkRs6SpZhqgBqPU118&index=5&ab_channel=CodinginFlow

    //Lecture solutions
    //L4, L5 & L6

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //references
        exitBtn = (Button) findViewById(R.id.exitButton);
        search = findViewById(R.id.edittxtmainsearch);
        addbtn = findViewById(R.id.btnAddMain);

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get search info from user
                String searchdata = search.getText().toString().toLowerCase();
                String cntryShortName = searchdata.substring(0,1)
                        .toUpperCase() + searchdata.substring(1);

                int result = mainactivityViewmodel.search(cntryShortName);
                if (result == 1){
                    Toast toast = Toast.makeText(getApplicationContext(), cntryShortName +" is already added.",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
                if (result == 2){
                    Toast toast = Toast.makeText(getApplicationContext(), cntryShortName +"  was added.",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
                if (result == 3){
                    Toast toast = Toast.makeText(getApplicationContext(), "No data from country name",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });


        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //Assignment 2
        // Setup repository and livedata for country
        mainactivityViewmodel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        mainactivityViewmodel.getCountry().observe(this, new Observer<List<Country>>() {
            @Override
            public void onChanged(List<Country> countries) {
                        fileAdapter.updateList(countries);
            }
        });

        // set up the RecyclerView
        fileAdapter = new FileAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(fileAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        startForegroundService();

    }
        //int indexhack;
        @Override
        public void onCountryClicked(int index) {
        Country country = mainactivityViewmodel.getCountry(index);
        startActivityForResult(country);
        }

    private void startActivityForResult(Country country) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(MAIN_TO_DETAILS, country.getId());
        startActivityForResult(intent,R_C_details );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == R_C_details) {
            if (resultCode == RESULT_OK) {
                mainactivityViewmodel.getCountry();
            }
        }
    }

    private void startForegroundService(){
            Intent foregroundService = new Intent(this, NotificationService.class);
            foregroundService.putExtra(KEY_SLEEP_TIME, 60000);
            startService(foregroundService);
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
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
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