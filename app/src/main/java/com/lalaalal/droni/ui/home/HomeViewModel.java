package com.lalaalal.droni.ui.home;

import android.util.Log;
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

        try {
            setWeatherData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setWeatherData() throws JSONException {
        WeatherProvider weatherProvider = new WeatherProvider();

        Log.d("weather", weatherProvider.getWeatherData());
        JSONObject weatherData = new JSONObject(weatherProvider.getWeatherData());
        JSONObject kpIndexData = new JSONObject(weatherProvider.getKpIndexData());

        String status = weatherData.getJSONArray("weather").getJSONObject(0).getString("main");
        int temp = weatherData.getJSONObject("main").getInt("temp") - 273;
        int wind = weatherData.getJSONObject("wind").getInt("speed");
        int kp = kpIndexData.getJSONObject("kindex").getInt("currentP");

        weatherStatus.setValue(status);
        temperature.setValue(temp);
        windSpeed.setValue(wind);
        kpIndex.setValue(kp);
    }

    public LiveData<String> getWeatherStatus() {
        return weatherStatus;
    }

    public LiveData<Integer> getTemperature() {
        return temperature;
    }

    public LiveData<Integer> getWindSpeed() {
        return windSpeed;
    }

    public LiveData<Integer> getKpIndex() {
        return kpIndex;
    }
}