package com.justfoodz.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.justfoodz.R;
import com.justfoodz.utils.AuthPreference;
import com.justfoodz.utils.UrlConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReferEarnActivity extends AppCompatActivity {
    TextView refercodemsg, refercode,joinfriends,earnpoints,txtlpoints;
    String REferCode, REFERMSG,REFERSENDMESSAGE,REFEREARNPOINTS,REFERALJOINFRIENDS, referral_earn_points,referral_join_friends ;
    AuthPreference authPreference;
    ImageView watsapp,facebook,share,twitter;
    ImageView iv_back;
    private ProgressDialog pDialog;
    public static ArrayList<String> aa_user_name, aa_user_email,aa_user_phone;
    RelativeLayout rrfrnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_earn);
        refercode = (TextView) findViewById(R.id.refercode);
        refercodemsg = (TextView) findViewById(R.id.refercodemsg);
        earnpoints=(TextView)findViewById(R.id.earnpoints);
        joinfriends=(TextView)findViewById(R.id.joinfriends);
        txtlpoints=(TextView)findViewById(R.id.txtlpoints);
        watsapp = (ImageView) findViewById(R.id.watsapp);
        facebook=(ImageView)findViewById(R.id.facebook);
        share=(ImageView)findViewById(R.id.share);
        twitter=(ImageView)findViewById(R.id.twitter);
        rrfrnd=(RelativeLayout) findViewById(R.id.rrfrnd);
        iv_back=findViewById(R.id.iv_back);

        authPreference = new AuthPreference(this);
        REferCode = authPreference.getReferal_code();
        REFERMSG = authPreference.getReferal_codemsg();
        REFERSENDMESSAGE=authPreference.getReferal_sharingmsg();
        REFERALJOINFRIENDS=authPreference.getReferalJoinFriends();
        REFEREARNPOINTS=authPreference.getREferalEarnPoints();
        refercode.setText(REferCode);
        refercodemsg.setText(REFERMSG);
        aa_user_name=new ArrayList<>();
        aa_user_email=new ArrayList<>();
        aa_user_phone=new ArrayList<>();

        getDetail();

        rrfrnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (referral_join_friends.equals("0")) {

                } else {
                    Intent i = new Intent(ReferEarnActivity.this,Referedfrnlist.class);
                    startActivity(i);
                }
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ReferEarnActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        watsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWhatsApp(view);
            }
        });
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareTwitter();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, REFERSENDMESSAGE);
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
                startActivity(Intent.createChooser(sharingIntent, "Share using"));

            }
        });
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, REFERSENDMESSAGE);
                 boolean facebookAppFound = false;
                List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
                for (ResolveInfo info : matches) {
                    if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana")) {
                        intent.setPackage(info.activityInfo.packageName);
                        facebookAppFound = true;
                        break;
                    }
                }

                if (!facebookAppFound) {
                    String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + REFERSENDMESSAGE;
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
                }

                startActivity(intent);
            }
        });


    }
    public void openWhatsApp(View view){
        PackageManager pm=getPackageManager();
        try {
            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");
              PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
             waIntent.setPackage("com.whatsapp");
             waIntent.putExtra(Intent.EXTRA_TEXT, REFERSENDMESSAGE);
            startActivity(Intent.createChooser(waIntent, "Share with"));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show();
        }catch(Exception e){
            e.printStackTrace();
        }

    }
    private void shareTwitter() {
        Intent tweetIntent = new Intent(Intent.ACTION_SEND);
        tweetIntent.putExtra(Intent.EXTRA_TEXT, REFERSENDMESSAGE);
        tweetIntent.setType("text/plain");

        PackageManager packManager = getPackageManager();
        List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);

        boolean resolved = false;
        for (ResolveInfo resolveInfo : resolvedInfoList) {
            if (resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")) {
                tweetIntent.setClassName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name);
                resolved = true;
                break;
            }
        }
        if (resolved) {
            startActivity(tweetIntent);
        } else {
            Intent i = new Intent();
            i.putExtra(Intent.EXTRA_TEXT, REFERSENDMESSAGE);
            i.setAction(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://twitter.com/intent/tweet?text=" + REFERSENDMESSAGE));
            startActivity(i);
            Toast.makeText(this, "Twitter app isn't found", Toast.LENGTH_LONG).show();
        }
    }

    public void getDetail() {

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        aa_user_name.clear();
        aa_user_email.clear();
        aa_user_phone.clear();

        StringRequest strReq1 = new StringRequest(Request.Method.POST, UrlConstants.customer_points, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("payresponse", "" + response);
                try {
                    pDialog.dismiss();
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("success");

                    if (success == 0) {
                         referral_earn_points = obj.optString("referral_earn_points");
                         referral_join_friends = obj.optString("referral_join_friends");
                        String Total_Points_Earned = obj.optString("Total_Points_Earned");
                        earnpoints.setText("You have earn "  + referral_earn_points+" points ");
                        joinfriends.setText("You have refered "+referral_join_friends+" friends ");
                        txtlpoints.setText("Total Points Earned "+Total_Points_Earned);
                        JSONObject friends = obj.getJSONObject("friends");
                        JSONArray referralfriendList = friends.getJSONArray("referralfriendList");
                        if (referralfriendList.length()>0){
                            for (int i =0;i<referralfriendList.length();i++)
                            {
                                JSONObject jj = referralfriendList.getJSONObject(i);
                                String user_name = jj.getString("user_name");
                                String user_email = jj.getString("user_email");
                                String user_phone = jj.getString("user_phone");
                                aa_user_name.add(user_name);
                                aa_user_email.add(user_email);
                                aa_user_phone.add(user_phone);
                            }
                        }
                    } else {
                        String success_msg = obj.optString("success_msg");
                        Toast.makeText(ReferEarnActivity.this, success_msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.getMessage();
                    e.printStackTrace();
                    Log.e("ex",""+e);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error",""+error);
                Toast.makeText(getApplicationContext(), "Please check your network connection", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("CustomerId", ""+authPreference.getCustomerId());
                Log.e("qwerty",""+params);
                return params;
            }
        };
        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        requestQueue1.add(strReq1);
    }
}