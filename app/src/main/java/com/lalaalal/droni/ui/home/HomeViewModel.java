package com.lalaalal.droni.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.lalaalal.droni.WeatherProvider;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> weatherStatus;
    private MutableLiveData<Integer> temperature;
    private MutableLiveData<Integer> windSpeed;
    private MutableLiveData<Integer> kpIndex;

    public HomeViewModel() {
        weatherStatus = new MutableLiveData<>();
        temperature = new MutableLiveData<>();
        windSpeed = new MutableLiveData<>();
        kpIndex = new MutableLiveData<>();
    }

    void setWeatherData(WeatherProvider weatherProvider) throws JSONException {

        JSONObject weatherData = new JSONObject(weatherProvider.getWeatherData());
        JSONObject kpIndexData = new JSONObject(weatherProvider.getKpIndexData());

        String status = weatherData.getJSONArray("weather").getJSONObject(0).getString("icon");
        int temp = weatherData.getJSONObject("main").getInt("temp") - 273;
        int wind = weatherData.getJSONObject("wind").getInt("speed");
        int kp = kpIndexData.getJSONObject("kindex").getInt("currentP");

        weatherStatus.setValue(status);
        temperature.setValue(temp);
        windSpeed.setValue(wind);
        kpIndex.setValue(kp);
    }

    LiveData<String> getWeatherStatus() {
        return weatherStatus;
    }

    LiveData<Integer> getTemperature() {
        return temperature;
    }

    LiveData<Integer> getWindSpeed() {
        return windSpeed;
    }

    LiveData<Integer> getKpIndex() {
        return kpIndex;
    }
}