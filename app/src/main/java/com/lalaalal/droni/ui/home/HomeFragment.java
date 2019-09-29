package com.lalaalal.droni.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.lalaalal.droni.DroniException;
import com.lalaalal.droni.R;
import com.lalaalal.droni.WeatherProvider;
import org.json.JSONException;

import java.lang.reflect.Field;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private ImageView weatherStatus;
    private TextView windSpeed;
    private TextView temperature;
    private TextView kpIndex;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        try {
            homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
            homeViewModel.setWeatherData(getValidWeatherProvider());

            initLayout(root);
        } catch (DroniException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            Toast.makeText(getContext(), "Wrong Data Received", Toast.LENGTH_SHORT).show();
        }

        return root;
    }

    private static int getResId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private WeatherProvider getValidWeatherProvider() throws DroniException {
        WeatherProvider weatherProvider = new WeatherProvider();

        if (weatherProvider.getKpIndexData() == null || weatherProvider.getWeatherData() == null)
            throw new DroniException(DroniException.NO_RESPONSE);
        return weatherProvider;
    }

    private void initLayout(View root) {
        weatherStatus = root.findViewById(R.id.weather_status_iv);
        windSpeed = root.findViewById(R.id.wind_speed_tv);
        temperature = root.findViewById(R.id.temperature_tv);
        kpIndex = root.findViewById(R.id.kp_index_tv);

        homeViewModel.getWeatherStatus().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                int resId = getResId(s, R.drawable.class);
                if (resId != -1)
                    weatherStatus.setImageResource(resId);
            }
        });
        homeViewModel.getWindSpeed().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                String str = "풍속 : " + String.valueOf(integer) + "m/s";
                windSpeed.setText(str);
            }
        });
        homeViewModel.getTemperature().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                String str = "기온 : " + String.valueOf(integer) + "℃";
                temperature.setText(str);
            }
        });
        homeViewModel.getKpIndex().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                String str = "KP : " + String.valueOf(integer);
                kpIndex.setText(str);
            }
        });
    }
}