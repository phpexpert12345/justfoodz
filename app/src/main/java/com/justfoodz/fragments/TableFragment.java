package com.justfoodz.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.justfoodz.activities.HomeActivity;
import com.justfoodz.R;
import com.justfoodz.models.Citymodel;
import com.justfoodz.models.Restaurantlistmodel;
import com.justfoodz.utils.AuthPreference;
import com.justfoodz.utils.Network;
import com.justfoodz.utils.UrlConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TableFragment extends Fragment {
    LinearLayout restaurantlayout, citylayout, tablelayout, datedialog, timedialog;
    Dialog restaurantdialog, citydialog, tabledialog;
    private ArrayList<Restaurantlistmodel> restaurantList;

    ProgressDialog pDialog;
    RecyclerView restaurantrecycler, cityrecycler, tablerecycler;
    Citymodel citymodel;
    String CITY, Restaurantid, tableid, CUSTOMERid, Restrarantid;
    Button submit;
    EditText mobilenumber, noofguests, specialinstructions;
    String MOBILENUMBER, DATE, TIME, NOOFGUESTS, SPECAILINSTRUC;
    AuthPreference authPreference;
    private ArrayList<Citymodel> searchList;
    private ArrayList<Restaurantlistmodel> RestrasearchList;
    private ArrayList<Citymodel> cityList;


    private ArrayList<Citymodel> tablenoList;
    private TableNumberAdapter tableNumberAdapter;
    TextView restaurantname, city, tablenumber, error, date, time;
    private int mYear, mMonth, mDay;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    String amPm;
    TimePickerDialog timePickerDialog;
    EditText search_view, restra_search_view;
    Restaurantlistmodel restaurantlistmodel;
    private String id;

    public TableFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_table, container, false);
//        restaurantlayout = view.findViewById(R.id.restaurantlayout);
//        citylayout = view.findViewById(R.id.citylayout);
        tablelayout = view.findViewById(R.id.tablelayout);
        restaurantname = view.findViewById(R.id.restaurantname);
        submit = view.findViewById(R.id.submit);
        city = view.findViewById(R.id.city);
        tablenumber = view.findViewById(R.id.tablenumber);
        error = view.findViewById(R.id.error);
        mobilenumber = view.findViewById(R.id.mobilenumber);
        date = view.findViewById(R.id.date);
        time = view.findViewById(R.id.time);
        noofguests = view.findViewById(R.id.noofguests);
        datedialog = view.findViewById(R.id.datedialog);
        timedialog = view.findViewById(R.id.timedialog);
        specialinstructions = view.findViewById(R.id.specialinstructions);
        AuthPreference authPreference = new AuthPreference(getActivity());
        CUSTOMERid = authPreference.getCustomerId();
        Restrarantid = authPreference.getRestaurantId();
        id = getArguments().getString("id");


        restaurantList = new ArrayList<>();
        searchList = new ArrayList<>();
        RestrasearchList = new ArrayList<>();


        tablelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTablelistDialog();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tablenumber.getText().toString().length() == 0) {
                    Toast.makeText(getActivity(), "Please Select Your Table Number", Toast.LENGTH_LONG).show();
                } else if (mobilenumber.getText().toString().length() == 0) {
                    Toast.makeText(getActivity(), "Please Enter Mobile Number", Toast.LENGTH_LONG).show();
                } else if (date.getText().toString().length() == 0) {
                    Toast.makeText(getActivity(), "Please Select Date", Toast.LENGTH_LONG).show();
                } else if (time.getText().toString().length() == 0) {
                    Toast.makeText(getActivity(), "Please Select Time", Toast.LENGTH_LONG).show();
                } else if (noofguests.getText().toString().length() == 0) {
                    Toast.makeText(getActivity(), "Please Enter  No. of Guests", Toast.LENGTH_LONG).show();
                } else {
                    HitUrlForTableBookingData();

                }
            }
        });

        datedialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }

        });
        timedialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        time.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });
        return view;
    }


    public void HitUrlForTableBookingData() {

        MOBILENUMBER = mobilenumber.getText().toString();
        DATE = date.getText().toString();
        TIME = time.getText().toString();
        NOOFGUESTS = noofguests.getText().toString();
        SPECAILINSTRUC = specialinstructions.getText().toString();

        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.TableBookingAPi, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ressponse", response);
                pDialog.dismiss();
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    int success = jsonObj.getInt("success");
                    if (success == 0) {

                        String success_msg = jsonObj.optString("success_msg");
                        showCustomDialog1decline(success_msg);

                    } else if (success == 1) {
                        String success_msg = jsonObj.optString("success_msg");
                        showCustomDialog1decline(success_msg);
                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                showCustomDialog1decline(error.getMessage());

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to customer login
                Map<String, String> params = new HashMap<String, String>();
                params.put("restaurant_id", id);
                params.put("table_number_assign", tableid);
                params.put("booking_mobile", MOBILENUMBER);
                params.put("booking_date", DATE);
                params.put("booking_time", TIME);
                params.put("noofgusts", NOOFGUESTS);
                params.put("booking_instruction", SPECAILINSTRUC);
                params.put("customer_id", CUSTOMERid);
                return params;
            }

        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(strReq);
    }


    public void showTablelistDialog() {
        tabledialog = new Dialog(getActivity());
        tabledialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        tabledialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        tabledialog.setContentView(R.layout.tablebookingtablenolist);
        tabledialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        tabledialog.getWindow().getAttributes().windowAnimations = R.style.diologIntertnet;
        tabledialog.setCanceledOnTouchOutside(true);
        tablerecycler = (RecyclerView) tabledialog.findViewById(R.id.tablerecycler);


        HitURLforTableNumberList();


        tabledialog.show();
    }


    public void HitURLforTableNumberList() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(true);
        tablenoList = new ArrayList<Citymodel>();
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.GetRestaurantTableNumberList, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ressponse", response);
                pDialog.dismiss();
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray citylist = jsonObj.getJSONArray("RestaurantTableList");
                    for (int i = 0; i < citylist.length(); i++) {
                        JSONObject c = citylist.getJSONObject(i);

                        int error = c.getInt("error");
                        if (error == 1) {
                            String error_msg = c.getString("error_msg");
                            showCustomDialog1decline(error_msg);
                            tabledialog.dismiss();
                            pDialog.dismiss();
                        } else {

                            String ttableid = c.getString("id");
                            String restaurant_id = c.getString("restaurant_id");
                            String table_caption = c.getString("table_caption");
                            String restaurantCity = c.getString("table_number");
                            String table_no_guest_available = c.getString("table_no_guest_available");
                            String tabledisvplay = c.getString("Table_display_view");


                            Citymodel citymodel = new Citymodel();
                            citymodel.setCity_name(tabledisvplay);
                            citymodel.setId(id);
                            tablenoList.add(citymodel);
                            TableNumberAdapter tableNumberAdapter = new TableNumberAdapter(getActivity(), tablenoList);
                            LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            tablerecycler.setLayoutManager(horizontalLayoutManager1);
                            tablerecycler.setAdapter(tableNumberAdapter);
                            pDialog.dismiss();
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                showCustomDialog1decline(error.getMessage());

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to customer login
                Map<String, String> params = new HashMap<String, String>();
                params.put("restaurant_id", id);
                Log.e("dfffffffffff", "" + id);
                return params;
            }

        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(strReq);
    }

    private void showCustomDialog1decline(String s) {
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
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

    public class TableNumberAdapter extends RecyclerView.Adapter<TableNumberAdapter.ViewHolder> {

        Context context;
        private ArrayList<Citymodel> tablenoList;
        private int lastSelectedPosition = -1;


        public TableNumberAdapter(Context c, ArrayList<Citymodel> tablenoList) {
            this.context = c;
            this.tablenoList = tablenoList;


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

//            holder.tv_city_name.setText(pp.get(position).getCityname());
            citymodel = tablenoList.get(position);
            holder.tv_city_name.setText(tablenoList.get(position).getCity_name());

            holder.citychecked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.citychecked.setChecked(true);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tableid = tablenoList.get(position).getId();
                            lastSelectedPosition = position;
                            notifyDataSetChanged();
                            tablenumber.setText(holder.tv_city_name.getText());
                            tabledialog.dismiss();
                            pDialog.dismiss();
                        }
                    }, 1000);


                }
            });
        }

        @Override
        public int getItemCount() {
            return tablenoList.size();
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
}



