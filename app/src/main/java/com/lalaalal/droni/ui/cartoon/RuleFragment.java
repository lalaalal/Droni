package com.lalaalal.droni.ui.cartoon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.lalaalal.droni.R;

import java.util.ArrayList;

public class RuleFragment extends Fragment {

    private ArrayList<Integer> drawables;
    private int position = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cartoon, container, false);
    }

    private int nextPosition() {
        return position = (position + 1) % 4;
    }
}