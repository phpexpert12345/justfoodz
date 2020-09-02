package com.justfoodz.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.justfoodz.MyProgressDialog;
import com.justfoodz.R;
import com.justfoodz.adapters.ExtraAdapter;
import com.justfoodz.adapters.ExpandableListAdapter;

import com.justfoodz.adapters.MenuSizeAdapter;
import com.justfoodz.interfaces.MenuInterface;
import com.justfoodz.models.ExtrasModel;
import com.justfoodz.models.MenuSizeModel;
import com.justfoodz.models.SubMenuModel;
import com.justfoodz.retrofit.InterUserdata;
import com.justfoodz.retrofit.Model_location;
import com.justfoodz.retrofit.RetrofitAdapter;
import com.justfoodz.utils.AuthPreference;
import com.justfoodz.utils.Network;
import com.justfoodz.utils.UrlConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class ExtraItemActivity extends AppCompatActivity implements MenuInterface, View.OnClickListener {
    private RecyclerView rvExtraItem;
    private Button btnAdd;
    private ImageView ivBack;
    private ExtraAdapter extraAdapter;
    private MenuSizeAdapter menuSizeAdapter;
    private ProgressDialog pDialog;
    private AuthPreference authPreference;
    public ArrayList<ExtrasModel> extrasModelArrayList = new ArrayList<ExtrasModel>();
    public ArrayList<ExtrasModel> extrasModelArrayLists = new ArrayList<ExtrasModel>();
    private ArrayList<SubMenuModel> menuSizeModelArrayLists = new ArrayList<SubMenuModel>();
    public String restaurantPizzaItemName = "", restaurantPizzaItemPrice = "", extraAvailable = "", sizeAvailable = "", resId = "", restaurantName, restaurantAddress, categoryName;
    public ArrayList<MenuSizeModel> menuSizeModelArrayList = new ArrayList<MenuSizeModel>();
    public ArrayList<MenuSizeModel> menuSizeModelArrayListss = new ArrayList<MenuSizeModel>();
    int subMenuItemId, restaurantCategoryId, subMenuId;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_item);                       //deep
        sharedPreferences = getSharedPreferences("HotelName",MODE_PRIVATE);
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        initialization();
        initializedListener();
        statusBarColor();

    }

    private void initialization() {
        ivBack = findViewById(R.id.iv_back);
        btnAdd = findViewById(R.id.btnAdd);
        rvExtraItem = findViewById(R.id.rv_extra_item);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(ExtraItemActivity.this, LinearLayoutManager.VERTICAL, false);
        rvExtraItem.setLayoutManager(mLayoutManager);
        rvExtraItem.setItemAnimator(new DefaultItemAnimator());
        authPreference = new AuthPreference(this);
        expListView = (ExpandableListView) findViewById(R.id.lvExp);


        try {
            extraAvailable = getIntent().getStringExtra("extraAvailable");
            sizeAvailable = getIntent().getStringExtra("sizeAvailable");
            subMenuItemId = getIntent().getIntExtra("subMenuItemId", 1);
            resId = getIntent().getStringExtra("id");// resId
            restaurantCategoryId = getIntent().getIntExtra("restaurantCategoryId", 1);//Main Menu Id
            restaurantName = getIntent().getStringExtra("restaurantName");
            restaurantAddress = getIntent().getStringExtra("restaurantAddress");
            categoryName = getIntent().getStringExtra("categoryName");
            subMenuId = getIntent().getIntExtra("subMenuId", 1);
            restaurantPizzaItemPrice = getIntent().getStringExtra("getRestaurantPizzaItemPrice");
            restaurantPizzaItemName = getIntent().getStringExtra("getRestaurantPizzaItemName");


        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        if (sizeAvailable.equalsIgnoreCase("YES") && extraAvailable.equalsIgnoreCase("No")) {
            if (Network.isNetworkCheck(this)) {
                getExtraMenuItemSize();

            } else {
                Toast.makeText(this, getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();

            }

        } else if (sizeAvailable.equalsIgnoreCase("No") && extraAvailable.equalsIgnoreCase("YES")) {
            if (Network.isNetworkCheck(this)) {

                getExtraMenuItemExtra();

            } else {
                Toast.makeText(this, getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();

            }
        } else if (sizeAvailable.equalsIgnoreCase("YES") && (extraAvailable.equalsIgnoreCase("YES"))) {


        }
    }

    private void initializedListener() {
        ivBack.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btnAdd:
                countExtra();
                break;
            default:
        }
    }

    public void getExtraMenuItemSize() {

        menuSizeModelArrayList=new ArrayList<>();


        MyProgressDialog.show(this, R.string.please_wait);
        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<Model_RestaurantMenItemsSize> userLoginCall = interfaceRetrofit.phpexpert_restaurantMenuItemSize(String.valueOf(subMenuItemId));
        userLoginCall.enqueue(new Callback<Model_RestaurantMenItemsSize>() {
            @Override
            public void onResponse(Call<Model_RestaurantMenItemsSize> call, retrofit2.Response<Model_RestaurantMenItemsSize> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().getRestaurantMenItemsSize().size(); i++) {

                        MenuSizeModel menuSizeModel = response.body().getRestaurantMenItemsSize().get(i);
                       /* menuSizeModel.setId(sizeId);
                        menuSizeModel.setTrue(false);
                        menuSizeModel.setMenu("menu");
                        menuSizeModel.setFoodItemName(foodItemName);
                        menuSizeModel.setRestaurantPizzaItemName(restaurantPizzaItemName);
                        menuSizeModel.setRestaurantPizzaItemOldPrice(restaurantPizzaItemOldPrice);
                        menuSizeModel.setRestaurantPizzaItemPrice(restaurantPizzaItemPrice);
                        menuSizeModel.setQuantity(1);*/
                        menuSizeModelArrayList.add(menuSizeModel);
                    }

                }
                MyProgressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<Model_RestaurantMenItemsSize> call, Throwable t) {
                Toast.makeText(ExtraItemActivity.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
                MyProgressDialog.dismiss();
            }
        });



        /*


        pDialog = new ProgressDialog(ExtraItemActivity.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.EXTRA_MENU_ITEM_SIZE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                try {
                    Log.e("extraitem",""+response);
                    JSONObject jObj = new JSONObject(response);
                    JSONArray jsonArray = jObj.getJSONArray("RestaurantMenItemsSize");
                    String error = jObj.optString("error");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int sizeId = jsonObject.optInt("id");
                        String foodItemName = jsonObject.optString("FoodItemName");
                        String restaurantPizzaItemName = jsonObject.optString("RestaurantPizzaItemName");
                        String restaurantPizzaItemOldPrice = jsonObject.optString("RestaurantPizzaItemOldPrice");
                        String restaurantPizzaItemPrice = jsonObject.optString("RestaurantPizzaItemPrice");
                        MenuSizeModel menuSizeModel = new MenuSizeModel();
                        menuSizeModel.setId(sizeId);
                        menuSizeModel.setTrue(false);
                        menuSizeModel.setMenu("menu");
                        menuSizeModel.setFoodItemName(foodItemName);
                        menuSizeModel.setRestaurantPizzaItemName(restaurantPizzaItemName);
                        menuSizeModel.setRestaurantPizzaItemOldPrice(restaurantPizzaItemOldPrice);
                        menuSizeModel.setRestaurantPizzaItemPrice(restaurantPizzaItemPrice);
                        menuSizeModel.setQuantity(1);
                        menuSizeModelArrayList.add(menuSizeModel);
                    }
                    setAdaptor();


                } catch (JSONException e) {
                    Log.d("exception", "e");
                    e.getMessage();
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ExtraItemActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ItemID", subMenuItemId + "");
                Log.e("itemid",""+params);
                return params;
            }

        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(ExtraItemActivity.this);
        requestQueue.add(strReq);*/
    }


    public void getExtraMenuItemExtra() {
        pDialog = new ProgressDialog(ExtraItemActivity.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.EXTRA_MENU_ITEM_EXTRA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray ExtraToppingGroupArray = jObj.getJSONArray("ExtraToppingGroup");
                    for (int i = 0; i < ExtraToppingGroupArray.length(); i++) {

                        JSONObject jobj1 = ExtraToppingGroupArray.getJSONObject(i);
                        String foodAddonsGroup = jobj1.optString("Food_addonsgroup");
                        listDataHeader.add(foodAddonsGroup);
                        List<String> comingSoon = new ArrayList<String>();
                        int success = jobj1.optInt("success");
                        if (success == 0) {
                            comingSoon.clear();
                            JSONArray jsonArray = jobj1.getJSONArray("subItemsRecord");

                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject object = jsonArray.getJSONObject(j);
                                int extraId = object.optInt("id");
                                String foodAddonSelection = object.optString("Food_addonsselection");
                                String foodAddons = object.optString("Food_Addons");
                                String foodPriceAddons = object.optString("Food_PriceAddons");
                                ExtrasModel extrasModel = new ExtrasModel();
                                extrasModel.setId(extraId);
                                extrasModel.setFood_addonsselection(foodAddonSelection);
                                extrasModel.setFood_Addons(foodAddons);
                                extrasModel.setExtra("extra");
                                extrasModel.setFood_PriceAddons(foodPriceAddons);
                                extrasModel.setTrue(false);
                                extrasModel.setQuantity(1);
                                extrasModelArrayList.add(extrasModel);
                                comingSoon.add(foodAddons);
                            }
                            listDataChild.put(foodAddonsGroup,comingSoon);
                        }
                    }
                    Log.e("qw",sharedPreferences.getString("hname",""));
                    
                    String sss = sharedPreferences.getString("hname","");
                    Toast.makeText(ExtraItemActivity.this, "aa"+sss, Toast.LENGTH_SHORT).show();

                    if (sss.equals("FERNANDO'S"))
                    {
                        rvExtraItem.setVisibility(View.GONE);
                        expListView.setVisibility(View.VISIBLE);
                        listAdapter = new ExpandableListAdapter(ExtraItemActivity.this, listDataHeader, listDataChild);

                        expListView.setAdapter(listAdapter);
                    }
                    else
                    {
                        rvExtraItem.setVisibility(View.VISIBLE);
                        expListView.setVisibility(View.GONE);
                        setAdaptors();
                    }
                } catch (JSONException e) {
                    Log.d("exception", "e");
                    e.getMessage();
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ExtraItemActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ItemID", subMenuItemId + "");
//                params.put("SizeDI", "7317");     anuj
                Log.e("it",""+params);
                return params;
            }

        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(ExtraItemActivity.this);
        requestQueue.add(strReq);
    }

    public void setAdaptors() {
        for (int i = 0; i < extrasModelArrayList.size(); i++) {
            for (int j = 0; j < SubMenuActivity.subMenuCartList.size(); j++) {
                if (SubMenuActivity.subMenuCartList.get(j).getId().equals(extrasModelArrayList.get(i).getId())) {
                    extrasModelArrayList.get(i).setTrue(true);
                    menuSizeModelArrayLists.add(SubMenuActivity.subMenuCartList.get(j));
                }
            }
        }
        extraAdapter = new ExtraAdapter(ExtraItemActivity.this, ExtraItemActivity.this, extrasModelArrayList);
        rvExtraItem.setAdapter(extraAdapter);

    }

    private void statusBarColor() {
        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public synchronized void setAdaptor() {
        for (int i = 0; i < menuSizeModelArrayList.size(); i++) {
            for (int j = 0; j < SubMenuActivity.subMenuCartList.size(); j++) {
                if (SubMenuActivity.subMenuCartList.get(j).getId().equals(menuSizeModelArrayList.get(i).getId())) {
                    menuSizeModelArrayList.get(i).setTrue(true);
                    menuSizeModelArrayLists.add(SubMenuActivity.subMenuCartList.get(j));
                }
            }
        }
        menuSizeAdapter = new MenuSizeAdapter(ExtraItemActivity.this, ExtraItemActivity.this, menuSizeModelArrayList);
        rvExtraItem.setAdapter(menuSizeAdapter);
    }

    public synchronized void countExtra() {
        if (extrasModelArrayList.size() > 0) {
            for (int k = 0; k < SubMenuActivity.subMenuCartList.size(); k++) {
                String menu = SubMenuActivity.subMenuCartList.get(k).getMenu();
                if (menu.equals("menu")) {
                    MenuSizeModel menuSizeModel = new MenuSizeModel();
                    menuSizeModel.setQuantity(SubMenuActivity.subMenuCartList.get(k).getQuantity());
                    menuSizeModel.setId(SubMenuActivity.subMenuCartList.get(k).getId());
                    menuSizeModel.setFoodItemName(SubMenuActivity.subMenuCartList.get(k).getRestaurantPizzaItemName());
                    menuSizeModelArrayList.add(menuSizeModel);
                    menuSizeModelArrayListss.add(menuSizeModel);
                }

                for (int j = 0; j < menuSizeModelArrayLists.size(); j++) {
                    if (SubMenuActivity.subMenuCartList.get(k).getId().equals(menuSizeModelArrayLists.get(j).getId())) {
                        SubMenuActivity.subMenuCartList.remove(j);
                    }
        }
            }
            for (int i = 0; i < extrasModelArrayList.size(); i++) {
                if (extrasModelArrayList.get(i).isTrue()) {
                    SubMenuModel subMenuModel = new SubMenuModel();
                    Double aDouble = Double.parseDouble(restaurantPizzaItemPrice) + Double.parseDouble(extrasModelArrayList.get(i).getFood_PriceAddons());
                    subMenuModel.setRestaurantPizzaItemPrice("" + aDouble);
                    subMenuModel.setId(extrasModelArrayList.get(i).getId());
                    subMenuModel.setQuantity(1);
                    subMenuModel.setMenu("extra");
                    subMenuModel.setRestaurantPizzaItemName(restaurantPizzaItemName + System.lineSeparator()+ "+" +" " + extrasModelArrayList.get(i).getFood_Addons() );
                    boolean addvalue = false;
                    for (int j = 0; j < SubMenuActivity.subMenuCartList.size(); j++) {
                        if (SubMenuActivity.subMenuCartList.get(j).getId().equals(extrasModelArrayList.get(i).getId())) {
                            addvalue = true;
                            break;
                        } else {
                            addvalue = false;
                        }
                    }
                    if (addvalue) {

                    } else {
                        extrasModelArrayLists.add(extrasModelArrayList.get(i));
                        SubMenuActivity.subMenuCartList.add(subMenuModel);
                    }
                }
            }
        } else {
            for (int k = 0; k < SubMenuActivity.subMenuCartList.size(); k++) {
                if (SubMenuActivity.subMenuCartList.get(k).getMenu().equals("extra")) {
                    ExtrasModel extrasModel = new ExtrasModel();
                    extrasModel.setQuantity(SubMenuActivity.subMenuCartList.get(k).getQuantity());
                    extrasModel.setId(SubMenuActivity.subMenuCartList.get(k).getId());
                    extrasModel.setFood_Addons(SubMenuActivity.subMenuCartList.get(k).getRestaurantPizzaItemName());
                    extrasModelArrayList.add(extrasModel);
                    extrasModelArrayLists.add(extrasModel);
                }
                for (int j = 0; j < menuSizeModelArrayLists.size(); j++) {
                    if (SubMenuActivity.subMenuCartList.get(k).getId().equals(menuSizeModelArrayLists.get(j).getId())) {
//                        SubMenuActivity.subMenuCartList.remove(j);          //deep
                    }
                }
            }
            for (int i = 0; i < menuSizeModelArrayList.size(); i++) {
                if (menuSizeModelArrayList.get(i).isTrue()) {
                    SubMenuModel subMenuModel = new SubMenuModel();
                    subMenuModel.setRestaurantPizzaItemPrice(menuSizeModelArrayList.get(i).getRestaurantPizzaItemPrice());
                    subMenuModel.setId(menuSizeModelArrayList.get(i).getId());
                    subMenuModel.setQuantity(1);
                    subMenuModel.setMenu("menu");
                    subMenuModel.setRestaurantPizzaItemName(restaurantPizzaItemName + " (" + (menuSizeModelArrayList.get(i).getRestaurantPizzaItemName()) + ")");

//     Anuj               subMenuModel.setRestaurantPizzaItemName(restaurantPizzaItemName + " (" + menuSizeModelArrayList.get(i).getFoodItemName().concat(" ").concat(menuSizeModelArrayList.get(i).getRestaurantPizzaItemName()) + " )");
//                    boolean addvalue = false;

                    menuSizeModelArrayListss.add(menuSizeModelArrayList.get(i));
                    SubMenuActivity.subMenuCartList.add(subMenuModel);

//                    for (int j = 0; j < SubMenuActivity.subMenuCartList.size(); j++) {
//                        if (SubMenuActivity.subMenuCartList.get(j).getId().equals(menuSizeModelArrayList.get(i).getId())) {
//                            addvalue = true;
//                            break;
//                        } else {
//                            addvalue = false;
//                        }
//                    }
//                    if (addvalue) {
//
//                    } else {
//                        menuSizeModelArrayListss.add(menuSizeModelArrayList.get(i));
//                        SubMenuActivity.subMenuCartList.add(subMenuModel);
//                    }
                }
            }
        }
        Intent intent = new Intent(this, SubMenuActivity.class);
        intent.putExtra("subMenuItemId", subMenuItemId);
        intent.putExtra("extraAvailable", extraAvailable);
        intent.putExtra("sizeAvailable", sizeAvailable);
        intent.putExtra("id", resId);
        intent.putExtra("restaurantCategoryId", restaurantCategoryId);
        intent.putExtra("restaurantName", restaurantName);
        intent.putExtra("restaurantAddress", restaurantAddress);
        intent.putExtra("categoryName", categoryName);
        intent.putExtra("subMenuItemId",subMenuItemId);
        if (extrasModelArrayList.size() > 0) {
            intent.putExtra("extrasModelArrayList", extrasModelArrayLists);

        }
        if (menuSizeModelArrayListss.size() > 0) {
            intent.putExtra("menuSizeModelArrayList", menuSizeModelArrayListss);

        }
        startActivity(intent);
        finish();
    }

    @Override
    public void menuItemPosition(boolean istrue, int position) {
        try {
            if (extrasModelArrayList.size() > 0) {
                if (extrasModelArrayList.get(position).getFood_addonsselection().equalsIgnoreCase("2") || extrasModelArrayList.get(position).getFood_addonsselection().equalsIgnoreCase("3")) {
                    for (int i = 0; i < extrasModelArrayList.size(); i++) {
                        if (i == position) {
                            extrasModelArrayList.get(i).setTrue(istrue);
                        } else {
                            extrasModelArrayList.get(i).setTrue(false);
                        }
                    }
                } else {
                    extrasModelArrayList.get(position).setTrue(istrue);
                }
                extraAdapter.notifyDataSetChanged();
            } else {
                for (int i = 0; i < menuSizeModelArrayList.size(); i++) {
                    if (i == position) {
                        menuSizeModelArrayList.get(i).setTrue(istrue);
                    } else {
                        menuSizeModelArrayList.get(i).setTrue(false);
                    }
                }
                menuSizeAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}