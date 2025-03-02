package com.example.weather_orhei;

public class WeatherItem {
    private String title;
    private String description;

    public WeatherItem(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
