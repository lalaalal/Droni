package com.lalaalal.droni.ui.cartoon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.lalaalal.droni.R;

public class Rule2Fragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Toast.makeText(getContext(), "아래로 내려주세요", Toast.LENGTH_SHORT).show();

        return inflater.inflate(R.layout.fragment_rule, container, false);
    }
}