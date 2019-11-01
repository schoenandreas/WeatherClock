package com.example.weatherclock.requests;

import android.util.Log;

import com.example.weatherclock.requests.responses.WeatherResponse;
import com.example.weatherclock.util.AppExecutors;
import com.example.weatherclock.util.Constants;

import java.io.IOException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Response;

public class WeatherApiClient {

    private static final String TAG = "WeatherApiClient";
    
    private static WeatherApiClient instance;
    private MutableLiveData<WeatherResponse> mWeather;
    private RetrieveWeatherRunnable mRetrieveWeatherRunnable;

    public static WeatherApiClient getInstance(){
        if(instance == null){
            instance = new WeatherApiClient();
        }
        return  instance;
    }

    private WeatherApiClient(){
        mWeather = new MutableLiveData<>();
    }

    public LiveData<WeatherResponse> getWeather(){
        return mWeather;
    }

    public void getWeatherApi(float latitude, float longitude, String exclude, String unit) {
        if(mRetrieveWeatherRunnable != null){
            mRetrieveWeatherRunnable = null;
        }
        mRetrieveWeatherRunnable = new RetrieveWeatherRunnable(latitude, longitude, exclude, unit);

        final Future handler = AppExecutors.getInstance().networkIO().submit(mRetrieveWeatherRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //let user know its timed out
                handler.cancel(true);
            }
        }, Constants.NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);

    }

    private class RetrieveWeatherRunnable implements Runnable{

        private float latitude;
        private float longitude;
        private String exclude;
        private String unit;
        boolean cancelRequest;

        public RetrieveWeatherRunnable(float latitude, float longitude, String exclude, String unit) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.exclude = exclude;
            this.unit = unit;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = getWeather(latitude, longitude, exclude, unit).execute();
                if(cancelRequest){
                    return;
                }
                if(response.code() == 200){
                    WeatherResponse weatherResponse = (WeatherResponse)response.body();
                    mWeather.postValue(weatherResponse);
                }else{
                    String error = response.errorBody().string();
                    Log.d(TAG, "run: Error "+error);
                    mWeather.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mWeather.postValue(null);
            }

        }

        private Call<WeatherResponse> getWeather(float latitude, float longitude, String exclude, String unit) {
            return ServiceGenerator.getWeatherApi().getWeatherReport(
                    Constants.API_KEY,
                    latitude,
                    longitude,
                    exclude,
                    unit
            );
        }

        private void cancelRequest(){
            Log.d(TAG, "cancelRequest: canceling Request");
            cancelRequest = true;
        }

    }

}
