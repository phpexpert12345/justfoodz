package com.justfoodz.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.justfoodz.Database.Database;

import com.justfoodz.MyProgressDialog;
import com.justfoodz.R;
import com.justfoodz.models.Citymodel;
import com.justfoodz.models.SubList.ChildHeader;
import com.justfoodz.models.SubList.MenuHeader;
import com.justfoodz.retrofit.InterUserdata;
import com.justfoodz.retrofit.RetrofitAdapter;
import com.justfoodz.utils.UrlConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class Bothitem extends AppCompatActivity {
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Bothitem.this, Ravifinalitem.class);
        intent.putExtra("tocall","both");
        intent.putExtra("itemid",getIntent().getStringExtra("itemid"));
        intent.putExtra("itemname",getIntent().getStringExtra("itemname"));
        startActivity(intent);
        finish();
    }

    ProgressDialog progressDialog;
    RecyclerView rc_both;
    ImageView backboth;
    RequestQueue requestQueue;
    ArrayList<Model_ExtraToppingGroup> menuHeaders;
    ArrayList<ChildHeader> childHeaders;
    LinearLayoutManager linearLayoutManager;
    Button addboth;
    Database database;
    SQLiteDatabase sqLiteDatabase;
    public static String re_id,it_id,name, price,quant;
    ArrayList<String> extra_item_list_id;
    ArrayList<String> extra_item_list_name;
    ArrayList<String> extra_item_list_price;
    String pound = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bothitem);
        initialization();
        getExtraMenuItemExtra();
        pound = SplashScreenActivity.currency_symbol;
        backboth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Bothitem.this, Ravifinalitem.class);
                intent.putExtra("tocall","both");
                intent.putExtra("itemid",getIntent().getStringExtra("itemid"));
                intent.putExtra("itemname",getIntent().getStringExtra("itemname"));
                startActivity(intent);
                finish();
            }
        });
        addboth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int qunt = 0;
                double price1 = 0.0;
                double total = 0.0;
                for (int i =0;i<extra_item_list_price.size();i++)
                {
                    total = total+Double.parseDouble(extra_item_list_price.get(i));
                }
                total = total+Double.parseDouble(getIntent().getStringExtra("price"));
                SQLiteDatabase db = database.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM item_table where  size_item_id='"+getIntent().getStringExtra("sub_item_id")+"' AND extra_item_id='"+extra_item_list_id+"'", null);
                if (cursor.moveToNext()) {

                    // database.update_item_all(getIntent().getStringExtra("itemid"),getIntent().getStringExtra("sub_item_id"),getIntent().getStringExtra("sub_item_name"),""+extra_item_list_id,""+extra_item_list_name,total,1);
                    qunt = Integer.parseInt(cursor.getString(7));
                    price1 = Double.parseDouble(cursor.getString(6));
                    price1 = price1 / qunt;
                    price1 = (double) Math.round(price1 * 100) / 100;
                    qunt = qunt + 1;
                    price1 = price1 * qunt;

                    Log.e("databasevale", "" + cursor.getString(4));
                    database.update_both_quantity(getIntent().getStringExtra("itemid"), ""+getIntent().getStringExtra("sub_item_id"),"" + extra_item_list_id, price1, qunt);
                }
                else
                {
                    if (getIntent().getStringExtra("itemname").contains("'"))
                    {
                        String aaa = getIntent().getStringExtra("itemname").replace("'","''" );
                        database.InsertItem(getIntent().getStringExtra("itemid"),""+aaa,getIntent().getStringExtra("sub_item_id"),getIntent().getStringExtra("sub_item_name"),""+extra_item_list_id,""+extra_item_list_name,total,1);
                        Ravifinalitem.cart_Item_number = Ravifinalitem.cart_Item_number +1;
                    }
                    else {
                        database.InsertItem(getIntent().getStringExtra("itemid"),getIntent().getStringExtra("itemname"),getIntent().getStringExtra("sub_item_id"),getIntent().getStringExtra("sub_item_name"),""+extra_item_list_id,""+extra_item_list_name,total,1);
                        Ravifinalitem.cart_Item_number = Ravifinalitem.cart_Item_number +1;
                    }
                }
                finish();
            }
        });

    }
    public void initialization()
    {
        requestQueue = Volley.newRequestQueue(this);
        rc_both = (RecyclerView) findViewById(R.id.rc_both);
        backboth = (ImageView) findViewById(R.id.backboth);
        addboth = (Button) findViewById(R.id.addboth);
        menuHeaders = new ArrayList<>();
        childHeaders = new ArrayList<>();
        database = new Database(Bothitem.this);
        extra_item_list_id = new ArrayList<>();
        extra_item_list_name = new ArrayList<>();
        extra_item_list_price = new ArrayList<>();
    }
    public void getExtraMenuItemExtra()
    {
        menuHeaders.clear();
        menuHeaders=new ArrayList<>();
        MyProgressDialog.show(this, R.string.please_wait);
        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<Model_ExtraToppingGroupList> userLoginCall = interfaceRetrofit.phpexpert_restaurantMenuItemExtra(getIntent().getStringExtra("itemid"),getIntent().getStringExtra("sub_item_id"));
        userLoginCall.enqueue(new Callback<Model_ExtraToppingGroupList>() {
            @Override
            public void onResponse(Call<Model_ExtraToppingGroupList> call, retrofit2.Response<Model_ExtraToppingGroupList> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().getExtraToppingGroup().size(); i++) {
                        Model_ExtraToppingGroup model_extraToppingGroup = response.body().getExtraToppingGroup().get(i);

                        menuHeaders.add(model_extraToppingGroup);
                    }
                    if (menuHeaders.size() > 0) {
                        SubmenuList submenuList = new SubmenuList(Bothitem.this,menuHeaders);
                        linearLayoutManager = new LinearLayoutManager(Bothitem.this, LinearLayoutManager.VERTICAL, false);
                        rc_both.setLayoutManager(linearLayoutManager);
                        rc_both.setAdapter(submenuList);
                    }
                }
                MyProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Model_ExtraToppingGroupList> call, Throwable t) {
                Toast.makeText(Bothitem.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
                MyProgressDialog.dismiss();
            }
        });

      /*  StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants.EXTRA_MENU_ITEM_EXTRA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.e("bothextraResponse",""+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("ExtraToppingGroup");
                        for (int i =0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String id = jsonObject1.getString("id");
                            String ToppingGroupID = jsonObject1.getString("ToppingGroupID");
                            String Food_addonsgroup = jsonObject1.getString("Food_addonsgroup");
                            String success = jsonObject1.getString("success");
                            String success_msg = jsonObject1.getString("success_msg");
                            JSONArray jsonArray1 = jsonObject1.getJSONArray("subItemsRecord");
//                          childHeaders.clear();
                            menuHeaders.add(new MenuHeader(id,ToppingGroupID,Food_addonsgroup,success,success_msg,jsonArray1));
                        }
                        SubmenuList submenuList = new SubmenuList(Bothitem.this,menuHeaders);
                        linearLayoutManager = new LinearLayoutManager(Bothitem.this, LinearLayoutManager.VERTICAL, false);
                        rc_both.setLayoutManager(linearLayoutManager);
                        rc_both.setAdapter(submenuList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("extraError",""+error);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("ItemID",getIntent().getStringExtra("itemid"));
                params.put("SizeDI",getIntent().getStringExtra("sub_item_id"));
                Log.e("ppp",""+params);
                return params;
            }
        };
        requestQueue.add(stringRequest);*/
    }

    public class SubmenuList extends RecyclerView.Adapter<SubmenuList.ViewHolder>
    {
        Context context;
        ArrayList<Model_ExtraToppingGroup> mm;
        ArrayList<Model_subItemsRecord> itemList;

        public SubmenuList(Context context, ArrayList<Model_ExtraToppingGroup> m)
        {
            this.context = context;
            this.mm = m;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater;
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(R.layout.finalsubitemlist,parent,false);
            itemList = new ArrayList<>();
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            holder.txtmain.setText(mm.get(position).getFood_addonsgroup());
            itemList.clear();
            Log.e("list",""+mm.get(position).getSubItemsRecord());
            for (int i = 0;i<mm.get(position).getSubItemsRecord().size();i++)
            {
                try {
                    Model_subItemsRecord model_subItemsRecord = mm.get(position).getSubItemsRecord().get(i);
                 /*   JSONObject jsonObject2  = mm.get(position).getJsonArray().getJSONObject(i);
                    String idc = jsonObject2.getString("id");
                    String FoodItemID = jsonObject2.getString("FoodItemID");
                    String FoodItemSizeID = jsonObject2.getString("FoodItemSizeID");
                    String Food_addonsselection = jsonObject2.getString("Food_addonsselection");
                    String Food_Addons = jsonObject2.getString("Food_Addons");

                    String result = Html.fromHtml(Food_Addons).toString();
                    String Food_PriceAddons = jsonObject2.getString("Food_PriceAddons");*/

                    itemList.add(model_subItemsRecord);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Log.e("cc",""+itemList);
            ItemAdapter ii = new ItemAdapter(context,itemList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            holder.rcall.setLayoutManager(mLayoutManager);
            holder.rcall.setAdapter(ii);
        }

        @Override
        public int getItemCount() {
            return mm.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtmain;
            RecyclerView rcall;
            public ViewHolder(View itemView) {
                super(itemView);
                txtmain = (TextView) itemView.findViewById(R.id.txtmain);
                rcall = (RecyclerView) itemView.findViewById(R.id.rcall);
            }
        }
    }

    public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {
        Context contextc;
        ArrayList<Model_subItemsRecord> single;
        RadioButton globalradio=null;
        String idx="",namex ="",pricex="";

        public ItemAdapter(Context context, ArrayList<Model_subItemsRecord> m) {
            this.contextc = context;
            this.single = m;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater;
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View vv = layoutInflater.inflate(R.layout.menu_item,parent,false);
            return new MyViewHolder(vv);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
            Log.e("chilList",""+single.get(position).getFood_addonsselection());

            holder.txtitemname.setText(single.get(position).getFood_Addons());
            holder.txtprice.setText(pound.concat(single.get(position).getFood_PriceAddons()));
            if (single.get(position).getFood_addonsselection().equals("3"))
            {
                holder.rbitem.setVisibility(View.VISIBLE);
                holder.cbitem.setVisibility(View.GONE);
                holder.rbitem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            RadioButton checked_rb = (RadioButton) view;
                            if(globalradio != null){
                                globalradio.setChecked(false);
                                extra_item_list_id.remove(idx);
                                extra_item_list_id.add(single.get(position).getId());
                                extra_item_list_name.remove(namex);
                                extra_item_list_name.add(holder.txtitemname.getText().toString());
                                extra_item_list_price.remove(pricex);
                                extra_item_list_price.add(single.get(position).getFood_PriceAddons());
                                idx = single.get(position).getId();
                                namex = holder.txtitemname.getText().toString();
                                pricex = single.get(position).getFood_PriceAddons();
                            }else {
                                    extra_item_list_id.add(single.get(position).getId());
                                    extra_item_list_name.add(holder.txtitemname.getText().toString());
                                    extra_item_list_price.add(single.get(position).getFood_PriceAddons());
                                    idx = single.get(position).getId();
                                    namex = holder.txtitemname.getText().toString();
                                    pricex = single.get(position).getFood_PriceAddons();
                            }
                            globalradio=checked_rb;
                    }
                });
            }
            else {
                holder.rbitem.setVisibility(View.GONE);
                holder.cbitem.setVisibility(View.VISIBLE);
            }
            holder.cbitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String a = single.get(position).getId()+"^"+holder.txtitemname.getText().toString()+"^"+single.get(position).getFood_PriceAddons();
                    if(holder.cbitem.isChecked()){
                        extra_item_list_id.add(single.get(position).getId());
                        extra_item_list_name.add(holder.txtitemname.getText().toString());
                        extra_item_list_price.add(single.get(position).getFood_PriceAddons());
                    }
                    else {
                        extra_item_list_id.remove(single.get(position).getId());
                        extra_item_list_name.remove(holder.txtitemname.getText().toString());
                        extra_item_list_price.remove(single.get(position).getFood_PriceAddons());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            Log.e("size",""+single.size());
            return single.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView txtitemname, txtprice;
            CheckBox cbitem;
            RadioButton rbitem;
            public MyViewHolder(View itemView) {
                super(itemView);
                txtitemname = (TextView) itemView.findViewById(R.id.txtitemname);
                txtprice = (TextView) itemView.findViewById(R.id.txtprice);
                cbitem = (CheckBox) itemView.findViewById(R.id.cbitem);
                rbitem = (RadioButton) itemView.findViewById(R.id.rbitem);
            }
        }
    }
}