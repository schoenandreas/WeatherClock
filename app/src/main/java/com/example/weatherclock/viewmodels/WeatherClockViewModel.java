package com.example.weatherclock.viewmodels;

import android.app.Application;
import android.location.Location;
import android.util.Log;

import com.example.weatherclock.R;
import com.example.weatherclock.repositories.LocationRepository;
import com.example.weatherclock.repositories.WeatherRepository;
import com.example.weatherclock.requests.responses.WeatherResponse;
import com.example.weatherclock.util.App;
import com.example.weatherclock.util.UIUpdateNotifier;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


public class WeatherClockViewModel extends AndroidViewModel {

    private static final String TAG = "WeatherClockViewModel";

    private WeatherRepository mWeatherRepository;
    private LocationRepository mLocationRepository;

    private Location location;
    private long weatherResponseTime = 0;
    private String unit = "si";
    private String exclude = "daily,alerts,flags";

    private Timer timer = new Timer();
    private TimerTask timerTask;
    private MutableLiveData<UIUpdateNotifier> uiUpdateNotifierMutableLiveData = new MutableLiveData<>();



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

    public void disableTimerTask() {
        timerTask.cancel();
    }

    public void enableTimerTask() {
        long periodToRepeat = 60 * 60 * 1000; /* 1 hour  */
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "run: Update!");
                update();
            }
        };
        timer.schedule(timerTask, delay(), periodToRepeat);
        if (weatherResponseIsOutdated()) {
            update();
        }
    }

    private void update() {
        UIUpdateNotifier uiUpdateNotifier = UIUpdateNotifier.getInstance();
        uiUpdateNotifierMutableLiveData.postValue(uiUpdateNotifier);
    }

    private long delay() {
        Calendar calendar = Calendar.getInstance();
        long secDelay = 60 - calendar.get(Calendar.SECOND);
        long minDelay = 60 - calendar.get(Calendar.MINUTE);
        long delay = ((minDelay - 1) * 60 + secDelay) * 1000;
        //long testDelay = secDelay*1000;
        return delay;
    }

    private boolean weatherResponseIsOutdated() {

        Calendar calendar = Calendar.getInstance();
        long time = calendar.getTimeInMillis() / 1000;
        long timeHour = time - calendar.get(Calendar.SECOND) - calendar.get(Calendar.MINUTE) * 60;
        //  long timeMinute = time - calendar.get(Calendar.SECOND);
        if (timeHour > weatherResponseTime) {
            Log.d(TAG, "weatherResponseIsOutdated");
            return true;
        } else {
            Log.d(TAG, "weatherResponseIsOutdated NOT");
            return false;
        }
    }

    public MutableLiveData<UIUpdateNotifier> getUiUpdateNotifierMutableLiveData() {
        return uiUpdateNotifierMutableLiveData;
    }

    public void setWeatherResponseTime(long weatherResponseTime) {
        this.weatherResponseTime = weatherResponseTime;
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
