package com.practicalTest.practicalTest.service;

import com.practicalTest.practicalTest.dto.WeatherResponse;
import com.practicalTest.practicalTest.dto.WeatherSummary;
import com.practicalTest.practicalTest.exception.WeatherApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherService {

    private final WebClient webClient;

    @Value("${weather.api.url}")
    private String apiUrl;

    @Value("${weather.api.key}")
    private String apiKey;

    @Async("taskExecutor")
    @Cacheable(value = "weather-cache", key = "#city")
    public CompletableFuture<WeatherSummary> getWeatherSummary(String city) {
        log.info("Fetching weather data for city: {}", city);

        try {
            WeatherResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/forecast")
                            .queryParam("q", city)
                            .queryParam("appid", apiKey)
                            .queryParam("units", "metric")
                            .build())
                    .retrieve()
                    .bodyToMono(WeatherResponse.class)
                    .block();

            if (response == null || response.getList() == null || response.getList().isEmpty()) {
                throw new WeatherApiException("No weather data found for city: " + city);
            }

            WeatherSummary summary = processWeatherData(response, city);
            log.info("Weather summary processed for city: {}", city);

            return CompletableFuture.completedFuture(summary);

        } catch (WebClientResponseException e) {
            log.error("Error fetching weather data for city: {}", city, e);
            if (e.getStatusCode().value() == 404) {
                throw new WeatherApiException("City not found: " + city);
            }
            throw new WeatherApiException("Failed to fetch weather data: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error fetching weather data for city: {}", city, e);
            throw new WeatherApiException("Unexpected error: " + e.getMessage());
        }
    }

    private WeatherSummary processWeatherData(WeatherResponse response, String city) {
        List<WeatherResponse.WeatherData> last7Days = response.getList()
                .stream()
                .limit(7 * 8) // 8 forecasts per day (3-hour intervals)
                .collect(Collectors.toList());

        // Calculate average temperature
        double averageTemp = last7Days.stream()
                .mapToDouble(data -> data.getMain().getTemp())
                .average()
                .orElse(0.0);

        // Find hottest day
        WeatherResponse.WeatherData hottestData = last7Days.stream()
                .max(Comparator.comparingDouble(data -> data.getMain().getTemp()))
                .orElse(null);

        // Find coldest day
        WeatherResponse.WeatherData coldestData = last7Days.stream()
                .min(Comparator.comparingDouble(data -> data.getMain().getTemp()))
                .orElse(null);

        String hottestDay = formatDate(hottestData != null ? hottestData.getDt() : 0);
        String coldestDay = formatDate(coldestData != null ? coldestData.getDt() : 0);

        return new WeatherSummary(
                city,
                Math.round(averageTemp * 10.0) / 10.0,
                hottestDay,
                coldestDay
        );
    }

    private String formatDate(long timestamp) {
        return Instant.ofEpochSecond(timestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}