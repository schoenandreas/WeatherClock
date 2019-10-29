package com.example.weatherclock.repositories;

import com.example.weatherclock.requests.WeatherApiClient;
import com.example.weatherclock.requests.responses.WeatherResponse;

import androidx.lifecycle.LiveData;


public class WeatherRepository {

    private static WeatherRepository instance;
    private WeatherApiClient mWeatherApiClient;

    private WeatherRepository() {
        mWeatherApiClient = WeatherApiClient.getInstance();

    }

    public static WeatherRepository getInstance() {
        if (instance == null) {
            instance = new WeatherRepository();
        }
        return instance;
    }

    public LiveData<WeatherResponse> getWeather() {
        return mWeatherApiClient.getWeather();
    }

    public void getWeatherApi(float latitude, float longitude, String exclude){
        mWeatherApiClient.getWeatherApi(latitude,longitude,exclude);
    }
}
