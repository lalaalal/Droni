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
}