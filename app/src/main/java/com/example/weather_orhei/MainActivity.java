package com.example.weather_orhei;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "639b8753805c7ff8188620379ed14880";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    private RecyclerView recyclerView;
    private WeatherAdapter adapter;
    private ArrayList<WeatherItem> weatherList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        weatherList = new ArrayList<>();
        adapter = new WeatherAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Запрос данных о прогнозе на 5 дней
        fetchFiveDayForecast("Orhei");
    }


    private void fetchFiveDayForecast(String city) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherApi weatherApi = retrofit.create(WeatherApi.class);

        // Запрос прогноза на 5 дней
        Call<FiveDayForecastResponse> call = weatherApi.getFiveDayForecast(city, API_KEY, "metric");
        call.enqueue(new Callback<FiveDayForecastResponse>() {
            @Override
            public void onResponse(Call<FiveDayForecastResponse> call, Response<FiveDayForecastResponse> response) {
                if (response.isSuccessful()) {
                    FiveDayForecastResponse forecastResponse = response.body();
                    if (forecastResponse != null) {
                        for (FiveDayForecastResponse.ForecastItem item : forecastResponse.getList()) {
                            // Формируем текст для каждого дня прогноза
                            String forecastText = String.format("Date: %s, Temp: %.1f°C, Condition: %s",
                                    item.getDt_txt(), item.getMain().getTemp(), item.getWeather()[0].getDescription());
                            weatherList.add(new WeatherItem("Forecast for " + item.getDt_txt(), forecastText));
                        }
                        adapter.submitList(new ArrayList<>(weatherList));  // Обновление списка с прогнозом
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Error fetching forecast data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FiveDayForecastResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
