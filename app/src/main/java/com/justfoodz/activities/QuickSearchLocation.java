package com.justfoodz.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.gson.Gson;
import com.justfoodz.MyApplication;
import com.justfoodz.MyProgressDialog;
import com.justfoodz.R;
import com.justfoodz.fragments.HomeFragment;
import com.justfoodz.fragments.Model_Reasurant;
import com.justfoodz.models.Citymodel;
import com.justfoodz.models.PlaceAutoComplete;
import com.justfoodz.models.PlacePredictions;
import com.justfoodz.models.RestaurantModel;
import com.justfoodz.retrofit.InterUserdata;
import com.justfoodz.retrofit.Model_location;
import com.justfoodz.retrofit.RetrofitAdapter;
import com.justfoodz.utils.AuthPreference;
import com.justfoodz.utils.DatabaseHelper;
import com.justfoodz.utils.MyPref;
import com.justfoodz.utils.Network;
import com.justfoodz.utils.UrlConstants;
import com.justfoodz.utils.VolleyJSONRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Callback;

public class QuickSearchLocation extends AppCompatActivity {
    LinearLayout citydialog, localitydialog, placedialog;
    Dialog dialog, dialog1;
    private RecyclerView cityrecycler;
    private ArrayList<Citymodel> CityList;
    private CITYAdapter cityAdapter;
    EditText search_view;
    ProgressDialog progressDialog;
    private ArrayList<Citymodel> searchList;
    private Citymodel citymodel;
    private TextView city, servicename, edt_placesearch;
    private EditText edtPassCode;
    private Button tv_search_txt;
    String CITY;

    private ProgressDialog pDialog;
    public ArrayList<RestaurantModel> restaurantModelArrayList;
    private AuthPreference authPreference;
    public static RestaurantModel restaurantModel;
    ImageView iv_back;

    private VolleyJSONRequest request;
    private AutoCompleteAdapter mAutoCompleteAdapter;
    private PlacePredictions predictions;
    private Handler handler;
    String service = "sss", cuisineName = "dd", data;
    private DatabaseHelper databaseHelper;
    MyPref myPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_search_location);

        myPref = new MyPref(QuickSearchLocation.this);
        intializeUI();

        final Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        service = bundle.getString("serviceName");
        cuisineName = bundle.getString("cuisineName");
        data = bundle.getString("data");

        if (data.equals("1")) servicename.setText("'" + service + "'");
        else servicename.setText("'" + cuisineName + "'");

        if (myPref.getReferId().equals("2")) {
            placedialog.setVisibility(View.GONE);
            localitydialog.setVisibility(View.VISIBLE);
            citydialog.setVisibility(View.VISIBLE);
        } else {
            placedialog.setVisibility(View.VISIBLE);
            edt_placesearch.setText("" + myPref.getPickupAdd());
            localitydialog.setVisibility(View.GONE);
            citydialog.setVisibility(View.GONE);
        }


        citydialog.setOnClickListener(view -> showSettingPoup());

        iv_back.setOnClickListener(view -> finish());

        placedialog.setOnClickListener(view -> showSingleSearch());


        tv_search_txt.setOnClickListener(view -> {

            if (data.equals("1")) {
                if (myPref.getReferId().equals("2")) {
                    CITY = city.getText().toString();
                    if (CITY.equals("")) {
                        city.setError("Please Select Your City");
                        city.requestFocus();
                    } else if (Network.isNetworkCheck(QuickSearchLocation.this)) {

                        HomeActivity.where_para = CITY;
                        searchFromPassCode(service, "");

                    } else {
                        Toast.makeText(QuickSearchLocation.this, getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    CITY = edt_placesearch.getText().toString();
                    if (edt_placesearch.equals("")) {
                        edt_placesearch.setError("Please Select Your City");
                        edt_placesearch.requestFocus();
                    } else if (Network.isNetworkCheck(QuickSearchLocation.this)) {

                        HomeActivity.where_para = CITY;
                        searchFromPassCode(service, "");

                    } else {
                        Toast.makeText(QuickSearchLocation.this, getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
                    }
                }

            } else {
                if (myPref.getReferId().equals("2")) {
                    CITY = city.getText().toString();
                    if (CITY.equals("")) {
                        city.setError("Please Select Your City");
                        city.requestFocus();
                    } else if (Network.isNetworkCheck(QuickSearchLocation.this)) {

                        HomeActivity.where_para = CITY;
                        searchFromPassCode("", cuisineName);

                    } else {
                        Toast.makeText(QuickSearchLocation.this, getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    CITY = edt_placesearch.getText().toString();
                    if (edt_placesearch.equals("")) {
                        edt_placesearch.setError("Please Select Your City");
                        edt_placesearch.requestFocus();
                    } else if (Network.isNetworkCheck(QuickSearchLocation.this)) {

                        HomeActivity.where_para = CITY;
                        searchFromPassCode("", cuisineName);

                    } else {
                        Toast.makeText(QuickSearchLocation.this, getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
                    }
                }

            }


        });
    }

    private void intializeUI() {
        CityList = new ArrayList<Citymodel>();
        databaseHelper = new DatabaseHelper(QuickSearchLocation.this);
        citydialog = findViewById(R.id.citydialog);
        localitydialog = findViewById(R.id.lllocation);
        placedialog = findViewById(R.id.placedialog);
        city = findViewById(R.id.city);
        edt_placesearch = findViewById(R.id.edt_placesearch);
        iv_back = findViewById(R.id.iv_back);
        servicename = findViewById(R.id.servicename);
        edtPassCode = findViewById(R.id.edt_passCode);
        tv_search_txt = findViewById(R.id.tv_search_txt);
        authPreference = new AuthPreference(this);
        searchList = new ArrayList<>();

    }

    public void showSettingPoup() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.searchcitydailog);
        dialog.getWindow().getAttributes().windowAnimations = R.style.diologAnimatioLocation;
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setCanceledOnTouchOutside(true);

        cityrecycler = (RecyclerView) dialog.findViewById(R.id.cityrecycler);
        search_view = (EditText) dialog.findViewById(R.id.search_view);

        ArrayList<Citymodel> cityListing = databaseHelper.getCityListing();
        if (cityListing.size() > 0) {
            CityList.clear();
            for (int i = 0; i < cityListing.size(); i++) {
                CityList.add(cityListing.get(i));
            }
            if (CityList.size() > 0) {
                CITYAdapter cityAdapter = new CITYAdapter(QuickSearchLocation.this, CityList);
                LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(QuickSearchLocation.this, LinearLayoutManager.VERTICAL, false);
                cityrecycler.setLayoutManager(horizontalLayoutManager1);
                cityrecycler.setAdapter(cityAdapter);
            }
        } else {
            if (!Network.isNetworkCheck(this)) {
                Toast.makeText(this, getResources().getString(R.string.internet), Toast.LENGTH_LONG).show();
            } else {
                HitURLforCityLIst();
            }
        }


        search_view.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                for (int i = 0; i < CityList.size(); i++) {
                    String temp = CityList.get(i).getCity_name();
                    temp = temp.toLowerCase();
                    if (temp.contains(cs.toString())) {
                        searchList.clear();
                        String id = CityList.get(i).getId();
                        String name = CityList.get(i).getCity_name();
                        Citymodel citymodel = new Citymodel();
                        citymodel.setCity_name(name);
                        citymodel.setId(id);
                        searchList.add(citymodel);
                    } else {

                    }
                }

                cityAdapter = new CITYAdapter(QuickSearchLocation.this, searchList);
                LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(QuickSearchLocation.this, LinearLayoutManager.VERTICAL, false);
                cityrecycler.setLayoutManager(horizontalLayoutManager1);
                cityrecycler.setAdapter(cityAdapter);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        dialog.show();
    }


    public void HitURLforCityLIst() {
        CityList=new ArrayList<>();
        MyProgressDialog.show(this, R.string.please_wait);
        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<Model_CityListing> userLoginCall = interfaceRetrofit.citylising(UrlConstants.City);
        userLoginCall.enqueue(new Callback<Model_CityListing>() {
            @Override
            public void onResponse(Call<Model_CityListing> call, retrofit2.Response<Model_CityListing> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().getCityList().size(); i++) {
                        Citymodel model_cityDetail = response.body().getCityList().get(i);
                        if (model_cityDetail.getError().equalsIgnoreCase("0")) {
                            CityList.add(model_cityDetail);
                            databaseHelper.saveCityDetails(model_cityDetail);
                        }
                    }
                    if (CityList.size() > 0) {
                        CITYAdapter cityAdapter = new CITYAdapter(QuickSearchLocation.this, CityList);
                        LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(QuickSearchLocation.this, LinearLayoutManager.VERTICAL, false);
                        cityrecycler.setLayoutManager(horizontalLayoutManager1);
                        cityrecycler.setAdapter(cityAdapter);
                    }
                }
                MyProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Model_CityListing> call, Throwable t) {
                Toast.makeText(QuickSearchLocation.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
                MyProgressDialog.dismiss();
            }
        });
    }

    public class CITYAdapter extends RecyclerView.Adapter<CITYAdapter.ViewHolder> implements Filterable {

        Context context;
        private ArrayList<Citymodel> CityList;
        private int lastSelectedPosition = -1;
        private ArrayList<Citymodel> filterList;

        public CITYAdapter(Context c, ArrayList<Citymodel> CityList) {
            this.context = c;
            this.CityList = CityList;
            this.filterList = CityList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(R.layout.citylistrecycler, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            citymodel = CityList.get(position);
            holder.tv_city_name.setText(CityList.get(position).getCity_name());
            holder.citychecked.setOnClickListener(v -> {
                holder.citychecked.setChecked(true);
                new Handler().postDelayed(() -> {
                    lastSelectedPosition = position;
                    notifyDataSetChanged();
                    city.setText(holder.tv_city_name.getText());
                    dialog.dismiss();
                }, 1000);


            });
        }

        @Override
        public int getItemCount() {
            return CityList.size();
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    String charString = constraint.toString();
                    if (charString.isEmpty()) {
                        filterList = CityList;
                    } else {
                        ArrayList<Citymodel> myList = new ArrayList<>();
                        for (Citymodel temp : CityList) {
                            if (temp.getCity_name().toLowerCase().contains(charString)) ;
                            filterList.add(temp);
                        }
                        filterList = myList;
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filterList;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    filterList = (ArrayList<Citymodel>) results.values;
                    notifyDataSetChanged();
                }
            };
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView tv_city_name;
            LinearLayout cuisinesLayout;
            RadioButton citychecked;
            RadioGroup citygroup;


            public ViewHolder(View itemView) {
                super(itemView);
                tv_city_name = (TextView) itemView.findViewById(R.id.tv_city_name);
                cuisinesLayout = (LinearLayout) itemView.findViewById(R.id.cuisines_layout);
                citychecked = (RadioButton) itemView.findViewById(R.id.citychecked);
                citygroup = (RadioGroup) itemView.findViewById(R.id.citygroup);


            }
        }
    }

    private void searchFromPassCode(String serviceName, String cusiine) {
        HomeFragment.passCode = CITY;
        String startPoint = "0";
        String endPoint = "15";

        MyProgressDialog.show(this, R.string.please_wait);
        restaurantModelArrayList = new ArrayList<RestaurantModel>();

        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<Model_Reasurant> userLoginCall = interfaceRetrofit.locationsearching("https://www.justfoodz.com/api-access/phpexpert_search.php?where=" + CITY + "&service=" + "Pizza" +"&startPoint=" + startPoint + "&endPoint=" + endPoint);
        userLoginCall.enqueue(new Callback<Model_Reasurant>() {
            @Override
            public void onResponse(Call<Model_Reasurant> call, retrofit2.Response<Model_Reasurant> response) {
                if (response.isSuccessful()) {

                    for (int i = 0; i < response.body().getSearchResult().size(); i++) {

                        RestaurantModel restaurantModel = response.body().getSearchResult().get(i);
                        if (restaurantModel.getError().equalsIgnoreCase("1")) {
                            restaurantModelArrayList.add(restaurantModel);
                        } else {
                            Toast.makeText(QuickSearchLocation.this, "Sorry! There are no restaurants In Your area.", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }


                    if (restaurantModelArrayList.size() > 0) {
                        Intent ii = new Intent(QuickSearchLocation.this, RestaurantsListActivity.class);
                        ii.putExtra("restaurantModelArrayList", restaurantModelArrayList);
                        ii.putExtra("passCode", HomeFragment.passCode);
                        ii.putExtra("cityname", CITY);
                        startActivity(ii);
                        finish();
                    }

                }

                MyProgressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<Model_Reasurant> call, Throwable t) {
                Toast.makeText(QuickSearchLocation.this, "" + t.toString(), Toast.LENGTH_SHORT).show();

                MyProgressDialog.dismiss();
            }
        });
    }

    private void showCustomDialog1decline(String s) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("" + s);

        alertDialog.setIcon(R.drawable.tick);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();

    }


    public void showSingleSearch() {
        dialog1 = new Dialog(QuickSearchLocation.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.picklocation);
        dialog1.getWindow().getAttributes().windowAnimations = R.style.diologAnimatioLocation;
        dialog1.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialog1.setCanceledOnTouchOutside(true);
        final ListView mAutoCompleteList;
        final EditText Address;


        Address = (EditText) dialog1.findViewById(R.id.adressText);
        mAutoCompleteList = (ListView) dialog1.findViewById(R.id.searchResultLV);
        final String GETPLACESHIT = "places_hit";


        Address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // optimised way is to start searching for laction after user has typed minimum 3 chars
                if (Address.getText().length() > 1) {


//                    searchBtn.setVisibility(View.GONE);

                    Runnable run = new Runnable() {


                        @Override
                        public void run() {

                            // cancel all the previous requests in the queue to optimise your network calls during autocomplete search
                            MyApplication.volleyQueueInstance.cancelRequestInQueue(GETPLACESHIT);

                            //build Get url of Place Autocomplete and hit the url to fetch result.
                            request = new VolleyJSONRequest(Request.Method.GET, getPlaceAutoCompleteUrl(Address.getText().toString()), null, null, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
//                                        searchBtn.setVisibility(View.VISIBLE);
                                    mAutoCompleteAdapter = null;
                                    Log.e("PLACES RESULT:::", response);
                                    Gson gson = new Gson();
                                    predictions = gson.fromJson(response, PlacePredictions.class);

                                    if (mAutoCompleteAdapter == null) {
                                        mAutoCompleteAdapter = new AutoCompleteAdapter(QuickSearchLocation.this, predictions.getPlaces(), QuickSearchLocation.this);
                                        mAutoCompleteList.setAdapter(mAutoCompleteAdapter);
                                    } else {
                                        mAutoCompleteAdapter.clear();
                                        mAutoCompleteAdapter.addAll(predictions.getPlaces());
                                        mAutoCompleteAdapter.notifyDataSetChanged();
                                        mAutoCompleteList.invalidate();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });

                            //Give a tag to your request so that you can use this tag to cancle request later.
                            request.setTag(GETPLACESHIT);

                            MyApplication.volleyQueueInstance.addToRequestQueue(request);

                        }

                    };

                    // only canceling the network calls will not help, you need to remove all callbacks as well
                    // otherwise the pending callbacks and messages will again invoke the handler and will send the request
                    if (handler != null) {
                        handler.removeCallbacksAndMessages(null);
                    } else {
                        handler = new Handler();
                    }
                    handler.postDelayed(run, 1000);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

        mAutoCompleteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                // pass the result to the calling activity
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        edt_placesearch.setText(predictions.getPlaces().get(position).getPlaceDesc());

                        dialog1.dismiss();

                    }
                }, 1000);
            }
        });

        dialog1.show();
    }

    public class AutoCompleteAdapter extends ArrayAdapter<PlaceAutoComplete> {
        ViewHolder holder;
        Context context;
        List<PlaceAutoComplete> Places;
        private Activity mActivity;

        public AutoCompleteAdapter(Context context, List<PlaceAutoComplete> modelsArrayList, Activity activity) {
            super(context, R.layout.autocomplete_row, modelsArrayList);
            this.context = context;
            this.Places = modelsArrayList;
            this.mActivity = activity;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            final ViewHolder holder;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.autocomplete_row, parent, false);
                holder = new ViewHolder();
                holder.name = (TextView) rowView.findViewById(R.id.place_name);
                holder.location = (TextView) rowView.findViewById(R.id.place_detail);

                rowView.setTag(holder);

            } else
                holder = (ViewHolder) rowView.getTag();
            /***** Get each Model object from ArrayList ********/
            holder.Place = Places.get(position);
            StringTokenizer st = new StringTokenizer(holder.Place.getPlaceDesc(), ",");
            /************  Set Model values in Holder elements ***********/

            holder.name.setText(st.nextToken());
            String desc_detail = "";
            for (int i = 1; i < st.countTokens(); i++) {
                if (i == st.countTokens() - 1) {
                    desc_detail = desc_detail + st.nextToken();
                } else {
                    desc_detail = desc_detail + st.nextToken() + ",";
                }
            }
            holder.location.setText(desc_detail);
            return rowView;


        }


        class ViewHolder {
            PlaceAutoComplete Place;
            TextView name, location;

        }

        @Override
        public int getCount() {
            return Places.size();
        }
    }

    public String getPlaceAutoCompleteUrl(String input) {
        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/place/autocomplete/json");
        urlString.append("?input=");
        try {
            urlString.append(URLEncoder.encode(input, "utf8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

//        urlString.append("&amp;location=");
//        urlString.append(latitude + "," + longitude);
        urlString.append("&amp;radius=500&amp;language=en");

//        urlString.append("&types=(cities)");
        urlString.append("&components=country:" + myPref.getUserName());//country code

        urlString.append("&key=" + "AIzaSyDNyevW_K3I8Nk_6Rg6jntytgi0W0rNu58");

        Log.d("FINAL URL:::   ", urlString.toString());
        return urlString.toString();
    }
}
