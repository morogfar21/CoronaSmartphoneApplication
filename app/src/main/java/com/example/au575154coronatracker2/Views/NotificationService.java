package com.example.au575154coronatracker2.Views;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.au575154coronatracker2.Models.Country;
import com.example.au575154coronatracker2.Models.CountryRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotificationService extends Service {

    public static final int Notification_Id = 1;
    public static final String Service_Channel = "serviceChannel";
    public static final String SERVICE_TASK_RESULT_COMPLETE = "SERVICE_TASK_RESULT_COMPLETE";
    public static final String EXTRA_BROADCAST_RESULT = "EXTRA_BROADCAST_RESULT";
    public static final String KEY_SLEEP_TIME = "KEY_SLEEP_TIME";

    boolean started = false;
    int count, i ;
    int sleepTime;
    ExecutorService executorService;
    CountryRepository repository;
    Country notifyCountry;
    List<Country> countries;

    public NotificationService(){
        repository = CountryRepository.getInstance(this.getApplication());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        count = 0;
    }

    // Update of data every minute from API and update notification.
    // The service will call repository

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        sleepTime = intent.getIntExtra(KEY_SLEEP_TIME,60000);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(Service_Channel,
                    "NotificationService", NotificationManager.IMPORTANCE_LOW);
            NotificationManager notificationManager = (NotificationManager) getSystemService(
                    Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Notification notification = new NotificationCompat.Builder(this, Service_Channel)
                .setContentTitle("Notification Service information")
                .setContentText("Country: ")
                .build();
        startForeground(Notification_Id, notification);


        CallApi();
        return START_STICKY;
    }

    private void CallApi(){
        if (!started){
            started = true;
        }
        updateDataFromAPI();
    }

    private void updateDataFromAPI(){
        if (executorService == null){
            executorService = Executors.newSingleThreadExecutor();
        }

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    //API Get call every 60 sec.
                    Thread.sleep(sleepTime);
                    getcountries();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //sendNotificationResult("Data has been updated for Country: ");
                sendNotificationResult("Data has been updated for Country: " +
                        notifyCountry.getName() + " New cases: " + notifyCountry.getCases());

                if (started){
                    updateDataFromAPI();
                }
            }
        });
    }
    private void getcountries(){
        repository.getCountries();
        countries = repository.service();
        if(i > countries.size()){
         i = 0;
        }

        notifyCountry = countries.get(i);
        i++;
    }

    private void sendNotificationResult(String result){
    Intent broadcast = new Intent();
    broadcast.setAction(SERVICE_TASK_RESULT_COMPLETE);
    broadcast.putExtra(EXTRA_BROADCAST_RESULT, result);
    LocalBroadcastManager.getInstance(this).sendBroadcast(broadcast);
    }

    @Override
    public void onDestroy() {
        started = false;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}