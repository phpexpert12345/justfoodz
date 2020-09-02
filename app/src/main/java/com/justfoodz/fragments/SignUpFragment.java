package com.justfoodz.fragments;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.justfoodz.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment implements View.OnClickListener {
    private TextView tvAlreadyAccount;


    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        initialization(view);
        initializedListener(view);

        return view;
    }


    private void initialization(View view) {
        tvAlreadyAccount = view.findViewById(R.id.tv_already_account);
    }


    private void initializedListener(View view) {
        tvAlreadyAccount.setOnClickListener(this);
    }

    public void replaceLoginFragment() {
        FragmentManager fragmentManager = getFragmentManager();
       LoginFragment loginFragment = new  LoginFragment();
        fragmentManager.beginTransaction().replace(R.id.content_frame, loginFragment).commit();


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_already_account:
                replaceLoginFragment();
                break;
            default:
                break;
        }

    }


}
