package com.example.weatherclock.viewmodels;

import android.app.Application;
import android.location.Location;

import com.example.weatherclock.repositories.LocationRepository;
import com.example.weatherclock.repositories.WeatherRepository;
import com.example.weatherclock.requests.responses.WeatherResponse;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


public class WeatherClockViewModel extends AndroidViewModel {

    private WeatherRepository mWeatherRepository;
    private LocationRepository mLocationRepository;


    public WeatherClockViewModel(@NonNull Application application) {
        super(application);
        mWeatherRepository = WeatherRepository.getInstance();
        mLocationRepository = LocationRepository.getInstance(application);
    }

    public LiveData<WeatherResponse> getWeather(){
        return mWeatherRepository.getWeather();
    }
    public LiveData<Location> getLocation(){
        return mLocationRepository.getLocation();
    }

    public void getWeatherApi(float latitude, float longitude, String exclude){
        mWeatherRepository.getWeatherApi(latitude,longitude,exclude);
    }

    public void getLocationApi(){
        mLocationRepository.getLocationApi();
    }


}
