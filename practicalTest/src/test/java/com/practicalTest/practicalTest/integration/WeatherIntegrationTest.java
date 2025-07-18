//package com.practicalTest.practicalTest.integration;// WeatherIntegrationTest.java
//
//import com.github.tomakehurst.wiremock.WireMockServer;
//import com.github.tomakehurst.wiremock.client.WireMock;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.TestPropertySource;
//
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@TestPropertySource(properties = {
//        "weather.api.url=http://localhost:8089/data/2.5/forecast",
//        "weather.api.key=test-key"
//})
//class WeatherIntegrationTest {
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    private WireMockServer wireMockServer;
//
//    @BeforeEach
//    void setUp() {
//        wireMockServer = new WireMockServer(8089);
//        wireMockServer.start();
//        WireMock.configureFor("localhost", 8089);
//    }
//
//    @AfterEach
//    void tearDown() {
//        wireMockServer.stop();
//    }
//
//    @Test
//    void testWeatherEndpoint_Success() {
//        // Given
//        stubFor(get(urlPathEqualTo("/data/2.5/forecast"))
//                .willReturn(aResponse()
//                        .withStatus(200)
//                        .withHeader("Content-Type", "application/json")
//                        .withBody(getMockWeatherResponse())));
//
//        // When
//        ResponseEntity<String> response = restTemplate.getForEntity(
//                "/weather?city=London", String.class);
//
//        // Then
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertTrue(response.getBody().contains("London"));
//    }
//
//    private String getMockWeatherResponse() {
//        return """
//                {
//                    "cod": "200",
//                    "message": 0,
//                    "cnt": 40,
//                    "list": [
//                        {
//                            "dt": 1700000000,
//                            "main": {
//                                "temp": 20.5,
//                                "feels_like": 19.8,
//                                "temp_min": 18.0,
//                                "temp_max": 22.0,
//                                "pressure": 1013,
//                                "humidity": 65
//                            },
//                            "weather": [
//                                {
//                                    "id": 800,
//                                    "main": "Clear",
//                                    "description": "clear sky",
//                                    "icon": "01d"
//                                }
//                            ],
//                            "dt_txt": "2024-11-20 12:00:00"
//                        }
//                    ],
//                    "city": {
//                        "id": 2643743,
//                        "name": "London",
//                        "country": "GB"
//                    }
//                }
//                """;
//    }
//}