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

public class SlideOne extends Fragment {
    TextView txthead1,txtdes1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sliderone,container,false);
        txthead1 = view.findViewById(R.id.txthead1);
        txtdes1 = view.findViewById(R.id.txtdes1);
        txthead1.setText(SliderImage.splash_screen_first_title);
        txtdes1.setText(SliderImage.splash_screen_first_des);
        return view;
    }
}
