package com.example.weatherclock.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.weatherclock.R;
import com.example.weatherclock.models.WeatherData;
import com.example.weatherclock.requests.responses.WeatherResponse;
import com.example.weatherclock.util.UIUpdateNotifier;
import com.example.weatherclock.viewmodels.WeatherClockViewModel;
import com.example.weatherclock.views.customViews.ClockView;
import com.github.pwittchen.weathericonview.WeatherIconView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private WeatherClockViewModel mWeatherClockViewModel;

    private static TextView textView;
    private static TextView[] degreeTextViews;
    private static WeatherIconView[] weatherIconViews;
    private static ClockView clockView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        }

        initViews();

        mWeatherClockViewModel = ViewModelProviders.of(this).get(WeatherClockViewModel.class);
        subscribeObservers();

        initOnClickListeners();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mWeatherClockViewModel.enableTimerTask();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWeatherClockViewModel.disableTimerTask();
    }

    private void subscribeObservers() {
        mWeatherClockViewModel.getWeather().observe(this, new Observer<WeatherResponse>() {
            @Override
            public void onChanged(WeatherResponse weatherResponse) {

                try {
                    Log.d(TAG, "onChanged: " + weatherResponse.toString());
                    updateWeatherUI(weatherResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //textView.setText(weatherResponse.toString());
            }
        });

        mWeatherClockViewModel.getLocationLiveData().observe(this, new Observer<Location>() {
            @Override
            public void onChanged(Location locationArg) {
                Log.d(TAG, "onChanged: WeatherData Changed");
                mWeatherClockViewModel.setLocation(locationArg);
                // textView.setText("Latitude "+ (mWeatherClockViewModel.getLocation().getLatitude())+ " Longitude " + (mWeatherClockViewModel.getLocation().getLongitude()));
            }
        });

        mWeatherClockViewModel.getUiUpdateNotifierMutableLiveData().observe(this, new Observer<UIUpdateNotifier>() {
            @Override
            public void onChanged(UIUpdateNotifier uiUpdateNotifier) {
                Log.d(TAG, "onChanged: UIUpdatenotifier On Changed");
                makeWeatherApiCall();
            }
        });
    }

    private void getWeatherApi(float latitude, float longitude) {
        mWeatherClockViewModel.getWeatherApi(latitude, longitude);
    }

    private void makeWeatherApiCall() {
        mWeatherClockViewModel.getLocationApi();
        try {
            Log.d(TAG, "makeWeatherApiCall: Location used");
            getWeatherApi((float) mWeatherClockViewModel.getLocation().getLatitude(), (float) mWeatherClockViewModel.getLocation().getLongitude());
        } catch (RuntimeException e) {
            Log.d(TAG, "makeWeatherApiCall: Use hardcoded Coordinates");
            getWeatherApi((float) 48.137154, (float) 11.576124);
        }
    }


    private void updateWeatherUI(WeatherResponse weatherResponse) {
        WeatherData[] hourData = weatherResponse.getHourly().getData();
        WeatherData current = weatherResponse.getCurrently();
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        String tempSetting = mWeatherClockViewModel.getUnitDisplay();

        degreeTextViews[12].setText(Math.round(current.getTemperature()) + tempSetting);
        weatherIconViews[12].setIconResource(mWeatherClockViewModel.translateWeatherIcon(current.getIcon()));
        for (int i = 0; i < 12; i++) {
            int j = (i + hour) % 12;
            degreeTextViews[j].setText(Math.round(hourData[i].getTemperature()) + tempSetting);
            weatherIconViews[j].setIconResource(mWeatherClockViewModel.translateWeatherIcon(hourData[i].getIcon()));
        }
        mWeatherClockViewModel.setWeatherResponseTime(current.getTime());
        clockView.reDraw();
    }

    private void initOnClickListeners() {
        findViewById(R.id.gpsfab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });
        findViewById(R.id.refreshFab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeWeatherApiCall();

            }
        });
        findViewById(R.id.degreeFab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWeatherClockViewModel.toggleUnit();
                makeWeatherApiCall();
            }
        });
    }


    private void initViews() {
        degreeTextViews = new TextView[13];
        weatherIconViews = new WeatherIconView[13];
        int[] tvIDs = {R.id.textView0, R.id.textView1, R.id.textView2, R.id.textView3, R.id.textView4, R.id.textView5, R.id.textView6, R.id.textView7, R.id.textView8, R.id.textView9, R.id.textView10, R.id.textView11, R.id.textView12};
        int[] wiIDs = {R.id.my_weather_icon0, R.id.my_weather_icon1, R.id.my_weather_icon2, R.id.my_weather_icon3, R.id.my_weather_icon4, R.id.my_weather_icon5, R.id.my_weather_icon6, R.id.my_weather_icon7, R.id.my_weather_icon8, R.id.my_weather_icon9, R.id.my_weather_icon10, R.id.my_weather_icon11, R.id.my_weather_icon12};

        for (int i = 0; i < 13; i++) {
            degreeTextViews[i] = findViewById(tvIDs[i]);
            weatherIconViews[i] = findViewById(wiIDs[i]);
        }

        textView = findViewById(R.id.textView);
        clockView = findViewById(R.id.clockView);
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {//Can add more as per requirement

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
        }
    }

}
