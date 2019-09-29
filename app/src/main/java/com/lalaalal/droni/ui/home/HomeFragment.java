package com.lalaalal.droni.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.lalaalal.droni.R;
import com.lalaalal.droni.WeatherProvider;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private TextView weatherStatus;
    private TextView windSpeed;
    private TextView temperature;
    private TextView kpIndex;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        weatherStatus = root.findViewById(R.id.weather_status_tv);
        windSpeed = root.findViewById(R.id.wind_speed_tv);
        temperature = root.findViewById(R.id.temperature_tv);
        kpIndex = root.findViewById(R.id.kp_index_tv);

        homeViewModel.getWeatherStatus().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                weatherStatus.setText(s);
            }
        });
        homeViewModel.getWindSpeed().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                windSpeed.setText(String.valueOf(integer));
            }
        });
        homeViewModel.getTemperature().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                temperature.setText(String.valueOf(integer));
            }
        });
        homeViewModel.getKpIndex().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                kpIndex.setText(String.valueOf(integer));
            }
        });

        return root;
    }

    private void setWeatherData() throws JSONException {
        WeatherProvider weatherProvider = new WeatherProvider();

        JSONObject weatherData = new JSONObject(weatherProvider.getWeatherData());
        JSONObject kpIndexData = new JSONObject(weatherProvider.getKpIndexData());

        String status = weatherData.getJSONArray("weather").getJSONObject(0).getString("main");
        int temp = weatherData.getJSONObject("main").getInt("temp") - 273;
        int wind = weatherData.getJSONObject("wind").getInt("speed");
        int kp = kpIndexData.getJSONObject("kindex").getInt("currentP");

        windSpeed.setText(wind);
        temperature.setText(temp);
        weatherStatus.setText(status);
        kpIndex.setText(kp);
    }
}