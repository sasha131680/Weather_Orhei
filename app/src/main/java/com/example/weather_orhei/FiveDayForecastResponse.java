package com.example.weather_orhei;

import java.util.List;

public class FiveDayForecastResponse {
    private List<ForecastItem> list;

    public List<ForecastItem> getList() {
        return list;
    }

    public static class ForecastItem {
        private Main main;
        private Weather[] weather;
        private String dt_txt; // Дата и время прогноза

        public Main getMain() {
            return main;
        }

        public Weather[] getWeather() {
            return weather;
        }

        public String getDt_txt() {
            return dt_txt;
        }

        public static class Main {
            private float temp; // Температура

            public float getTemp() {
                return temp;
            }
        }

        public static class Weather {
            private String description; // Описание погоды

            public String getDescription() {
                return description;
            }
        }
    }
}
