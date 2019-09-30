package com.lalaalal.droni.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.lalaalal.droni.R;
import com.lalaalal.droni.ui.LoadingDialog;

import java.lang.reflect.Field;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private ImageView weatherStatus;
    private TextView windSpeed;
    private TextView temperature;
    private TextView kpIndex;

    private CardView communityCardView;
    private CardView calendarCardView;
    private CardView eventCardView;
    private CardView ruleCardView;

    private CardView droneVideoButton;
    private CardView newDroneNoticeButton;
    private CardView blockedAreaButton;
    private CardView kpSiteButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        LoadingDialog loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.showDialog();
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        homeViewModel.setWeatherData(loadingDialog);

        communityCardView = root.findViewById(R.id.connect_community);
        calendarCardView = root.findViewById(R.id.connect_calendar);
        eventCardView = root.findViewById(R.id.connect_event);
        ruleCardView = root.findViewById(R.id.rule_show);

        droneVideoButton = root.findViewById(R.id.drone_video);
        newDroneNoticeButton = root.findViewById(R.id.new_drone_notice);
        blockedAreaButton = root.findViewById(R.id.blocked_area);
        kpSiteButton = root.findViewById(R.id.kp_site);

        initLayout(root);
        setOnClickListeners();

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

    private void initLayout(View root) {
        weatherStatus = root.findViewById(R.id.weather_status_iv);
        windSpeed = root.findViewById(R.id.wind_speed_tv);
        temperature = root.findViewById(R.id.temperature_tv);
        kpIndex = root.findViewById(R.id.kp_index_tv);

        homeViewModel.getWeatherStatus().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                int resId = getResId("w" + s, R.drawable.class);
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
                if (integer <= 3)
                    kpIndex.setTextColor(Color.GREEN);
                else if (integer == 4)
                    kpIndex.setTextColor(Color.rgb(242, 101, 32));
                else
                    kpIndex.setTextColor(Color.RED);
                kpIndex.setText(str);
            }
        });
    }

    private void setOnClickListeners() {
        eventCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUrl("https://cafe.naver.com/ArticleList.nhn?search.clubid=29883976&search.menuid=2&search.boardtype=L");
            }
        });
        communityCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUrl("https://cafe.naver.com/dronetime");
            }
        });

        calendarCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUrl("https://calendar.google.com/calendar?cid=NWJsdWlnOXUzb3Nha2Q3NzdnaGxwcG1yb3NAZ3JvdXAuY2FsZW5kYXIuZ29vZ2xlLmNvbQ");
            }
        });

        ruleCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUrl("https://cafe.naver.com/dronplay/154739");
            }
        });

        droneVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUrl("https://www.youtube.com/watch?v=DRMAZ-SZJyQ");
            }
        });
        newDroneNoticeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUrl("https://www.youtube.com/watch?v=t_eUV3jnvqA");
            }
        });
        blockedAreaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUrl("https://www.anadronestarting.com/drone-legal-guide/");
            }
        });
        kpSiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUrl("https://spaceweather.rra.go.kr/observation/ground/magnetism/kindex?lang=ko");
            }
        });

    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}