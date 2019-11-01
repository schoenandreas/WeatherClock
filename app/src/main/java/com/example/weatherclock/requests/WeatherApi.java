package com.example.weatherclock.requests;

import com.example.weatherclock.requests.responses.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WeatherApi {


    @GET("forecast/{key}/{latitude},{longitude}")
    Call<WeatherResponse> getWeatherReport(
            @Path("key") String key,
            @Path("latitude") float latitude,
            @Path("longitude") float longitude,
            @Query("exclude") String excludeParams,
            @Query("units") String unitParam
    );

}
