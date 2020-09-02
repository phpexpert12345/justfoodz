package com.justfoodz.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import com.justfoodz.activities.MainMenuActivity;
import com.justfoodz.R;
import com.justfoodz.activities.Model_RestaurantMenuItem;
import com.justfoodz.activities.SplashScreenActivity;
import com.justfoodz.activities.SubMenuActivity;
import com.justfoodz.adapters.MenuAdapter;
import com.justfoodz.interfaces.MenuItemInterface;
import com.justfoodz.models.MenuModel;
import com.justfoodz.models.Restaurantlistmodel;
import com.justfoodz.models.SubList.RestaurantMencategory;
import com.justfoodz.retrofit.InterUserdata;
import com.justfoodz.retrofit.Model_location;
import com.justfoodz.retrofit.RetrofitAdapter;
import com.justfoodz.retrofit.ServiceGenerator;
import com.justfoodz.utils.AuthPreference;
import com.justfoodz.utils.MyProgressDialog;
import com.justfoodz.utils.Network;
import com.justfoodz.utils.UrlConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment implements MenuItemInterface {
    private ProgressDialog pDialog;
    private AuthPreference authPreference;
    private List<RestaurantMencategory> menuModelArrayList;
    private MenuModel menuModel;
    private RecyclerView rv_menu;
    private MenuAdapter menuAdapter;
    private String id;
    private String restaurantAddress, restaurantName;
    public static String menutypevalue;
    String currentDateTimeString;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initialization(view);
        currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        menuModelArrayList = new ArrayList<>();
        if (Network.isNetworkCheck(getActivity())) {
            if (menutypevalue.equals("true")) {
                makeServerRequest("1");
            } else {
                makeServerRequest("0");
            }

        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();

        }


    }

    private void initialization(View view) {
        authPreference = new AuthPreference(getActivity());
        rv_menu = view.findViewById(R.id.rv_menu);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rv_menu.setLayoutManager(mLayoutManager);
        rv_menu.setItemAnimator(new DefaultItemAnimator());
        id = getArguments().getString("id");
        restaurantName = getArguments().getString("restaurantName");
        restaurantAddress = getArguments().getString("restaurantAddress");
    }


    private void makeServerRequest(final String menu_type) {
        MyProgressDialog.show(getActivity(), R.string.please_wait);


        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<RestaurantMencategoryArray> userLoginCall = interfaceRetrofit.phpexpert_restaurantMenuCategorys(id, menu_type, currentDateTimeString, "" + authPreference.getCustomerId());
        userLoginCall.enqueue(new Callback<RestaurantMencategoryArray>() {
            @Override
            public void onResponse(Call<RestaurantMencategoryArray> call, retrofit2.Response<RestaurantMencategoryArray> response) {
                if (response.isSuccessful()) {

                    RestaurantMencategoryArray body = response.body();
                    for (int i = 0; i < body.getRestaurantMencategory().size(); i++) {
                        RestaurantMencategory restaurantMencategory = body.getRestaurantMencategory().get(i);
                        if (restaurantMencategory.getError()!=1) {
                            menuModelArrayList.add(restaurantMencategory);
                        }

                    }
                    if (menuModelArrayList.size() > 0) {
                        menuAdapter = new MenuAdapter(getActivity(), MenuFragment.this, menuModelArrayList);
                        rv_menu.setAdapter(menuAdapter);
                        rv_menu.setVisibility(View.VISIBLE);
                    }

                }
                MyProgressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<RestaurantMencategoryArray> call, Throwable t) {

                MyProgressDialog.dismiss();
                rv_menu.setVisibility(View.GONE);
            }
        });


        /*Map<String, String> params = new HashMap<String, String>();
        params.put("resid", id);
        params.put("menu_type", menu_type);
        params.put("menu_items_current_time", currentDateTimeString);
        params.put("CustomerId", "" + authPreference.getCustomerId());

        InterUserdata interUserdata = ServiceGenerator.createService(InterUserdata.class);
        interUserdata.getLoginResponse(params).enqueue(new Callback<MenuModel>() {

            @Override
            public void onResponse(Call<MenuModel> call, retrofit2.Response<MenuModel> response) {

                if (response.isSuccessful())
                    MyProgressDialog.dismiss();

                menuModelArrayList = response.body().getRestaurantMencategory();

                menuAdapter = new MenuAdapter(getActivity(), MenuFragment.this, menuModelArrayList);
                rv_menu.setAdapter(menuAdapter);

            }

            @Override
            public void onFailure(Call<MenuModel> call, Throwable t) {
                MyProgressDialog.dismiss();
            }
        });*/
    }


    @Override
    public void menuItemPosition(int position) {
        Intent intent = new Intent(getActivity(), SubMenuActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("restaurantAddress", restaurantAddress);
        intent.putExtra("restaurantName", restaurantName);
        intent.putExtra("restaurantCategoryId", menuModelArrayList.get(position).getId());
        intent.putExtra("categoryName", menuModelArrayList.get(position).getCategoryName());
        startActivityForResult(intent, 1);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == getActivity().RESULT_OK) {
                MainMenuActivity.tvCartItemCount.setText("" + SubMenuActivity.subMenuCartList.size());


            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        MyProgressDialog.dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();


    }
}













