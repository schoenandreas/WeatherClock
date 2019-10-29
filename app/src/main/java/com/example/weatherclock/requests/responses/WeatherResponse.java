package com.example.weatherclock.requests.responses;

import com.example.weatherclock.models.WeatherData;
import com.example.weatherclock.models.WeatherHourly;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeatherResponse {


    @SerializedName("latitude")
    @Expose()
    private float latitude;

    @SerializedName("longitude")
    @Expose()
    private float longitude;

    @SerializedName("timezone")
    @Expose()
    private String timezone;

    @SerializedName("currently")
    @Expose()
    private WeatherData currently;

    @SerializedName("hourly")
    @Expose()
    private WeatherHourly hourly;

    @SerializedName("offset")
    @Expose()
    private int offset;

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public WeatherData getCurrently() {
        return currently;
    }

    public WeatherHourly getHourly() {
        return hourly;
    }

    public int getOffset() {
        return offset;
    }

    @Override
    public String toString() {
        return "WeatherResponse{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", timezone='" + timezone + '\'' +
                ", currently=" + currently +
                ", hourly=" + hourly +
                ", offset=" + offset +
                '}';
    }
}
