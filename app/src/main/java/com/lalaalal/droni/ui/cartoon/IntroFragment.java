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

public class IntroFragment extends Fragment {

    private ArrayList<Integer> drawables;
    private int position = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_cartoon, container, false);

        drawables = new ArrayList<>();
        drawables.add(R.drawable.intro1);
        drawables.add(R.drawable.intro2);
        drawables.add(R.drawable.intro3);
        drawables.add(R.drawable.intro4);

        root.setBackgroundResource(R.drawable.intro1);

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int resId = drawables.get(nextPosition());
                root.setBackgroundResource(resId);
            }
        });

        Toast.makeText(getContext(), "터치해 넘겨주세요", Toast.LENGTH_SHORT).show();

        return root;
    }

    private int nextPosition() {
        if (position < 3)
            return position++;
        return 3;
    }
}