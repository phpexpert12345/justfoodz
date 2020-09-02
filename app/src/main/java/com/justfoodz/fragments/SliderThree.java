package com.justfoodz.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.justfoodz.R;
import com.justfoodz.activities.SliderImage;

public class SliderThree extends Fragment {
    TextView txthead3,txtdes3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sliderthree,container,false);
        txthead3 = view.findViewById(R.id.txthead3);
        txtdes3 = view.findViewById(R.id.txtdes3);
        txthead3.setText(SliderImage.splash_screen_second_title);
        txtdes3.setText(SliderImage.splash_screen_second_desc);
        return view;
    }
}
