package com.lalaalal.droni.ui.cartoon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.lalaalal.droni.R;

import java.util.ArrayList;

public class KpAlarmFragment extends Fragment implements View.OnClickListener {

    private ArrayList<Integer> drawables;
    private View kp5;
    private int position = 0;

    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_cartoon, container, false);

        drawables = new ArrayList<>();
        drawables.add(R.drawable.kp1);
        drawables.add(R.drawable.kp2);
        drawables.add(R.drawable.kp3);
        drawables.add(R.drawable.kp4);

        kp5 = root.findViewById(R.id.kp5);
        kp5.setOnClickListener(this);

        root.setBackgroundResource(R.drawable.kp1);
        root.setOnClickListener(this);

        Toast.makeText(getContext(), "터치해 넘겨주세요", Toast.LENGTH_SHORT).show();
        return root;
    }

    private int nextPosition() {
        return position = (position + 1) % 5;
    }

    @Override
    public void onClick(View view) {
        if (position == 3) {
            kp5.setVisibility(View.VISIBLE);

            Toast.makeText(getContext(), "아래로 내려주세요", Toast.LENGTH_SHORT).show();
            nextPosition();
        }
        else {
            kp5.setVisibility(View.INVISIBLE);

            int resId = drawables.get(nextPosition());
            root.setBackgroundResource(resId);
        }
    }

}
