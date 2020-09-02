package com.justfoodz.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.justfoodz.Multipart.MultipartRequest;
import com.justfoodz.Multipart.NetworkOperationHelper;
import com.justfoodz.R;
import com.justfoodz.utils.AuthPreference;
import com.justfoodz.utils.UrlConstants;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WriteReviewActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivBack,imgreview;
    private TextView tvPublishReview,txtimage;
    private TextView txtrating;
    private MaterialEditText edtWriteReview;
    private String  writeReview;
    private ProgressDialog pDialog;
    private RelativeLayout rlRestaurantName;
    RatingBar ratingBar;
    AuthPreference authPreference;
    ArrayList <String> name;
    ArrayList<String> id;
    Spinner spinner_restaurant_order_name;
    String id_to_send="",rating_review_tosend="0";

    private int REQUEST_CAMERA = 0;
    private static int RESULT_LOAD_IMAGE = 1;
    String imageviewSelected = "";
    private String UserProfileImage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);
        authPreference = new AuthPreference(this);
        statusBarColor();
        initialization();
        initializedListener();
        displayMyOrder();
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rating_review_tosend = String.valueOf(rating);
                txtrating.setText(""+rating_review_tosend+" Star");
            }
        });
        spinner_restaurant_order_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long idd) {
                if (position>0)
                {
                    id_to_send = ""+id.get(position-1);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void initialization() {
        name = new ArrayList<>();
        id = new ArrayList<>();
        ivBack = findViewById(R.id.iv_back);
        imgreview = findViewById(R.id.imgreview);
        txtrating = findViewById(R.id.txtrating);
        txtimage = findViewById(R.id.txtimage);
        spinner_restaurant_order_name = findViewById(R.id.spinner_restaurant_order_name);
        edtWriteReview = findViewById(R.id.edt_write_review);
        tvPublishReview = findViewById(R.id.tv_publish_review);
        rlRestaurantName = findViewById(R.id.rl_restaurant_name);
        ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setNumStars(5);
    }

    private void initializedListener() {
        ivBack.setOnClickListener(this);
        tvPublishReview.setOnClickListener(this);
        rlRestaurantName.setOnClickListener(this);
        txtimage.setOnClickListener(this);
        customerId = authPreference.getCustomerId();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_publish_review:
                writeReviewValidation();

                break;
            case R.id.ratingBar:
                showRestaurantList();
                break;
            default:
            case R.id.txtimage:
                String title = "Open Photo";
                CharSequence[] itemlist ={"Take a Photo", "Pick from Gallery"};

             AlertDialog.Builder builder = new AlertDialog.Builder(WriteReviewActivity.this);
                builder.setTitle(title);
                builder.setItems(itemlist, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:// Take Photo
                                takePhotoFromCamera();
                                break;
                            case 1:// Choose Existing Photo
                                imageviewSelected = "profilepic";
                                selectImage();
                                break;

                            default:
                                break;
                        }
                    }
                });
         AlertDialog alert = builder.create();
                alert.setCancelable(true);
                alert.show();
                break;
        }
    }

    private void showRestaurantList() {

    }

    private void displayMyOrder() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        name.clear();
        id.clear();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.ORDER_LISTS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                try {
                    Log.e("rere",""+response);
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    if (success == 0) {
                        JSONObject orders = jObj.getJSONObject("orders");
                        JSONArray jsonArray = orders.getJSONArray("OrderViewResult");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String orderIdentifyno = jsonObject.getString("order_identifyno");
                            String order_time = jsonObject.getString("order_time");
                            String restaurant_id = jsonObject.getString("restaurant_id");
                            String ordPrice = jsonObject.getString("ordPrice");
                            String order_type = jsonObject.getString("order_type");
                            String payment_mode = jsonObject.getString("payment_mode");
                            String restaurant_name = jsonObject.getString("restaurant_name");
                            String restaurant_address = jsonObject.getString("restaurant_address");
                            String order_display_message = jsonObject.getString("order_display_message");
                            String order_date = jsonObject.getString("order_date");

//                            orderModel = new OrderModel();
//                            orderModel.setRestaurant_name(restaurant_name);
//                            orderModel.setOrder_identifyno(orderIdentifyno);
//                            orderModel.setOrdPrice(ordPrice);
//                            orderModel.setRestaurant_name(restaurant_name);
//                            orderModel.setRestaurant_address(restaurant_address);
//                            orderModel.setPayment_mode(payment_mode);
//                            orderModel.setOrder_type(order_type);
//                            orderModel.setOrder_date(order_date);
//                            orderModel.setOrder_time(order_time);
//                            orderModel.setRestaurant_id(restaurant_id);
//                            orderModel.setOrder_display_message(order_display_message);
//                            orderModelArrayList.add(orderModel);
                            id.add(restaurant_id);
                            name.add(restaurant_name+" ("+orderIdentifyno+")");
                        }
                        name.add(0,"Select your Order");
                        ArrayAdapter arrayAdapter = new ArrayAdapter(WriteReviewActivity.this,android.R.layout.simple_dropdown_item_1line,name);
                        spinner_restaurant_order_name.setAdapter(arrayAdapter);


                    } else {
                        String success_msg = jObj.getString("success_msg");
                        Toast.makeText(getApplicationContext(), success_msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Please check your network connection", Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to customer login

                Map<String, String> params = new HashMap<String, String>();
                String customerId = authPreference.getCustomerId();
                params.put("CustomerId", customerId);
                return params;
            }

        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(strReq);
    }


    public void writeReviewValidation() {
        writeReview = edtWriteReview.getText().toString().trim();
        if (id_to_send.equals("")) {
            showCustomDialog1decline ("Select your order","2");
        }        else if (rating_review_tosend.equals("0")){
            showCustomDialog1decline ("Give rating","2");
        } else if (writeReview.isEmpty()) {
            edtWriteReview.setError("Enter Order Issue");
            edtWriteReview.requestFocus();
        } else if (com.justfoodz.utils.Network.isNetworkCheck(WriteReviewActivity.this)) {


            if(UserProfileImage.isEmpty()){
                userLogin();
            }

          else   updateReview();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }
    }



    private void statusBarColor() {
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void showCustomDialog1decline (String s, final String todo)
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(WriteReviewActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(""+s);

        alertDialog.setIcon(R.drawable.tick);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if (todo.equals("1"))
                {
                    startActivity(new Intent(WriteReviewActivity.this, HomeActivity.class));
                    finish();
                }
                else {
                    alertDialog.dismiss();
                }
            }
        });
        alertDialog.show();
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
        imageviewSelected = "profilepic";
    }

    private void selectImage() {
        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK && null != data) {
//                Bitmap photo = (Bitmap) data.getExtras().get("data");
//                panpic.setImageBitmap(photo);

                if (imageviewSelected == "profilepic") {
                    imgreview.setVisibility(View.VISIBLE);
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    imgreview.setImageBitmap(photo);
                    Uri tempUri = getImageUri(getApplicationContext(), photo);
                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    File finalFile = new File(getRealPathFromURI(tempUri));
                    UserProfileImage =  String.valueOf(finalFile);
                    Log.e("image",""+finalFile+"     "+tempUri);
                }
            } else if (imageviewSelected == "profilepic") {
                imgreview.setVisibility(View.VISIBLE);
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                UserProfileImage = cursor.getString(columnIndex);
                imgreview.setImageBitmap(decodeUri(selectedImage));
                cursor.close();
            }
        } catch (Exception e) {
            Log.d("expception:", "" + e.getMessage());
            Toast.makeText(this, "Something went wrong" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);

                cursor.close();
            }
        }
        return path;
    }

    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(
                getContentResolver().openInputStream(selectedImage), null, o);

        final int REQUIRED_SIZE = 100;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(
                getContentResolver().openInputStream(selectedImage), null, o2);
    }

    String customerId;
    private void updateReview() {

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();

        MultipartRequest req = null;
        HashMap<String, String> params = new HashMap<>();

        params.put("CustomerId", customerId);
        params.put("resid", id_to_send);
        params.put("RestaurantReviewRating", rating_review_tosend);
        params.put("RestaurantReviewContent", writeReview);

        try {
            req = new MultipartRequest( UrlConstants.WRITE_REVIEW_URL, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    showCustomDialog(error.getMessage());
                    return;
                }
            }, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Success", ""+response);
                    pDialog.dismiss();
                    try {
                        JSONObject jObj = new JSONObject(response);
                        int success = jObj.getInt("success");
                        if (success == 1) {
                            String success_msg = jObj.optString("success_msg");
                            showCustomDialog1decline (success_msg,"1");
                        } else if (success == 0) {
                            String error_msg = jObj.getString("error_msg");
                            showCustomDialog1decline (error_msg,"2");
                        } else if (success == 2) {
                            String success_msg = jObj.getString("success_msg");
                            showCustomDialog1decline (success_msg,"2");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, params);


            req.addImageData("ReviewPhoto", new File(UserProfileImage));
            Log.e("qqqq",""+UserProfileImage);
            NetworkOperationHelper.getInstance(WriteReviewActivity.this).addToRequestQueue(req);


        } catch (UnsupportedEncodingException e) {
            pDialog.dismiss();
            e.printStackTrace();

        } catch (NullPointerException e) {
            pDialog.dismiss();
            e.printStackTrace();
        }
    }

    private void showCustomDialog (String s)
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(""+s);

        alertDialog.setIcon(R.drawable.tick);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();

    }

    private void userLogin() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.WRITE_REVIEW_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    int success = object.getInt("success");
                    if (success == 1) {
                        String success_msg = object.optString("success_msg");

                        showCustomDialog1decline(success_msg, "1");

                    } else {
                        String success_msg = object.optString("success_msg");
                        showCustomDialog1decline(success_msg, "2");

                    }

                } catch (JSONException e) {
                    pDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("CustomerId", customerId);
                params.put("resid", id_to_send);
                params.put("RestaurantReviewRating", rating_review_tosend);
                params.put("RestaurantReviewContent", writeReview);
                params.put("ReviewPhoto", "");

                return params;
            }
        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(strReq);
    }

}
