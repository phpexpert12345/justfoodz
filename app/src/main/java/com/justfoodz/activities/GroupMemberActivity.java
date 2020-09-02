package com.justfoodz.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.justfoodz.R;
import com.justfoodz.models.MemberModel;
import com.justfoodz.utils.AuthPreference;
import com.justfoodz.utils.ConnectionDetector;
import com.justfoodz.utils.Network;
import com.justfoodz.utils.UrlConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GroupMemberActivity extends AppCompatActivity {
    ProgressDialog pDialog;
    private ArrayList<MemberModel> memberList;
    RecyclerView memberrecycler;
    ImageView addmember,iv_back;
    Dialog memberdetaildialog,activedialog;
    String  MEMBERNAME, MEMBEREMAIL, MEMBERMOBILE, memberStatus,GROUPMEMID;
    AuthPreference authPreference;
    String Customerid;
    MemberModel memberModel;
    int activestatuspos;
    TextView groupnamee;
    String groupname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_memberdialog);
        groupnamee=findViewById(R.id.groupnamee);
        iv_back=findViewById(R.id.iv_back);
        authPreference = new AuthPreference(this);
        Customerid = authPreference.getCustomerId();
        memberrecycler=(RecyclerView)findViewById(R.id.memberrecycler);
        addmember=findViewById(R.id.addmember);
        memberList = new ArrayList<>();

        groupname= getIntent().getStringExtra("groupname");
        groupnamee.setText(groupname);

        HitUrlforgetMemberDetails();
        addmember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddMemberdetails();
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(GroupMemberActivity.this,GroupActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void AddMemberdetails() {
        memberdetaildialog = new Dialog(this);
        memberdetaildialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        memberdetaildialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        memberdetaildialog.setContentView(R.layout.addmemberdetails);
        memberdetaildialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        memberdetaildialog.getWindow().getAttributes().windowAnimations = R.style.diologIntertnet;
        memberdetaildialog.setCanceledOnTouchOutside(true);
        Button submit = (Button) memberdetaildialog.findViewById(R.id.submit);
        TextView groupnameqq = (TextView) memberdetaildialog.findViewById(R.id.groupname);
        final EditText membername = (EditText) memberdetaildialog.findViewById(R.id.membername);
        final EditText memberemail = (EditText) memberdetaildialog.findViewById(R.id.memberemail);
        final EditText membermobilenumber = (EditText) memberdetaildialog.findViewById(R.id.membermobilenumber);
        ImageView addmemberdetail = (ImageView) memberdetaildialog.findViewById(R.id.addmemberdetail);
        Button cancel = (Button) memberdetaildialog.findViewById(R.id.cancel);

        groupnameqq.setText(groupname);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memberdetaildialog.dismiss();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (membername.getText().toString().equals("")){
                    membername.setError("Enter Member Name");
                    membername.requestFocus();
                }else if (!isValidEmail(memberemail.getText().toString())){
                    memberemail.setError("Enter Valid Member Email Address");
                    memberemail.requestFocus();
                }else if (membermobilenumber.getText().toString().equals("")){
                    membermobilenumber.setError("Enter Member Number");
                    membermobilenumber.requestFocus();
                }else {
                    if (!ConnectionDetector.networkStatus(getApplicationContext())) {
                        memberdetaildialog.show();
                    } else {
                        MEMBERNAME = membername.getText().toString();
                        MEMBEREMAIL = memberemail.getText().toString();
                        MEMBERMOBILE = membermobilenumber.getText().toString();
                        HitUrlforAddMemberData();
                        memberdetaildialog.dismiss();
                    }
                }
            }
        });
        memberdetaildialog.show();
    }

    public void HitUrlforAddMemberData() {
        pDialog = new ProgressDialog(GroupMemberActivity.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.Addmember, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ressponse", response);
                pDialog.dismiss();
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    int success=jsonObj.getInt("success");
                    if (success==1){
                        String success_msg=jsonObj.getString("success_msg");
                        showCustomDialog1decline(success_msg,"1");
                    }else{
                        String success_msg=jsonObj.getString("success_msg");
                        showCustomDialog1decline(success_msg,"2");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                showCustom(getResources().getString(R.string.went_wrong));

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to customer login
                Map<String, String> params = new HashMap<String, String>();

                params.put("group_member_id",getIntent().getStringExtra("group_member_id"));
                params.put("CustomerId",Customerid);
                params.put("friend_name",MEMBERNAME);
                params.put("friend_email",MEMBEREMAIL);
                params.put("friend_mobile",MEMBERMOBILE);
                return params;
            }

        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }

    public void HitUrlforgetMemberDetails() {
        pDialog = new ProgressDialog(GroupMemberActivity.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        memberList.clear();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.MemberList, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ressponsegroup", response);
                pDialog.dismiss();
                try {
                    memberList.clear();
                    JSONObject jsonObj = new JSONObject(response);
                    String success =jsonObj.getString("success");
                    if (success.equals("1")){
                        memberrecycler.setVisibility(View.GONE);
                        String success_msg=jsonObj.getString("success_msg");
                        showCustom(success_msg);
                    }else {
                        memberrecycler.setVisibility(View.VISIBLE);
                        String group_member_id = jsonObj.getString("group_member_id");
                        String customerId = jsonObj.getString("CustomerId");
                        String id = jsonObj.getString("id");
                        String customer_group_name = jsonObj.getString("customer_group_name");
                        JSONObject gr= jsonObj.getJSONObject("group");
                        JSONArray memlist= gr.getJSONArray("memberlist");
                        for (int i = 0; i < memlist.length(); i++) {
                            JSONObject c = memlist.getJSONObject(i);
                            String group_id = c.getString("group_id");
                            String groupmemberid=c.getString("group_member_id");
                            String gid = c.getString("id");
                            String statusm = c.getString("status");
                            String friend_name = c.getString("friend_name");
                            String friend_email = c.getString("friend_email");
                            String friend_mobile = c.getString("friend_mobile");


                            memberList.add(new MemberModel(id, friend_name,friend_email,friend_mobile,statusm,groupmemberid));

                            MemberAdapter memberAdapter = new  MemberAdapter(GroupMemberActivity.this, memberList);
                            LinearLayoutManager horizontalLayoutManager2 = new LinearLayoutManager(GroupMemberActivity.this, LinearLayoutManager.VERTICAL, false);
                            memberrecycler.setLayoutManager(horizontalLayoutManager2);
                            memberrecycler.setAdapter(memberAdapter);

                        }
                    }




                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                showCustom(getResources().getString(R.string.went_wrong));

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to customer login
                Map<String, String> params = new HashMap<String, String>();
                params.put("group_member_id",getIntent().getStringExtra("group_member_id"));
                Log.e("groupparam",""+params);
                return params;
            }

        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }

    private void showCustom(String s) {
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

    private void showCustomDialog1decline(String s, final String todo) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("" + s);

        alertDialog.setIcon(R.drawable.tick);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if (todo.equals("1")) {
                    memberList.clear();
                    HitUrlforgetMemberDetails();
                    memberdetaildialog.dismiss();

                }else if (todo.equals("4")){
                    memberList.clear();
                    HitUrlforgetMemberDetails();
                    activedialog.dismiss();
                }
                else {
                    alertDialog.dismiss();
                }
            }
        });
        alertDialog.show();

    }

    private void showCustomdelete(String s, final String b) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("" + s);

        alertDialog.setIcon(R.drawable.tick);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if (b.equals("1")) {
                    HitUrlforgetMemberDetails();
                    alertDialog.dismiss();
                }else {
                    alertDialog.dismiss();
                }
            }
        });
        alertDialog.show();

    }

    public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {

        Context context;

        ArrayList<MemberModel> memberList;

        public MemberAdapter(Context c, ArrayList<MemberModel> memberList) {
            this.context = c;
            this.memberList = memberList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(R.layout.memberlistrecycler, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            memberModel = memberList.get(position);
            holder.username.setText(memberList.get(position).getFriend_name());
            holder.email.setText(memberList.get(position).getFriend_email());
            holder.mobile.setText(memberList.get(position).getFriend_mobile());

            String memStatus = memberModel.getStatusM();
            if (memStatus.equalsIgnoreCase("0")) {

                holder.activemember.setVisibility(View.VISIBLE);
                holder.deactivemember.setVisibility(View.GONE);
            } else {

                holder.activemember.setVisibility(View.GONE);
                holder.deactivemember.setVisibility(View.VISIBLE);
            }

            holder.statuslayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Network.isNetworkCheck(context)) {
                        Toast.makeText(context, context.getResources().getString(R.string.internet), Toast.LENGTH_LONG).show();
                    } else {
                        memberModel = memberList.get(position);
                        activestatuspos = position;
                        memberStatus = memberModel.getStatusM();
                        GROUPMEMID = memberModel.getGroupmemberid();
//                    Toast.makeText(context, "" + statusindexPos, Toast.LENGTH_LONG).show();
//                    Toast.makeText(context, categryStatus, Toast.LENGTH_LONG).show();


                        if (memberStatus.equals("0")) {
                            activedialog = new Dialog(context);
                            activedialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
                            activedialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            activedialog.setContentView(R.layout.catdelete_dialog_layout);
                            activedialog.show();
                            Button cancelBtn = (Button) activedialog.findViewById(R.id.cancel);
                            Button okBtn = (Button) activedialog.findViewById(R.id.ok);
                            TextView txtDialogTv = (TextView) activedialog.findViewById(R.id.txtdialog);
                            txtDialogTv.setText("Are You Sure to Deactivate?");
                            cancelBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    activedialog.dismiss();
                                }
                            });
                            okBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //deactivateVouture();
                                    memberModel = memberList.get(position);
                                    activestatuspos = position;
                                    memberStatus = memberModel.getStatusM();
                                    memberModel.setStatusM("1");
                                    if (!Network.isNetworkCheck(context)) {
                                        Toast.makeText(context, context.getResources().getString(R.string.internet), Toast.LENGTH_LONG).show();
                                    } else {

//                                holder.switchonIv.setVisibility(View.INVISIBLE);
//                                holder.switchoffIv.setVisibility(View.VISIBLE);
                                        HitURLforGroupDeACtive();
                                    }
                                }
                            });
                        } else if (memberStatus.equals("1")) {
                            activedialog = new Dialog(context);

                            activedialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
                            activedialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            activedialog.setContentView(R.layout.catdelete_dialog_layout);
                            activedialog.show();
                            Button cancelBtn = (Button) activedialog.findViewById(R.id.cancel);
                            Button okBtn = (Button) activedialog.findViewById(R.id.ok);
                            TextView txtDialogTv = (TextView) activedialog.findViewById(R.id.txtdialog);
                            txtDialogTv.setText("Are You Sure To Activate?");
                            cancelBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    activedialog.dismiss();
                                }
                            });
                            okBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //activateVouture();
                                    memberModel = memberList.get(position);
                                    activestatuspos = position;
                                    memberStatus = memberModel.getStatusM();
                                    memberModel.setStatusM("0");
                                    if (!Network.isNetworkCheck(context)) {
                                        Toast.makeText(context, context.getResources().getString(R.string.internet), Toast.LENGTH_LONG).show();
                                    } else {
                                        HitURLforGroupACtive();
                                    }
                                }
                            });


                        }
                    }
                }
            });

            holder.imgdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HitURLforGroupDelete(memberList.get(position).groupmemberid);
                }
            });
        }
        public void HitURLforGroupDeACtive() {


            pDialog = new ProgressDialog(GroupMemberActivity.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(true);
            pDialog.show();
            StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.memberactive, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("ressponse", response);
                    pDialog.dismiss();
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        int success = jsonObj.getInt("success");
                        if (success == 1) {
                            String success_msg = jsonObj.getString("success_msg");
                            showCustomDialog1decline(success_msg,"4");


                        } else {
                            String success_msg = jsonObj.getString("success_msg");
                            showCustomDialog1decline(success_msg,"2");
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    showCustom(getResources().getString(R.string.went_wrong));

                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting params to customer login
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("group_member_id",memberList.get(activestatuspos).getGroupmemberid());
                    params.put("group_member_status", "1");
//                group_status = "1";

                    return params;
                }

            };
            // Adding request to request queue
            RequestQueue requestQueue = Volley.newRequestQueue(GroupMemberActivity.this);
            requestQueue.add(strReq);
        }

        public void HitURLforGroupACtive() {

            pDialog = new ProgressDialog(GroupMemberActivity.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(true);
            pDialog.show();
            StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.memberactive, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("ressponse", response);
                    pDialog.dismiss();
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        int success = jsonObj.getInt("success");
                        if (success == 1) {

                            String success_msg = jsonObj.getString("success_msg");
                            showCustomDialog1decline(success_msg,"4");


                        } else {
                            String success_msg = jsonObj.getString("success_msg");
                            showCustomDialog1decline(success_msg,"2");
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    showCustom(getResources().getString(R.string.went_wrong));

                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting params to customer login
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("group_member_id",memberList.get(activestatuspos).getGroupmemberid());
                    params.put("group_member_status", "0");

                    return params;
                }

            };
            // Adding request to request queue
            RequestQueue requestQueue = Volley.newRequestQueue(GroupMemberActivity.this);
            requestQueue.add(strReq);
        }


        public void HitURLforGroupDelete(final String a) {

            pDialog = new ProgressDialog(GroupMemberActivity.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(true);
            pDialog.show();
            StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.group_member_delete, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("ressponse", response);
                    pDialog.dismiss();
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        String success = jsonObj.getString("success");
                        String success_msg = jsonObj.getString("success_msg");
                        if (success.equals("0")) {
                            showCustomdelete(success_msg,"1");
                        } else {
                            showCustomdelete(success_msg,"2");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    showCustom(getResources().getString(R.string.went_wrong));

                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting params to customer login
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("group_member_id",a);
                   Log.e("papapa",""+params);
                    return params;
                }
            };
            // Adding request to request queue
            RequestQueue requestQueue = Volley.newRequestQueue(GroupMemberActivity.this);
            requestQueue.add(strReq);
        }

        @Override
        public int getItemCount() {
            return memberList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView username, email, mobile;
            ImageView activemember,deactivemember,imgdelete;
            LinearLayout statuslayout;

            public ViewHolder(View itemView) {
                super(itemView);
                username = (TextView) itemView.findViewById(R.id.username);
                email = (TextView) itemView.findViewById(R.id.email);
                mobile = (TextView) itemView.findViewById(R.id.mobile);
                 statuslayout=(LinearLayout)itemView.findViewById(R.id.statuslayout);
                activemember=(ImageView)itemView.findViewById(R.id.activemember);
                deactivemember=(ImageView)itemView.findViewById(R.id.deactivemember);
                imgdelete=(ImageView)itemView.findViewById(R.id.imgdelete);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(GroupMemberActivity.this,GroupActivity.class);
        startActivity(intent);
        finish();
    }
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}