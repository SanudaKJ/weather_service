package com.practicalTest.practicalTest.dto;


import lombok.Data;
import java.util.List;

@Data
public class WeatherResponse {
    private String cod;
    private int message;
    private int cnt;
    private List<WeatherData> list;
    private City city;

    @Data
    public static class WeatherData {
        private long dt;
        private Main main;
        private List<Weather> weather;
        private String dt_txt;

        @Data
        public static class Main {
            private double temp;
            private double feels_like;
            private double temp_min;
            private double temp_max;
            private int pressure;
            private int humidity;
        }

        @Data
        public static class Weather {
            private int id;
            private String main;
            private String description;
            private String icon;
        }
    }

    @Data
    public static class City {
        private int id;
        private String name;
        private String country;
    }
}