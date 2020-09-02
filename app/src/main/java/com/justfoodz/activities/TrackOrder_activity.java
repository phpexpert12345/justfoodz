package com.justfoodz.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;

import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.ui.IconGenerator;
import com.justfoodz.DataParser;

import com.justfoodz.R;
import com.justfoodz.RoundCornerDrawable;
import com.justfoodz.utils.AuthPreference;
import com.justfoodz.utils.UrlConstants;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class TrackOrder_activity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ImageView back, driverpic, phonecall;

    RequestQueue requestQueue;
    String RestroLat = "";
    String RestroLan = "";
    String CustomerLat = "";
    String CustomerLng = "";
    String RiderLat = "";
    String RiderLng = "";
    private Marker sourceMarker;
    private Marker destinationMarker;
    private Marker riderMarker;
    TextView driver_name, driver_number, txtriderinfo;
    AuthPreference authPreference;
    TimerTask mTimerTask;
    final Handler handler = new Handler();
    Timer t = new Timer();
    String driverphone_number, d_name;
    LatLng Restaurant;
    LatLng CustomerLocation;
    LatLng Riderlocation;
    String orderIdentifyNo;
//            ,restroname,restroadd,customername,customeradd,timeduration,distance,duration;
//    public static String sourceaddress,destinationaddress,trip_id,txt_to_locationmap,txt_time_distancemap,txt_duration_textmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_orderrr);
        statusBarColor();
        requestQueue = Volley.newRequestQueue(this);
        orderIdentifyNo = getIntent().getStringExtra("orderIdentifyNo");


        authPreference = new AuthPreference(this);
        driverpic = (ImageView) findViewById(R.id.driverpic);
        phonecall = (ImageView) findViewById(R.id.phonecall);
        driver_name = (TextView) findViewById(R.id.driver_name);
        driver_number = (TextView) findViewById(R.id.driver_number);
        txtriderinfo = (TextView) findViewById(R.id.txtriderinfo);

        Trackorder();
        doTimerTask();

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent o = new Intent(TrackOrder_activity.this,MainActivity.class);
//                startActivity(o);
                t.cancel();
                finish();
            }
        });

        phonecall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t.cancel();
                String phone = driverphone_number;
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent i = new Intent(TrackOrder_activity.this,MainActivity.class);
//        startActivity(i);
        t.cancel();
        finish();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;


        Log.e("aaa", "rest " + RestroLat + " " + RestroLan + "cutomer " + CustomerLat + "  " + CustomerLng + " rider" + RiderLat + " " + RiderLng);
        Double resturentLat = Double.valueOf(RestroLat);
        Double resturentLng = Double.valueOf(RestroLan);
        Double customerLat = Double.valueOf(CustomerLat);
        Double customerLng = Double.valueOf(CustomerLng);
        Double riderLat = Double.valueOf(RiderLat);
        Double riderLng = Double.valueOf(RiderLng);


        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


//        LatLng Restaurant = new LatLng(resturentLat, resturentLng);
//        mMap.addMarker(new MarkerOptions().position(Restaurant).title("Restaurant Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.restromarker)));


//           Restaurant = new LatLng(28.8316, 77.5780);
        Restaurant = new LatLng(resturentLat, resturentLng);
        // mMap.addMarker(new MarkerOptions().position(Restaurant).title("Restaurant Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.restromarker)));


//         CustomerLocation = new LatLng(28.6208, 77.3639);
        CustomerLocation = new LatLng(customerLat, customerLng);
        //  mMap.addMarker(new MarkerOptions().position(CustomerLocation).title("Customer Location"));

//        LatLng Riderlocation = new LatLng(28.6636, 77.3698);
        Riderlocation = new LatLng(riderLat, riderLng);
        //   mMap.addMarker(new MarkerOptions().position(Riderlocation).title("Rider Location"));

        List<LatLng> path = new ArrayList();

        //Execute Directions API request
        List<LatLng> path1 = new ArrayList();

        //Execute Directions API request

        //Draw the polyline

        CameraPosition cameraPosition = new CameraPosition.Builder().target(Restaurant).zoom(13).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        MarkerOptions sOptions = new MarkerOptions()
                .anchor(0.5f, 0.75f)
                .position(Restaurant)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.sidebar));
        MarkerOptions dOptions = new MarkerOptions()
                .anchor(0.5f, 0.75f)
                .position(CustomerLocation)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.sidebar));

        MarkerOptions rOption = new MarkerOptions()
                .anchor(0.5f, 0.75f)
                .position(Riderlocation)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ridermarker));

        sourceMarker = mMap.addMarker(sOptions);
        destinationMarker = mMap.addMarker(dOptions);
        riderMarker = mMap.addMarker(rOption);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(sourceMarker.getPosition());
        builder.include(destinationMarker.getPosition());
        LatLngBounds bounds = builder.build();
        int padding = 10; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.moveCamera(cu);

        String url = getUrl(Double.parseDouble("" + resturentLat), Double.parseDouble("" + resturentLng)
                , Double.parseDouble("" + customerLat), Double.parseDouble("" + customerLng));

        FetchUrl fetchUrl = new FetchUrl();
        fetchUrl.execute(url);

        LatLng TripTime = new LatLng(40.416775, -4.70379);
        mMap.addMarker(new MarkerOptions().position(TripTime).title("Marker in Time"));
    }


//    private View getInfoWindow(String distance, String duration, boolean isMyLocation) {
//        View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
//                (FrameLayout) findViewById(R.id.map), false);
//        TextView tv_desc = (TextView) infoWindow.findViewById(R.id.tv_desc);
//        LinearLayout info_window_time = (LinearLayout) infoWindow.findViewById(R.id.info_window_time);
//        TextView tv_distance = (TextView) infoWindow.findViewById(R.id.tv_distance);
//        TextView tv_title = (TextView) infoWindow.findViewById(R.id.tv_title);
//        TextView tv_duration = (TextView) infoWindow.findViewById(R.id.tv_duration);
//        ImageView imgNavigate = (ImageView) infoWindow.findViewById(R.id.imgNavigate);
//
//        //  ImageView imageView = (ImageView) infoWindow.findViewById(R.id.iv_scheduled_ride);
//        // distance = distance.toUpperCase();
//        if (isMyLocation) {
//            info_window_time.setVisibility(View.VISIBLE);
//            tv_title.setVisibility(View.VISIBLE);
//            tv_duration.setText(duration);
//            //   imageView.setImageResource(R.drawable.amu_bubble_mask);
//            tv_distance.setText(distance);
//            tv_title.setText(restroname);
//            tv_desc.setText(restroadd);
//            tv_desc.setMaxLines(1);
//        } else {
//            tv_desc.setMaxLines(2);
//            tv_title.setText(customername);
//            tv_desc.setText(customeradd);
//            info_window_time.setVisibility(View.GONE);
////            tv_title.setVisibility(View.VISIBLE);
//        }
//        return infoWindow;
//    }


    private String getUrl(double source_latitude, double source_longitude, double dest_latitude, double dest_longitude) {

        // Origin of route
        String str_origin = "origin=" + source_latitude + "," + source_longitude;

        // Destination of route
        String str_dest = "destination=" + dest_latitude + "," + dest_longitude;


        // Sensor enabled
        String sensor = "sensor=false" + "&key=" + getString(R.string.google_maps_key);

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        Log.e("url", url + "");
        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            String distance = "";
            String duration = "";
//            isDragging = false;
            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) {
                        distance = (String) point.get("distance");
                        continue;
                    } else if (j == 1) {
                        duration = (String) point.get("duration");
                        continue;
                    }


                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }

                if (Restaurant != null && CustomerLocation != null) {

                }

                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                int height = size.y / 6;

                System.out.println("HEIGHT IS " + height + "WIDTH IS " + width);
                mMap.setPadding(100, 100, 100, 100);


                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(sourceMarker.getPosition());
                builder.include(destinationMarker.getPosition());
                LatLngBounds bounds = builder.build();
                int padding = 0; // offset from edges of the map in pixels
//                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
//                    mMap.moveCamera(cu);

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(5);
                lineOptions.color(Color.BLACK);
                Log.d("onPostExecute", "onPostExecute lineoptions decoded");
            }
//            mMap.redirectMap(source_lat,source_lng,dest_lat,dest_lng);
            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null && points != null) {
                mMap.addPolyline(lineOptions);
//                startAnim(points);
            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }
    }


    public void Trackorder() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants.Rider_Track, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("qwqwqw", "" + response);
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("OrderDetailItem");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        int success = jsonObject1.getInt("success");

                        String restaurant_lat = jsonObject1.getString("restaurant_lat");
                        String restaurant_lng = jsonObject1.getString("restaurant_lng");
                        String customer_lat = jsonObject1.getString("customer_lat");
                        String customer_lng = jsonObject1.getString("customer_lng");
                        String rider_last_lng = jsonObject1.getString("rider_last_lng");
                        String rider_last_lat = jsonObject1.getString("rider_last_lat");
                        String DriverMobileNo = jsonObject1.getString("DriverMobileNo");
                        String DriverPhoto = jsonObject1.getString("DriverPhoto");
                        String rider_delivery_message_for_customer = jsonObject1.getString("rider_delivery_message_for_customer");
                        String DriverFirstName = jsonObject1.getString("DriverFirstName") + " " + jsonObject1.getString("DriverLastName");
                        Log.d("TAG", "late..........." + rider_last_lat + "...." + rider_last_lat);
                        //  String restaurant_name = jsonObject1.getString("restaurant_name");
                        //   String restaurant_address = jsonObject1.getString("restaurant_address");
                        //   String customer_name = jsonObject1.getString("customer_name");
                        //   String customer_address = jsonObject1.getString("customer_address");
                        txtriderinfo.setText("" + rider_delivery_message_for_customer);
                        Glide.with(TrackOrder_activity.this).load(DriverPhoto).into(driverpic);

                        driverphone_number = DriverMobileNo;
                        d_name = DriverFirstName;
                        RestroLat = restaurant_lat;
                        RestroLan = restaurant_lng;
                        CustomerLat = customer_lat;
                        CustomerLng = customer_lng;
                        RiderLat = rider_last_lat;
                        RiderLng = rider_last_lng;

//                            restroname= restaurant_name;
//                            restroadd = restaurant_address;
//                            customeradd = customer_address;
//                            customername = customer_name;
//                           Log.e("latandlong",""+RestroLat,RestroLan,CustomerLat,CustomerLng,RiderLat,RiderLng);
                        if (success == 1) {
                            if (RestroLat.equals("") || RestroLat.equals("null") || RestroLat.equals("Null") || RestroLat.equals("NULL") || RestroLan.equals("") || RestroLan.equals("null") || RestroLan.equals("Null") || RestroLan.equals("NULL") || CustomerLat.equals("") || CustomerLat.equals("null") || CustomerLat.equals("Null") || CustomerLat.equals("NULL") || CustomerLng.equals("") || CustomerLng.equals("null") || CustomerLng.equals("Null") || CustomerLng.equals("NULL") || RestroLan.equals("") || RestroLan.equals("null") || RestroLan.equals("Null") || RestroLan.equals("NULL") || RiderLat.equals("") || RiderLat.equals("null") || RiderLat.equals("Null") || RiderLat.equals("NULL")) {
                                Toast.makeText(TrackOrder_activity.this, "Didn't find  latitude or longitude", Toast.LENGTH_SHORT).show();
                            } else {
                                driver_name.setText(d_name);
                                driver_number.setText(driverphone_number);
                                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                                mapFragment.getMapAsync(TrackOrder_activity.this);
                            }
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getMessage();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("order_reference_number", orderIdentifyNo);
                Log.e("params", "" + params);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void doTimerTask() {

        mTimerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        Trackorder();
//                        Toast.makeText(TrackOrder_activity.this, "Test", Toast.LENGTH_SHORT).show();
                        Log.d("TIMER", "TimerTask run");
                    }
                });
            }
        };

        // public void schedule (TimerTask task, long delay, long period)
        t.schedule(mTimerTask, 1000, 10000);  //

    }

    private void statusBarColor() {
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
    }

}
