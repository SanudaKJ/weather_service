package com.practicalTest.practicalTest.controller;

import com.practicalTest.practicalTest.dto.WeatherSummary;
import com.practicalTest.practicalTest.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
@Slf4j
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping
    public CompletableFuture<ResponseEntity<WeatherSummary>> getWeatherSummary(
            @RequestParam String city) {

        log.info("Received request for weather summary of city: {}", city);

        return weatherService.getWeatherSummary(city)
                .thenApply(summary -> {
                    log.info("Returning weather summary for city: {}", city);
                    return ResponseEntity.ok(summary);
                });
    }
}