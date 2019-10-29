package com.example.weatherclock.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.weatherclock.R;
import com.example.weatherclock.models.WeatherData;
import com.example.weatherclock.requests.ServiceGenerator;
import com.example.weatherclock.requests.WeatherApi;
import com.example.weatherclock.requests.responses.WeatherResponse;
import com.example.weatherclock.util.Constants;
import com.example.weatherclock.viewmodels.WeatherClockViewModel;
import com.example.weatherclock.views.customViews.ClockView;
import com.github.pwittchen.weathericonview.WeatherIconView;
import com.thbs.skycons.library.CloudView;
import com.thbs.skycons.library.SkyconView;
import com.thbs.skycons.library.SunView;
import com.thbs.skycons.library.WindView;

import java.io.IOException;
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            checkPermission();
        }

        initViews();

        textView = findViewById(R.id.textView);

        mWeatherClockViewModel = ViewModelProviders.of(this).get(WeatherClockViewModel.class);
        subscribeObservers();


        findViewById(R.id.gpsfab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });
        findViewById(R.id.timefab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWeatherClockViewModel.getLocationApi();

            }
        });

        testRetrofitRequest();
    }

    private void subscribeObservers(){
        mWeatherClockViewModel.getWeather().observe(this, new Observer<WeatherResponse>() {
            @Override
            public void onChanged(WeatherResponse weatherResponse) {
                Log.d(TAG, "onChanged: "+ weatherResponse.toString());
                try{
                    updateWeatherUI(weatherResponse);
                }catch(Exception e){
                    e.printStackTrace();
                }
                //textView.setText(weatherResponse.toString());
            }
        });

        mWeatherClockViewModel.getLocation().observe(this, new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                Log.d(TAG, "onChanged: WeatherData Changed");

                textView.setText("Latitude "+ (location.getLatitude())+ " Longitude " + (location.getLongitude()));
            }
        });
    }

    private void getWeatherApi(float latitude, float longitude, String exclude){
        mWeatherClockViewModel.getWeatherApi(latitude,longitude,exclude);
    }

    private void testRetrofitRequest() {

        float latitude = (float) 48.137154;
        float longitude = (float) 11.576124;
        String exclude = "daily,alerts,flags";

        getWeatherApi(latitude,longitude,exclude);


    }


    private void updateWeatherUI(WeatherResponse weatherResponse){
        String tempSetting = "F";
        WeatherData[] hourData = weatherResponse.getHourly().getData();
        WeatherData current = weatherResponse.getCurrently();
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);

        degreeTextViews[12].setText(Math.round(current.getTemperature())+tempSetting);
        weatherIconViews[12].setIconResource(translateWeatherIcon(current.getIcon()));
        for(int i = 0; i<12; i++){
            int j = (i+hour)%12;
            degreeTextViews[j].setText(Math.round(hourData[i].getTemperature())+tempSetting);
            weatherIconViews[j].setIconResource(translateWeatherIcon(hourData[i].getIcon()));
        }
    }

    private String translateWeatherIcon(String sourceIcon){
        String translatedIcon = "";

        switch(sourceIcon){
            case "clear-day": translatedIcon = getString(R.string.wi_day_sunny);
                break;
            case "clear-night": translatedIcon = getString(R.string.wi_night_clear);
                break;
            case "rain": translatedIcon = getString(R.string.wi_rain);
                break;
            case "snow": translatedIcon = getString(R.string.wi_snow);
                break;
            case "sleet": translatedIcon = getString(R.string.wi_sleet);
                break;
            case "wind": translatedIcon = getString(R.string.wi_strong_wind);
                break;
            case "fog": translatedIcon = getString(R.string.wi_fog);
                break;
            case "cloudy": translatedIcon = getString(R.string.wi_cloudy);
                break;
            case "partly-cloudy-day": translatedIcon = getString(R.string.wi_day_cloudy);
                break;
            case "partly-cloudy-night": translatedIcon = getString(R.string.wi_night_cloudy);
                break;
            default:

        }

        return translatedIcon;
    }


    private void initViews(){
        degreeTextViews = new TextView[13];
        weatherIconViews = new WeatherIconView[13];
        int[] tvIDs = {R.id.textView0,R.id.textView1,R.id.textView2,R.id.textView3,R.id.textView4,R.id.textView5,R.id.textView6,R.id.textView7,R.id.textView8,R.id.textView9,R.id.textView10,R.id.textView11,R.id.textView12};
        int[] wiIDs = {R.id.my_weather_icon0,R.id.my_weather_icon1,R.id.my_weather_icon2,R.id.my_weather_icon3,R.id.my_weather_icon4,R.id.my_weather_icon5,R.id.my_weather_icon6,R.id.my_weather_icon7,R.id.my_weather_icon8,R.id.my_weather_icon9,R.id.my_weather_icon10,R.id.my_weather_icon11,R.id.my_weather_icon12};

        for(int i = 0; i<13; i++) {
            degreeTextViews[i] = findViewById(tvIDs[i]);
            weatherIconViews[i] = findViewById(wiIDs[i]);
        }
    }

    public void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ){//Can add more as per requirement

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
        }
    }
}
