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

public class SlideTwo extends Fragment {
    TextView txthead2,txtdes2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.slidertwo,container,false);
        txthead2 = view.findViewById(R.id.txthead2);
        txtdes2 = view.findViewById(R.id.txtdes2);
        txthead2.setText(SliderImage.splash_screen_second_title);
        txtdes2.setText(SliderImage.splash_screen_second_desc);
        return view;
    }
}
