package com.lalaalal.droni.ui.cartoon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.lalaalal.droni.R;

import java.util.ArrayList;

public class KpAlarmFragment extends Fragment {

    private ArrayList<Integer> drawables;
    private int position = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_cartoon, container, false);

        drawables = new ArrayList<>();
        drawables.add(R.drawable.kp1);
        drawables.add(R.drawable.kp2);
        drawables.add(R.drawable.kp3);
        drawables.add(R.drawable.kp4);


        root.setBackgroundResource(R.drawable.kp1);

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int resId = drawables.get(nextPosition());
                root.setBackgroundResource(resId);
            }
        });

        return root;
    }

    private int nextPosition() {
        return position = (position + 1) % 4;
    }
}
