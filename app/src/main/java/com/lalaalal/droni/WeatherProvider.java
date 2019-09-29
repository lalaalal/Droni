package com.lalaalal.droni;

import com.lalaalal.droni.client.HttpsClient;

public class WeatherProvider {
    private static final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?q=Seoul&appid=9db3d4e026322a65757adc4dd568d988";
    private static final String KP_URL = "https://spaceweather.rra.go.kr/api/kindex";
    private String weatherData;
    private String kpIndexData;

    public WeatherProvider() {
        try {
            HttpsClient weather = new HttpsClient(WEATHER_URL);
            Thread weatherThread = new Thread(weather);
            weatherThread.start();
            weatherThread.join();

            weatherData = weather.getResult();

            HttpsClient kpIndex = new HttpsClient(KP_URL);
            Thread kpThread = new Thread(kpIndex);
            kpThread.start();
            kpThread.join();

            kpIndexData = kpIndex.getResult();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getWeatherData() {
        return weatherData;
    }

    public String getKpIndexData() {
        return kpIndexData;
    }
}
