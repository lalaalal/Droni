package com.lalaalal.droni.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.lalaalal.droni.CallbackExecutor;
import com.lalaalal.droni.WeatherProvider;
import com.lalaalal.droni.ui.LoadingDialog;
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

    void setWeatherData(final LoadingDialog loadingDialog) {
        WeatherProvider weatherProvider = new WeatherProvider(loadingDialog) {
            @Override
            public void callBack() {
                loadingDialog.hideDialog();

                try {
                    JSONObject weatherData = new JSONObject(getWeatherData());
                    JSONObject kpIndexData = new JSONObject(getKpIndexData());

                    String status = weatherData.getJSONArray("weather").getJSONObject(0).getString("icon");
                    int temp = weatherData.getJSONObject("main").getInt("temp") - 273;
                    int wind = weatherData.getJSONObject("wind").getInt("speed");
                    int kp = kpIndexData.getJSONObject("kindex").getInt("currentP");

                    weatherStatus.postValue(status);
                    temperature.postValue(temp);
                    windSpeed.postValue(wind);
                    kpIndex.postValue(kp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        CallbackExecutor callbackExecutor = new CallbackExecutor();
        callbackExecutor.execute(weatherProvider);
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