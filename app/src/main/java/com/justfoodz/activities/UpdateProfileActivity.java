package com.justfoodz.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import com.justfoodz.MyProgressDialog;
import com.justfoodz.R;
import com.justfoodz.retrofit.InterUserdata;
import com.justfoodz.retrofit.Model_CustomerProfile;
import com.justfoodz.retrofit.Model_location;
import com.justfoodz.retrofit.RetrofitAdapter;
import com.justfoodz.utils.AuthPreference;
import com.justfoodz.utils.MyPref;
import com.justfoodz.utils.UrlConstants;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;

public class UpdateProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivBack;
    private Button Submit;
    private EditText edtCustomerId;
    MaterialEditText edtFirstName, edtLastName,  edtPhoneNumber,
            edtFloor, edtStreetName, edtZipCode, edtCity, edtState;
    private TextView edtEmail;
    private AuthPreference authPreference;
    private String s = "Customer id:";
    private String customerId, firstName, lastName, userPhone, userEmail, userFloor, userStreetName, userZipCode, city, state, userAddress;
    private ProgressDialog pDialog;
    ImageView profile_image;

    private int REQUEST_CAMERA = 0;
    private String UserProfileImage = "";
    String imageviewSelected = "";
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    Bitmap bitmap;
    private static int RESULT_LOAD_IMAGE = 1;
    MyPref myPref ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        myPref= new MyPref(UpdateProfileActivity.this);
        initialization();
        initializedListener();
        statusBarColor();
        getProfileData();

    }


    private void initialization() {
        authPreference = new AuthPreference(this);
        ivBack = findViewById(R.id.iv_back);
        Submit = findViewById(R.id.btn_submit);
        edtCustomerId = findViewById(R.id.edt_customer_id);
        edtFirstName = findViewById(R.id.edt_first_name);
        edtLastName = findViewById(R.id.edt_last_name);
        edtEmail = findViewById(R.id.edt_email);
        edtPhoneNumber = findViewById(R.id.edt_phone_number);
        edtFloor = findViewById(R.id.edt_floor);
        edtStreetName = findViewById(R.id.edt_street_name);
        edtZipCode = findViewById(R.id.edt_zip_code);
        edtCity = findViewById(R.id.edt_city);
        edtState = findViewById(R.id.edt_state);
        profile_image = findViewById(R.id.profile_image);
        authPreference = new AuthPreference(this);
        customerId = authPreference.getCustomerId();


        Log.e("auther", "" + authPreference.getFirstName() + authPreference.getUserPostcode());
        if (authPreference.getUserPhoto().equals("") || (authPreference.getUserPhoto().equals(null))) {

        } else {
            Picasso.get().load(authPreference.getUserPhoto()).into(profile_image);
        }

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = "Open Photo";
                CharSequence[] itemlist = {"Take a Photo", "Pick from Gallery"};

                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfileActivity.this);
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

            }
        });
    }

    private void selectImage() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }


    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
        imageviewSelected = "profilepic";
    }


    private void initializedListener() {
        ivBack.setOnClickListener(this);
        Submit.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                Intent intent = new Intent(UpdateProfileActivity.this, HomeActivity.class);
                startActivity(intent);
//                HomeActivity.mDrawerLayout.closeDrawer(HomeActivity.rl_main_left_drawer);
                break;
            case R.id.btn_submit:
                if (UserProfileImage.isEmpty()) {
                    userLogin();
                } else
                    updateProfileValidation();
                break;
            default:
                break;
        }

    }

    public void updateProfileValidation() {
        firstName = edtFirstName.getText().toString().trim();
        lastName = edtLastName.getText().toString().trim();
        userEmail = edtEmail.getText().toString().trim();
        userPhone = edtPhoneNumber.getText().toString();
//        userFloor = edtFloor.getText().toString().trim();
        userStreetName = edtStreetName.getText().toString().trim();
        userZipCode = edtZipCode.getText().toString().trim();
        city = edtCity.getText().toString().trim();
        state = edtState.getText().toString().trim();

        if (firstName.isEmpty()) {
            edtFirstName.setError("Enter First Name");
            edtFirstName.requestFocus();
        } else if (lastName.isEmpty()) {
            edtLastName.setError("Enter Last Name");
            edtLastName.requestFocus();
        } else if (!isValidEmail(userEmail)) {
            edtEmail.setError("Enter Valid Email Address");
            edtEmail.requestFocus();
        } else if (userPhone.isEmpty()) {
            edtPhoneNumber.setError("Enter Phone Number");
            edtPhoneNumber.requestFocus();
        } else if (userStreetName.isEmpty()) {
            edtStreetName.setError("Enter Complete Address");
            edtStreetName.requestFocus();
        } else if (com.justfoodz.utils.Network.isNetworkCheck(UpdateProfileActivity.this)) {

            updateProfile();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }
    }


    private void updateProfile() {

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();

        MultipartRequest req = null;
        HashMap<String, String> params = new HashMap<>();
        params.put("CustomerId", "" + customerId);
        params.put("CustomerFirstName", "" + firstName);
        params.put("CustomerLastName", "" + lastName);
        params.put("CustomerEmail", "" + userEmail);
        params.put("CustomerPhone", "" + userPhone);
        params.put("customerAppFloor", "" + userFloor);
        params.put("customerStreet", "" + userStreetName);
        params.put("customerZipcode", "" + userZipCode);
        params.put("customerCity", "" + city);
        params.put("customerState", "" + state);
        params.put("customer_country", myPref.getState());
        params.put("customer_city", myPref.getCity());
        params.put("customer_lat", myPref.getLatitude());
        params.put("customer_long", myPref.getLongitude());

        try {
            req = new MultipartRequest(UrlConstants.UPDATE_PROFILE_URL, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    showCustomDialog(error.getMessage());
                    return;
                }
            }, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Success", "" + response);
                    pDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(response);
                        int success = object.getInt("success");
                        if (success == 1) {
                            String success_msg = object.optString("success_msg");
                            String user_photo = object.optString("user_photo");
                            authPreference.setUserPhoto(user_photo);
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
            }, params);


            req.addImageData("user_photo", new File(UserProfileImage));
            Log.e("qqqq", "" + UserProfileImage);
            NetworkOperationHelper.getInstance(UpdateProfileActivity.this).addToRequestQueue(req);


        } catch (UnsupportedEncodingException e) {
            pDialog.dismiss();
            e.printStackTrace();

        } catch (NullPointerException e) {
            pDialog.dismiss();
            e.printStackTrace();
        }

    }

    private void showCustomDialog(String s) {
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

    private void statusBarColor() {
        Window window = this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color

    }

    private void showCustomDialog1decline(String s, final String todo) {
        final AlertDialog alertDialog = new AlertDialog.Builder(UpdateProfileActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("" + s);

        alertDialog.setIcon(R.drawable.tick);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if (todo.equals("1")) {
                    Intent intent = new Intent(UpdateProfileActivity.this, HomeActivity.class);
                    intent.putExtra("firstName", firstName);
                    intent.putExtra("lastName", lastName);
                    intent.putExtra("email", userEmail);
                    intent.putExtra("phoneNumber", userPhone);
                    intent.putExtra("streetNumber", userFloor);//floor
                    intent.putExtra("streetName", userStreetName);
                    intent.putExtra("zipCode", userZipCode);
                    intent.putExtra("userCity", city);
                    intent.putExtra("userState", state);
                    intent.putExtra("BitmapImage", bitmap);

                    Log.e("update data", "" + intent);

                    startActivity(intent);
                    finish();
                } else {
                    alertDialog.dismiss();
                }
            }
        });
        alertDialog.show();
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK && null != data) {
//                Bitmap photo = (Bitmap) data.getExtras().get("data");
//                panpic.setImageBitmap(photo);

                if (imageviewSelected == "profilepic") {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    profile_image.setImageBitmap(photo);
                    Uri tempUri = getImageUri(getApplicationContext(), photo);
                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    File finalFile = new File(getRealPathFromURI(tempUri));
                    UserProfileImage = String.valueOf(finalFile);
                    Log.e("image", "" + finalFile + "     " + tempUri);

                }

            } else if (imageviewSelected == "profilepic") {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                UserProfileImage = cursor.getString(columnIndex);
                profile_image.setImageBitmap(decodeUri(selectedImage));

                cursor.close();

            }
        } catch (Exception e) {
            Log.d("expception:", "" + e.getMessage());
            Toast.makeText(this, "Something went wrong" + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        profile_image.setImageBitmap(thumbnail);
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


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
                } else {
                    Toast.makeText(UpdateProfileActivity.this, "GET_ACCOUNTS Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private void userLogin() {


        MyProgressDialog.show(this, R.string.please_wait);
        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<Model_UpdateProfileUpdate> userLoginCall = interfaceRetrofit.phpexpert_customer_profite_update(customerId, edtFirstName.getText().toString(), edtLastName.getText().toString(),
                edtEmail.getText().toString(), edtPhoneNumber.getText().toString(), edtFloor.getText().toString(), edtStreetName.getText().toString(), edtZipCode.getText().toString(),
                edtCity.getText().toString(), edtState.getText().toString(), myPref.getState(), myPref.getCity(), myPref.getLatitude(),
                myPref.getLongitude());
        userLoginCall.enqueue(new Callback<Model_UpdateProfileUpdate>() {
            @Override
            public void onResponse(Call<Model_UpdateProfileUpdate> call, retrofit2.Response<Model_UpdateProfileUpdate> response) {
                if (response.isSuccessful()) {
                    try {
                        if (response.body().getSuccess().equalsIgnoreCase("1")) {
                            firstName = response.body().getCustomerFirstName();
                            lastName = response.body().getCustomerLastName();
                            userEmail = response.body().getCustomerEmail();
                            userPhone = response.body().getCustomerPhone();
                            MyProgressDialog.dismiss();
                            showCustomDialog1decline(response.body().getSuccess_msg(), "1");
                        } else {
                            MyProgressDialog.dismiss();
                            showCustomDialog1decline(response.body().getSuccess_msg(), "2");
                        }
                    } catch (Exception e) {
                        MyProgressDialog.dismiss();
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<Model_UpdateProfileUpdate> call, Throwable t) {
                Toast.makeText(UpdateProfileActivity.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
                MyProgressDialog.dismiss();
            }
        });


    }

    private void getProfileData() {

        MyProgressDialog.show(this, R.string.please_wait);
        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<Model_CustomerProfile> userLoginCall = interfaceRetrofit.phpexpert_customer_profile_info(customerId);
        userLoginCall.enqueue(new Callback<Model_CustomerProfile>() {
            @Override
            public void onResponse(Call<Model_CustomerProfile> call, retrofit2.Response<Model_CustomerProfile> response) {
                if (response.isSuccessful()) {
                    edtFirstName.setText(response.body().getFirst_name());
                    edtLastName.setText(response.body().getLast_name());
                    edtEmail.setText(response.body().getUser_email());
                    edtPhoneNumber.setText(response.body().getUser_phone());
                    edtStreetName.setText(response.body().getUser_address());
                    edtZipCode.setText(response.body().getUser_postcode());

                }
                MyProgressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<Model_CustomerProfile> call, Throwable t) {
                MyProgressDialog.dismiss();
                Toast.makeText(UpdateProfileActivity.this, "" + t.toString(), Toast.LENGTH_SHORT).show();

            }
        });


    }


}
