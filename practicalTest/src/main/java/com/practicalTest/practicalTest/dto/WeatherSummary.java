package com.practicalTest.practicalTest.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherSummary {
    private String city;
    private double averageTemperature;
    private String hottestDay;
    private String coldestDay;
}