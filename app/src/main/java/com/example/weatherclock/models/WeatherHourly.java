package com.example.weatherclock.models;

import java.util.Arrays;

public class WeatherHourly {

    private String summary;
    private String icon;
    private WeatherData[] data;

    public WeatherHourly(String summary, String icon, WeatherData[] data) {
        this.summary = summary;
        this.icon = icon;
        this.data = data;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public WeatherData[] getData() {
        return data;
    }

    public void setData(WeatherData[] data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return "WeatherHourly{" +
                "summary='" + summary + '\'' +
                ", icon='" + icon + '\'' +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
