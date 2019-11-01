package com.example.weatherclock.viewmodels;

import android.app.Application;
import android.location.Location;

import com.example.weatherclock.R;
import com.example.weatherclock.repositories.LocationRepository;
import com.example.weatherclock.repositories.WeatherRepository;
import com.example.weatherclock.requests.responses.WeatherResponse;
import com.example.weatherclock.util.App;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


public class WeatherClockViewModel extends AndroidViewModel {

    private WeatherRepository mWeatherRepository;
    private LocationRepository mLocationRepository;

    private Location location;
    private String unit = "si";
    private String exclude = "daily,alerts,flags";


    public WeatherClockViewModel(@NonNull Application application) {
        super(application);
        mWeatherRepository = WeatherRepository.getInstance();
        mLocationRepository = LocationRepository.getInstance(application);

    }

    public LiveData<WeatherResponse> getWeather() {
        return mWeatherRepository.getWeather();
    }

    public LiveData<Location> getLocationLiveData() {
        return mLocationRepository.getLocation();
    }

    public void getWeatherApi(float latitude, float longitude) {
        mWeatherRepository.getWeatherApi(latitude, longitude, exclude, unit);
    }

    public void getLocationApi() {
        mLocationRepository.getLocationApi();
    }


    public void toggleUnit() {
        if (unit.equals("si")) {
            unit = "us";
        } else if (unit.equals("us")) {
            unit = "si";
        }
    }

    public String getUnitDisplay() {
        if (unit.equals("si")) {
            return "C";
        } else if (unit.equals("us")) {
            return "F";
        }
        return "";
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return this.location;
    }

    public String translateWeatherIcon(String sourceIcon) {
        String translatedIcon = "";

        switch (sourceIcon) {
            case "clear-day":
                translatedIcon = App.getRes().getString(R.string.wi_day_sunny);
                break;
            case "clear-night":
                translatedIcon = App.getRes().getString(R.string.wi_night_clear);
                break;
            case "rain":
                translatedIcon = App.getRes().getString(R.string.wi_rain);
                break;
            case "snow":
                translatedIcon = App.getRes().getString(R.string.wi_snow);
                break;
            case "sleet":
                translatedIcon = App.getRes().getString(R.string.wi_sleet);
                break;
            case "wind":
                translatedIcon = App.getRes().getString(R.string.wi_strong_wind);
                break;
            case "fog":
                translatedIcon = App.getRes().getString(R.string.wi_fog);
                break;
            case "cloudy":
                translatedIcon = App.getRes().getString(R.string.wi_cloudy);
                break;
            case "partly-cloudy-day":
                translatedIcon = App.getRes().getString(R.string.wi_day_cloudy);
                break;
            case "partly-cloudy-night":
                translatedIcon = App.getRes().getString(R.string.wi_night_cloudy);
                break;
            default:

        }

        return translatedIcon;
    }


}
