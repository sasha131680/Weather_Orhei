package com.example.weather_orhei;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {


    // Запрос прогноза на 5 дней
    @GET("forecast")
    Call<FiveDayForecastResponse> getFiveDayForecast(
            @Query("q") String city,
            @Query("appid") String apiKey,
            @Query("units") String units
    );
}
