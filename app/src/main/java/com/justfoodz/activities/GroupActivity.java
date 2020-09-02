package com.justfoodz.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;

import android.os.Bundle;
import android.text.Html;
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
import com.justfoodz.models.BuyGiftModel;
import com.justfoodz.models.GroupModel;
import com.justfoodz.models.MemberModel;
import com.justfoodz.utils.AuthPreference;
import com.justfoodz.utils.ConnectionDetector;
import com.justfoodz.utils.Network;
import com.justfoodz.utils.UrlConstants;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GroupActivity extends AppCompatActivity {
    ImageView addgroupname,iv_back;
    Dialog Addgroupdialog, Addmemberdialog, memberdetaildialog, deletedialog, activedialog;
    RecyclerView grouprecycler;
    private ArrayList<GroupModel> groupList;

    ProgressDialog pDialog;
    String GROUPNAME, Customerid;
    AuthPreference authPreference;

    ImageView imageblank;
    TextView emptytxt;
    LinearLayout layout2;

    String GROUPID,GROUPMEMBERID;
    int statusindexPos,deleteindexpos,addindexpos;
    GroupModel groupModel;
    String groupStatus;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        addgroupname = findViewById(R.id.addgroupname);
        iv_back=findViewById(R.id.iv_back);
        emptytxt=findViewById(R.id.emptytxt);
        imageblank=findViewById(R.id.imageblank);
        layout2=findViewById(R.id.layout);
        grouprecycler = findViewById(R.id.grouprecycler);
        authPreference = new AuthPreference(this);
        Customerid = authPreference.getCustomerId();

        groupList = new ArrayList<>();


        HitURLforGroupList();


        addgroupname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddGroupDialog();
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(GroupActivity.this,MyAccountActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    public void showAddGroupDialog() {
//        groupModel = groupList.get(statusindexPos);
        Addgroupdialog = new Dialog(this);
        Addgroupdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Addgroupdialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Addgroupdialog.setContentView(R.layout.custom_groupname);
        Addgroupdialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        Addgroupdialog.getWindow().getAttributes().windowAnimations = R.style.diologIntertnet;
        Addgroupdialog.setCanceledOnTouchOutside(true);
        Button submit = (Button) Addgroupdialog.findViewById(R.id.submit);
        final EditText groupname = (EditText) Addgroupdialog.findViewById(R.id.groupname);
        Button cancel = (Button) Addgroupdialog.findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Addgroupdialog.dismiss();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (groupname.getText().toString().equals("")) {
                    groupname.setError("Enter Group Name");
                    groupname.requestFocus();
                }else {
                    if (!ConnectionDetector.networkStatus(getApplicationContext())) {
                        Addgroupdialog.show();
                    } else {
                        GROUPNAME = groupname.getText().toString();
                        HitUrlforAddGroup();
                        Addgroupdialog.dismiss();
                    }
                }
            }
        });
        Addgroupdialog.show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(GroupActivity.this,MyAccountActivity.class);
        startActivity(intent);
        finish();
    }

    public void HitUrlforDeletegroup() {
        pDialog = new ProgressDialog(GroupActivity.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.groupdelete, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ressponse", response);
                pDialog.dismiss();
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    String success = jsonObj.getString("success");
                    if (success.equals("0")) {
                        String success_msg = jsonObj.getString("success_msg");
                        showCustomDialog1decline(success_msg, "4");


                    } else {
                        String success_msg = jsonObj.getString("success_msg");
                        showCustomDialog1decline(success_msg, "2");
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
                 params.put("group_id", groupList.get(deleteindexpos).getGroup_id());
                return params;
            }

        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }

    public void HitUrlforAddGroup() {

        pDialog = new ProgressDialog(GroupActivity.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.AddGroup, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ressponse", response);
                pDialog.dismiss();
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    String success = jsonObj.getString("success");
                    if (success.equals("1")) {
                        String success_msg = jsonObj.getString("success_msg");
                        showCustomDialog1decline(success_msg, "1");


                    } else {
                        String success_msg = jsonObj.getString("success_msg");
                        showCustomDialog1decline(success_msg, "2");
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
                params.put("CustomerId", Customerid);
                params.put("customer_group_name", GROUPNAME);
                return params;
            }

        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }

    public void HitURLforGroupList() {
        pDialog = new ProgressDialog(GroupActivity.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.GroupListWdoutPayment, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ressponse", response);
                pDialog.dismiss();
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray groulist = jsonObj.getJSONArray("GroupListList");

                    for (int i = 0; i < groulist.length(); i++) {
                        JSONObject c = groulist.getJSONObject(i);
                        int error= c.getInt("error");
                        if (error==1){
                            String error_msg= c.getString("error_msg");
                            grouprecycler.setVisibility(View.GONE);
                            layout2.setVisibility(View.VISIBLE);
                            imageblank.setVisibility(View.VISIBLE);
                            emptytxt.setVisibility(View.VISIBLE);
                            emptytxt.setText(error_msg);

                        }else {
                            grouprecycler.setVisibility(View.VISIBLE);
                            layout2.setVisibility(View.GONE);
                            imageblank.setVisibility(View.GONE);
                            emptytxt.setVisibility(View.GONE);
                            String id = c.getString("id");
                            String group_id = c.getString("group_id");
                            String group_member_id = c.getString("group_member_id");
                            String status=c.getString("status");
                            String customer_group_name = c.getString("customer_group_name");
                            groupList.add(new GroupModel(id, customer_group_name,status,group_id,group_member_id));

                            GroupAdapter groupAdapter = new GroupAdapter(GroupActivity.this, groupList);
                            LinearLayoutManager horizontalLayoutManager2 = new LinearLayoutManager(GroupActivity.this, LinearLayoutManager.VERTICAL, false);
                            grouprecycler.setLayoutManager(horizontalLayoutManager2);
                            grouprecycler.setAdapter(groupAdapter);
                            groupAdapter.notifyDataSetChanged();
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
                pDialog.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to customer login
                Map<String, String> params = new HashMap<String, String>();
                params.put("CustomerId", Customerid);
                return params;
            }

        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }

    public void HitURLforGroupDeACtive() {


        pDialog = new ProgressDialog(GroupActivity.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.groupactive, new com.android.volley.Response.Listener<String>() {
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

                params.put("group_id", groupList.get(statusindexPos).getGroup_id());
                params.put("group_status", "1");
//                group_status = "1";

                return params;
            }

        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }

    public void HitURLforGroupACtive() {

        pDialog = new ProgressDialog(GroupActivity.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.groupactive, new com.android.volley.Response.Listener<String>() {
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

                params.put("group_id", groupList.get(statusindexPos).getGroup_id());
                params.put("group_status", "0");

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
                    groupList.clear();
                    HitURLforGroupList();
                    Addgroupdialog.dismiss();
                }   else  if (todo.equals("4")){
                    groupList.clear();
                    HitURLforGroupList();
                    activedialog.dismiss();
                }
                else {
                    alertDialog.dismiss();
                }


            }
        });
        alertDialog.show();

    }


    public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

        Context context;

        ArrayList<GroupModel> groupList;

        public GroupAdapter(Context c, ArrayList<GroupModel> groupList) {
            this.context = c;
            this.groupList = groupList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(R.layout.grouprecyclerlist, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            groupModel = groupList.get(position);
            holder.groupname.setText(groupList.get(position).getGroupname());

            String groupNameee = groupList.get(position).getGroupname();
            String groupiddd = groupList.get(position).getGroup_id();
            String catStatus = groupModel.getStatus();
            if (catStatus.equalsIgnoreCase("0")) {

                holder.activegroup.setVisibility(View.VISIBLE);
                holder.deactivegroup.setVisibility(View.GONE);
            } else {

                holder.activegroup.setVisibility(View.GONE);
                holder.deactivegroup.setVisibility(View.VISIBLE);
            }

            holder.Addnewmember.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(GroupActivity.this,GroupMemberActivity.class);
                    intent.putExtra("group_member_id",""+groupList.get(position).getGroup_member_id());
                    intent.putExtra("groupname",groupList.get(position).getGroupname());
                    startActivity(intent);
                    finish();


                }
            });
            holder.statuslayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Network.isNetworkCheck(context)) {
                        Toast.makeText(context, context.getResources().getString(R.string.internet), Toast.LENGTH_LONG).show();
                    } else {
                        groupModel = groupList.get(position);
                        statusindexPos = position;
                        groupStatus = groupModel.getStatus();
                        GROUPID = groupModel.getGroup_id();
//                    Toast.makeText(context, "" + statusindexPos, Toast.LENGTH_LONG).show();
//                    Toast.makeText(context, categryStatus, Toast.LENGTH_LONG).show();


                        if (groupStatus.equals("0")) {
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
                                    groupModel = groupList.get(position);
                                    statusindexPos = position;
                                    groupStatus = groupModel.getStatus();
                                    groupModel.setStatus("1");
                                    if (!Network.isNetworkCheck(context)) {
                                        Toast.makeText(context, context.getResources().getString(R.string.internet), Toast.LENGTH_LONG).show();
                                    } else {

//                                holder.switchonIv.setVisibility(View.INVISIBLE);
//                                holder.switchoffIv.setVisibility(View.VISIBLE);
                                        HitURLforGroupDeACtive();
                                    }
                                }
                            });
                        } else if (groupStatus.equals("1")) {
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
                                    groupModel = groupList.get(position);
                                    statusindexPos = position;
                                    groupStatus = groupModel.getStatus();
                                    groupModel.setStatus("0");
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

            holder.deletegroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Network.isNetworkCheck(context)) {
                        Toast.makeText(context, context.getResources().getString(R.string.internet), Toast.LENGTH_LONG).show();
                    } else {
                        groupModel = groupList.get(position);
                        deleteindexpos = position;
                        GROUPID = groupModel.getId();
                        Log.d("id", GROUPID);
                        activedialog = new Dialog(context);
                        activedialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        activedialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        activedialog.setContentView(R.layout.catdelete_dialog_layout);
                        activedialog.show();
                        Button cancelBtn = (Button) activedialog.findViewById(R.id.cancel);
                        Button okBtn = (Button) activedialog.findViewById(R.id.ok);
                        TextView txtdialogTv = (TextView) activedialog.findViewById(R.id.txtdialog);
                        txtdialogTv.setText("Are You Sure to Delete Group?");
                        cancelBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                activedialog.dismiss();
                            }
                        });
                        okBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (Network.isNetworkCheck(context)) {
                                    HitUrlforDeletegroup();
                                } else {
                                    Toast.makeText(context, context.getResources().getString(R.string.internet), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
                });
        }

        @Override
        public int getItemCount() {
            return groupList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView groupname;
            ImageView Addnewmember, deletegroup, activegroup, deactivegroup;
            LinearLayout statuslayout;


            public ViewHolder(View itemView) {
                super(itemView);
                groupname = (TextView) itemView.findViewById(R.id.groupname);
                Addnewmember = (ImageView) itemView.findViewById(R.id.Addnewmember);
                deletegroup = (ImageView) itemView.findViewById(R.id.deletegroup);
                activegroup = (ImageView) itemView.findViewById(R.id.groupactive);
                deactivegroup = (ImageView) itemView.findViewById(R.id.deactivegroup);
                statuslayout=(LinearLayout)itemView.findViewById(R.id.statuslayout);


                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position= getAdapterPosition();
                    }
                });





            }


        }

    }
}
