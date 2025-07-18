package com.practicalTest.practicalTest.controller;// WeatherControllerTest.java

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practicalTest.practicalTest.controller.WeatherController;
import com.practicalTest.practicalTest.dto.WeatherSummary;
import com.practicalTest.practicalTest.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WeatherController.class)
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetWeatherSummary_Success() throws Exception {
        // Given
        String city = "London";
        WeatherSummary summary = new WeatherSummary(city, 15.5, "2024-11-20", "2024-11-18");

        when(weatherService.getWeatherSummary(anyString()))
                .thenReturn(CompletableFuture.completedFuture(summary));

        // When & Then
        mockMvc.perform(get("/weather")
                        .param("city", city))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value(city))
                .andExpect(jsonPath("$.averageTemperature").value(15.5))
                .andExpect(jsonPath("$.hottestDay").value("2024-11-20"))
                .andExpect(jsonPath("$.coldestDay").value("2024-11-18"));
    }
}