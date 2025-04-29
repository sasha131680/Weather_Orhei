package com.example.weather_orhei;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;
import android.net.Uri;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "639b8753805c7ff8188620379ed14880";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    private RecyclerView recyclerView;
    private WeatherAdapter adapter;
    private ArrayList<WeatherItem> weatherList;
    private Button fetchWeatherButton, clearListButton;
    private Button cautareGoogleButton;
    private EditText searchInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cautareGoogleButton = findViewById(R.id.cautareGoogleButton);
        searchInput = findViewById(R.id.searchInput);

        cautareGoogleButton.setOnClickListener(v -> {
            String query = searchInput.getText().toString().trim();
            if (!query.isEmpty()) {
                String url = "https://www.google.com/search?q=" + Uri.encode(query);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Introduceți un text pentru căutare", Toast.LENGTH_SHORT).show();
            }
        });


        recyclerView = findViewById(R.id.recyclerView);
        fetchWeatherButton = findViewById(R.id.fetchWeatherButton);
        clearListButton = findViewById(R.id.clearListButton);
        weatherList = new ArrayList<>();
        adapter = new WeatherAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Обновление прогноза по нажатию на кнопку
        fetchWeatherButton.setOnClickListener(v -> fetchFiveDayForecast("Orhei"));

        // Очистка списка по нажатию на кнопку
        clearListButton.setOnClickListener(v -> {
            weatherList.clear();
            adapter.submitList(new ArrayList<>(weatherList)); // Обновление адаптера
        });
    }

    private void fetchFiveDayForecast(String city) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherApi weatherApi = retrofit.create(WeatherApi.class);

        Call<FiveDayForecastResponse> call = weatherApi.getFiveDayForecast(city, API_KEY, "metric");
        call.enqueue(new Callback<FiveDayForecastResponse>() {
            @Override
            public void onResponse(Call<FiveDayForecastResponse> call, Response<FiveDayForecastResponse> response) {
                if (response.isSuccessful()) {
                    weatherList.clear();
                    FiveDayForecastResponse forecastResponse = response.body();

                    if (forecastResponse != null) {
                        Map<String, ArrayList<Float>> dailyTemperatures = new HashMap<>();

                        for (FiveDayForecastResponse.ForecastItem item : forecastResponse.getList()) {
                            String date = item.getDt_txt().substring(0, 10);
                            float temp = item.getMain().getTemp();

                            dailyTemperatures.putIfAbsent(date, new ArrayList<>());
                            dailyTemperatures.get(date).add(temp);
                        }

                        for (Map.Entry<String, ArrayList<Float>> entry : dailyTemperatures.entrySet()) {
                            String date = entry.getKey();
                            ArrayList<Float> temps = entry.getValue();
                            float sum = 0;
                            for (float temp : temps) {
                                sum += temp;
                            }
                            float averageTemp = sum / temps.size();
                            String forecastText = String.format("Date: %s, Average Temp: %.1f°C", date, averageTemp);
                            weatherList.add(new WeatherItem("Forecast for " + date, forecastText));
                        }

                        adapter.submitList(new ArrayList<>(weatherList));
                    }
                } else {
                    Log.e("WeatherApp", "Response Error: " + response.code() + " - " + response.message());
                    Toast.makeText(MainActivity.this, "Error fetching forecast data: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FiveDayForecastResponse> call, Throwable t) {
                Log.e("WeatherApp", "Request Failed: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

