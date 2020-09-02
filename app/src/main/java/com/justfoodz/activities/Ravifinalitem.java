package com.justfoodz.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.justfoodz.Database.Database;

import com.justfoodz.R;
import com.justfoodz.models.SubList.ChildHeader;
import com.justfoodz.models.SubList.MenuHeader;
import com.justfoodz.utils.UrlConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Ravifinalitem extends AppCompatActivity {

    ProgressDialog progressDialog;
    RecyclerView rv_size;
    ImageView iv_backr;
    RequestQueue requestQueue;
    ArrayList<MenuHeader> menuHeaders;
    ArrayList<ChildHeader> childHeaders;
    LinearLayoutManager linearLayoutManager;
    Button btnAddr;
    Database database;
    SQLiteDatabase sqLiteDatabase;
    ArrayList<String> extra_item_list_id;
    ArrayList<String> extra_item_list_name;
    ArrayList<String> extra_item_list_price;
    String size_item ="";
    public static int cart_Item_number;
    String pound = SplashScreenActivity.currency_symbol;
    TextView txt_size;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ravifinalitem);
        initialization();
        statusBarColor();

        if (getIntent().getStringExtra("tocall").equals("extra"))
        {
            txt_size.setVisibility(View.GONE);
            getExtraMenuItemExtra();
        }
        else if (getIntent().getStringExtra("tocall").equals("size"))
        {
            txt_size.setVisibility(View.VISIBLE);
            txt_size.setText(getIntent().getStringExtra("itemname"));
            getSizeMenuItemExtra();
        }
        else if (getIntent().getStringExtra("tocall").equals("both"))
        {
            txt_size.setVisibility(View.VISIBLE);
            btnAddr.setVisibility(View.GONE);
            txt_size.setText(getIntent().getStringExtra("itemname"));
            getSizeMenuItemExtra();
        }
        iv_backr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnAddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qunt = 0;
                double price1 = 0.0;
                if (getIntent().getStringExtra("tocall").equals("extra"))
                {
                    double total = 0.0;
                    for (int i =0;i<extra_item_list_price.size();i++)
                    {
                        total = total+Double.parseDouble(extra_item_list_price.get(i));
                    }
                    total = total+Double.parseDouble(getIntent().getStringExtra("price"));
                    SQLiteDatabase db = database.getReadableDatabase();
                    Cursor cursor = db.rawQuery("SELECT * FROM item_table where extra_item_id='"+extra_item_list_id+"'", null);
                    if (cursor.moveToNext()) {
                        qunt = Integer.parseInt(cursor.getString(7));
                        price1 = Double.parseDouble(cursor.getString(6));
                        price1 = price1 / qunt;
                        price1 = (double) Math.round(price1 * 100) / 100;
                        qunt = qunt + 1;
                        price1 = price1 * qunt;

                        Log.e("databasevale", "" + cursor.getString(4));
                        database.update_extra_quantity(getIntent().getStringExtra("itemid"), "" + extra_item_list_id, price1, qunt);
                    }
                    else
                    {
                        database.InsertItem(getIntent().getStringExtra("itemid"),getIntent().getStringExtra("itemname"),"0","0",""+extra_item_list_id,""+extra_item_list_name,total,1);
                        cart_Item_number = cart_Item_number +1;
                        SubMenuActivity.tvCartItemCount.setText(""+cart_Item_number);
                    }
                    database.close();
                    finish();

                    Log.e("extra_id",""+extra_item_list_id);
                    Log.e("extra_item_list_name",""+extra_item_list_name);
                    Log.e("extra_item_list_price",""+extra_item_list_price);
                }
                else if (getIntent().getStringExtra("tocall").equals("size")||getIntent().getStringExtra("tocall").equals("both"))
                {
                    if (size_item.equals(""))
                    {
                        showCustomDialog1decline ("Please select any one");
                    }
                    else {
                        String currentString = size_item;
                        String[] separated = currentString.split("\\^");
                        double price = Double.parseDouble(separated[2]);
                        SQLiteDatabase db = database.getReadableDatabase();
                        Cursor cursor = db.rawQuery("SELECT * FROM item_table where item_id='"+getIntent().getStringExtra("itemid")+"' AND size_item_id='"+separated[0]+"'", null);
                        if (cursor.moveToNext()) {
                            qunt = Integer.parseInt(cursor.getString(7));
                            price1 = Double.parseDouble(cursor.getString(6));
                            price1 = price1 / qunt;
                            price1 = (double) Math.round(price1 * 100) / 100;
                            qunt = qunt + 1;
                            price1 = price1 * qunt;
                            database.update_item_size(getIntent().getStringExtra("itemid"), separated[0], qunt, price1);
                        }
                        else
                        {
                            if (getIntent().getStringExtra("itemname").contains("'"))
                            {
                                String aaa = getIntent().getStringExtra("itemname").replace("'","''" );
                                database.InsertItem(getIntent().getStringExtra("itemid"),aaa,separated[0],separated[1],"0","0",price,1);
                                cart_Item_number = cart_Item_number +1;
                                SubMenuActivity.tvCartItemCount.setText(""+cart_Item_number);
                            }
                            else {
                                database.InsertItem(getIntent().getStringExtra("itemid"),""+getIntent().getStringExtra("itemname"),separated[0],separated[1],"0","0",price,1);
                                cart_Item_number = cart_Item_number +1;
                                SubMenuActivity.tvCartItemCount.setText(""+cart_Item_number);
                            }
                            database.close();
                        }
                        finish();
                    }

                }

            }
        });
    }

    public void initialization()
    {
        requestQueue = Volley.newRequestQueue(this);
        rv_size = (RecyclerView) findViewById(R.id.rv_size);
        iv_backr = (ImageView) findViewById(R.id.iv_backr);
        btnAddr = (Button) findViewById(R.id.btnAddr);
        txt_size = (TextView) findViewById(R.id.txt_size);
        menuHeaders = new ArrayList<>();
        childHeaders = new ArrayList<>();
        database = new Database(Ravifinalitem.this);
        extra_item_list_id = new ArrayList<>();
        extra_item_list_name = new ArrayList<>();
        extra_item_list_price = new ArrayList<>();
    }

    public void getSizeMenuItemExtra()
    {
        childHeaders.clear();
        progressDialog = progressDialog.show(Ravifinalitem.this,"","",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants.EXTRA_MENU_ITEM_SIZE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.e("extraResponse",""+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("RestaurantMenItemsSize");
                    for (int i =0;i<jsonArray.length();i++)
                    {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            String idc = jsonObject2.getString("id");
                            String FoodItemID = jsonObject2.getString("FoodItemID");
                            String FoodItemSizeID = jsonObject2.getString("extraavailable");
                            String Food_addonsselection = "3";
                            String Food_Addons = jsonObject2.getString("RestaurantPizzaItemName");
                            String result = Html.fromHtml(Food_Addons).toString();
                            String Food_PriceAddons = jsonObject2.getString("RestaurantPizzaItemPrice");
                            childHeaders.add(new ChildHeader(idc,FoodItemID,FoodItemSizeID,Food_addonsselection,result,Food_PriceAddons));
                    }
                            ItemAdapter ii = new ItemAdapter(Ravifinalitem.this,childHeaders);
                            linearLayoutManager = new LinearLayoutManager(Ravifinalitem.this, LinearLayoutManager.VERTICAL, false);
                            rv_size.setLayoutManager(linearLayoutManager);
                            rv_size.setAdapter(ii);


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
                params.put("ItemID",""+getIntent().getStringExtra("itemid"));
                Log.e("",""+params);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void getExtraMenuItemExtra()
    {
        menuHeaders.clear();
        progressDialog = progressDialog.show(Ravifinalitem.this,"","",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants.EXTRA_MENU_ITEM_EXTRA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.e("extraResponse",""+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("ExtraToppingGroup");
                    for (int i =0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("id");
                        String ToppingGroupID = jsonObject1.getString("ToppingGroupID");
                        String Food_addonsgroup = jsonObject1.getString("Food_addonsgroup");
                        String Food_addonsgroup1 = Html.fromHtml(Food_addonsgroup).toString();
                        String success = jsonObject1.getString("success");
                        String success_msg = jsonObject1.getString("success_msg");
                        JSONArray jsonArray1 = jsonObject1.getJSONArray("subItemsRecord");
//                        childHeaders.clear();
                        menuHeaders.add(new MenuHeader(id,ToppingGroupID,Food_addonsgroup1,success,success_msg,jsonArray1));
                    }
                        SubmenuList submenuList = new SubmenuList(Ravifinalitem.this,menuHeaders);
                        linearLayoutManager = new LinearLayoutManager(Ravifinalitem.this, LinearLayoutManager.VERTICAL, false);
                        rv_size.setLayoutManager(linearLayoutManager);
                        rv_size.setAdapter(submenuList);


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
                Log.e("ppp",""+params);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public class SubmenuList extends RecyclerView.Adapter<SubmenuList.ViewHolder>
    {
        Context context;
        ArrayList<MenuHeader> mm;
        ArrayList<ChildHeader> itemList;

        public SubmenuList(Context context, ArrayList<MenuHeader> m)
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
            Log.e("list",""+mm.get(position).getJsonArray());
            for (int i = 0;i<mm.get(position).getJsonArray().length();i++)
            {
                try {
                    JSONObject jsonObject2  = mm.get(position).getJsonArray().getJSONObject(i);
                    String idc = jsonObject2.getString("id");
                    String FoodItemID = jsonObject2.getString("FoodItemID");
                    String FoodItemSizeID = jsonObject2.getString("FoodItemSizeID");
                    String Food_addonsselection = jsonObject2.getString("Food_addonsselection");
                    String Food_Addons = jsonObject2.getString("Food_Addons");
                    String result = Html.fromHtml(Food_Addons).toString();
                    String Food_PriceAddons = jsonObject2.getString("Food_PriceAddons");

                    itemList.add(new ChildHeader(idc,FoodItemID,FoodItemSizeID,Food_addonsselection,result,Food_PriceAddons));
                } catch (JSONException e) {
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
        ArrayList<ChildHeader> single;
        RadioButton globalradio=null;
         String idx="",namex ="",pricex="";

        public ItemAdapter(Context context, ArrayList<ChildHeader> m) {
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

                        String a = single.get(position).getId()+"^"+holder.txtitemname.getText().toString()+"^"+single.get(position).getFood_PriceAddons();
                        if(globalradio != null){
                            globalradio.setChecked(false);
                            if (getIntent().getStringExtra("tocall").equals("size"))
                            {
                                size_item = a;
                            }
                            else if (getIntent().getStringExtra("tocall").equals("extra"))
                            {
                                extra_item_list_id.remove(idx);
                                extra_item_list_id.add(single.get(position).getId());
                                extra_item_list_name.remove(namex);
                                extra_item_list_name.add(holder.txtitemname.getText().toString());
                                extra_item_list_price.remove(pricex);
                                extra_item_list_price.add(single.get(position).getFood_PriceAddons());
                                idx = single.get(position).getId();
                                namex = holder.txtitemname.getText().toString();
                                pricex = single.get(position).getFood_PriceAddons();
                            }
                        }
                        else {
                            size_item = a;
                            extra_item_list_id.add(single.get(position).getId());
                            extra_item_list_name.add(holder.txtitemname.getText().toString());
                            extra_item_list_price.add(single.get(position).getFood_PriceAddons());
                            idx = single.get(position).getId();
                            namex = holder.txtitemname.getText().toString();
                            pricex = single.get(position).getFood_PriceAddons();
                        }
//                       else globalradio.setChecked(true);
                        globalradio=checked_rb;
                        if (single.get(position).getFoodItemSizeID().equals("Yes"))
                        {
                           double total = 0.0;
                           total = Double.parseDouble(single.get(position).getFood_PriceAddons());
                          Intent i = new Intent(contextc,Bothitem.class);
                          i.putExtra("tocall",getIntent().getStringExtra("tocall"));
                          i.putExtra("itemid",getIntent().getStringExtra("itemid"));
                          i.putExtra("itemname",getIntent().getStringExtra("itemname"));
                          i.putExtra("price",""+total);
                          i.putExtra("sub_item_id",single.get(position).getId());
                          i.putExtra("sub_item_name",single.get(position).getFood_Addons());
                          startActivity(i);
                          finish();
                        }
                        else
                            {
                                btnAddr.setVisibility(View.VISIBLE);
                                size_item = a;
                            }
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
    private void statusBarColor() {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

    private void showCustomDialog1decline (String s)
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(Ravifinalitem.this).create();
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
//    public static String html2text(String html) {
//        return Jsoup.parse(html).text();
//    }
}