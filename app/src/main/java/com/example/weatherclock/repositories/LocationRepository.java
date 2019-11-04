package com.example.weatherclock.repositories;

import android.app.Activity;
import android.app.Application;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class LocationRepository {


    private static final String TAG = "LocationRepository";

    private static LocationRepository instance;
    private MutableLiveData<Location> mLocation;
    private FusedLocationProviderClient fusedLocationClient;
    //private Application application;



    private LocationRepository(Application application) {
       // this.application = application;
        mLocation = new MutableLiveData<>();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(application);
    }

    public static LocationRepository getInstance(Application application) {
        if (instance == null) {
            instance = new LocationRepository(application);
        }
        return instance;
    }

    public LiveData<Location> getLocation() {
        return mLocation;
    }

    public void getLocationApi(){

        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    mLocation.postValue(location);
                    Log.d(TAG, "Location not null ");
                }else{
                    Log.d(TAG, "Location null ");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Failure ");
                e.printStackTrace();
            }
        }).addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Log.d(TAG, "Complete ");
            }
        });
    }

}
