package com.practicalTest.practicalTest.service;// WeatherServiceTest.java



import com.practicalTest.practicalTest.dto.WeatherResponse;
import com.practicalTest.practicalTest.dto.WeatherSummary;
import com.practicalTest.practicalTest.exception.WeatherApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(weatherService, "apiUrl", "http://api.test.com");
        ReflectionTestUtils.setField(weatherService, "apiKey", "test-key");
    }

    @Test
    void testGetWeatherSummary_Success() throws Exception {
        // Given
        String city = "London";
        WeatherResponse mockResponse = createMockWeatherResponse();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(WeatherResponse.class))
                .thenReturn(Mono.just(mockResponse));

        // When
        CompletableFuture<WeatherSummary> result = weatherService.getWeatherSummary(city);
        WeatherSummary summary = result.get();

        // Then
        assertNotNull(summary);
        assertEquals(city, summary.getCity());
        assertTrue(summary.getAverageTemperature() > 0);
        assertNotNull(summary.getHottestDay());
        assertNotNull(summary.getColdestDay());
    }

    @Test
    void testGetWeatherSummary_CityNotFound() {
        // Given
        String city = "InvalidCity";

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(WeatherResponse.class))
                .thenReturn(Mono.empty());

        // When & Then
        assertThrows(WeatherApiException.class, () -> {
            weatherService.getWeatherSummary(city).get();
        });
    }

    private WeatherResponse createMockWeatherResponse() {
        WeatherResponse response = new WeatherResponse();

        WeatherResponse.WeatherData data1 = new WeatherResponse.WeatherData();
        data1.setDt(1700000000L);
        WeatherResponse.WeatherData.Main main1 = new WeatherResponse.WeatherData.Main();
        main1.setTemp(20.5);
        data1.setMain(main1);

        WeatherResponse.WeatherData data2 = new WeatherResponse.WeatherData();
        data2.setDt(1700010000L);
        WeatherResponse.WeatherData.Main main2 = new WeatherResponse.WeatherData.Main();
        main2.setTemp(15.0);
        data2.setMain(main2);

        response.setList(Arrays.asList(data1, data2));

        return response;
    }
}