package com.justfoodz.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.justfoodz.activities.CategoryImageZoom;
import com.justfoodz.R;
import com.justfoodz.activities.Ravifinalitem;
import com.justfoodz.activities.SplashScreenActivity;
import com.justfoodz.activities.SubMenuActivity;
import com.justfoodz.Database.Database;

import com.justfoodz.models.SubMenumodelravi;
import com.justfoodz.utils.MyPref;
import com.justfoodz.utils.Network;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuSubItemAdapter extends RecyclerView.Adapter<MenuSubItemAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<SubMenumodelravi> subMenumodelravis;
    String pound = SplashScreenActivity.currency_symbol;
    Dialog imagedialog;
    ImageView catergoryimagre;
    MyPref myPref;
    Bitmap bitmap;
    byte[] byteArray;

    public MenuSubItemAdapter(Context c, ArrayList<SubMenumodelravi> s) {
        this.context = c;
        this.subMenumodelravis = s;
        myPref = new MyPref(c);
    }

    @NonNull
    @Override
    public MenuSubItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.submenu_listitem, parent, false);
        return new MyViewHolder(itemView);
    }

    public void showMenuImageDialog() {
        imagedialog = new Dialog(context);
        imagedialog.setCanceledOnTouchOutside(true);
        imagedialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        imagedialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        imagedialog.setContentView(R.layout.activity_category_image_zoom);
        imagedialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        imagedialog.getWindow().getAttributes().windowAnimations = R.style.diologIntertnet;
        catergoryimagre = (ImageView) imagedialog.findViewById(R.id.catergoryimagre);

        catergoryimagre.setImageBitmap(bitmap);

        imagedialog.show();
    }

    @Override
    public void onBindViewHolder(@NonNull final MenuSubItemAdapter.MyViewHolder holder, final int position) {

        holder.tvMenuName.setText(subMenumodelravis.get(position).getRestaurantPizzaItemName());

        if (!subMenumodelravis.get(position).getFood_TypeImage().equalsIgnoreCase("")) {
            Picasso.get().load(subMenumodelravis.get(position).getFood_TypeImage()).into(holder.categoryimage);
        } else {
            Picasso.get().load("url").placeholder(R.drawable.noimagecircle).into(holder.categoryimage);
        }


        holder.categoryimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.categoryimage.buildDrawingCache();
                bitmap = holder.categoryimage.getDrawingCache();
                showMenuImageDialog();
//                 Intent intent=new Intent(context,CategoryImageZoom.class);
//                intent.putExtra("image",bitmap);
//                 context.startActivity(intent);
            }

        });
        if(subMenumodelravis.get(position).getResPizzaDescription().length()>60){
            holder.tvRestaurantPizzaDes.setText(subMenumodelravis.get(position).getResPizzaDescription().substring(0,60)+"...View More");
        }else {
            holder.tvRestaurantPizzaDes.setText(subMenumodelravis.get(position).getResPizzaDescription());
        }
        holder.tvRestaurantPizzaDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        context);
                builder.setIcon(R.drawable.logo);
             //   builder.setTitle(String.valueOf(subMenumodelravis.get(position).getResPizzaDescription().length()));
                builder.setMessage(subMenumodelravis.get(position).getResPizzaDescription());
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                                dialog.cancel();
                                //Toast.makeText(context,"Yes is clicked",Toast.LENGTH_LONG).show();
                            }
                        });
                builder.show();
            }
        });

        if(!subMenumodelravis.get(position).getRestaurantPizzaItemPrice().equalsIgnoreCase("")||
                subMenumodelravis.get(position).getRestaurantPizzaItemPrice().length()>0) {
            holder.tvItemCost.setText(myPref.getBookid().concat(subMenumodelravis.get(position).getRestaurantPizzaItemPrice()));
        }
        if (subMenumodelravis.get(position).getExtraavailable().equalsIgnoreCase("No") && subMenumodelravis.get(position).getSizeavailable().equalsIgnoreCase("No")) {

            final Database database = new Database(context);
            final String subMenuItemId = subMenumodelravis.get(position).getId();
            holder.tvPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, "   "+position, Toast.LENGTH_SHORT).show();
                    int qunt = 0;
                    double price = 0.0;
                    SQLiteDatabase db = database.getReadableDatabase();
                    Cursor cursor = db.rawQuery("SELECT * FROM item_table where item_id='" + subMenuItemId + "'", null);
                    if (cursor.moveToNext()) {
                        qunt = Integer.parseInt(cursor.getString(7)) + 1;
                        price = Double.parseDouble(subMenumodelravis.get(position).getRestaurantPizzaItemPrice()) * qunt;
                        database.update_item(subMenuItemId, qunt, price);
                        holder.tvPlus.setVisibility(View.GONE);
                        holder.llminusplus.setVisibility(View.VISIBLE);
                        holder.txtvaluesingle.setText("" + qunt);
                    } else {
                        holder.tvPlus.setVisibility(View.GONE);
                        holder.llminusplus.setVisibility(View.VISIBLE);
                        holder.txtvaluesingle.setText("1");
                        database.InsertItem(subMenuItemId, holder.tvMenuName.getText().toString(), "0", "0", "0", "0", Double.parseDouble(subMenumodelravis.get(position).getRestaurantPizzaItemPrice()), 1);
                        Ravifinalitem.cart_Item_number = Ravifinalitem.cart_Item_number + 1;
                        SubMenuActivity.tvCartItemCount.setText("" + Ravifinalitem.cart_Item_number);
                    }
                    database.close();
                }
            });
            holder.txtplussingle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int qunt = 0;
                    double price = 0.0;
                    SQLiteDatabase db = database.getReadableDatabase();
                    Cursor cursor = db.rawQuery("SELECT * FROM item_table where item_id='" + subMenuItemId + "'", null);
                    if (cursor.moveToNext()) {
                        qunt = Integer.parseInt(cursor.getString(7)) + 1;
                        price = Double.parseDouble(subMenumodelravis.get(position).getRestaurantPizzaItemPrice()) * qunt;
                        database.update_item(subMenuItemId, qunt, price);
                        holder.tvPlus.setVisibility(View.GONE);
                        holder.llminusplus.setVisibility(View.VISIBLE);
                        holder.txtvaluesingle.setText("" + qunt);
                    }
                    database.close();
                }
            });

            holder.txtminussingle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int qunt = 0;
                    double price = 0.0;
                    SQLiteDatabase db = database.getReadableDatabase();
                    Cursor cursor = db.rawQuery("SELECT * FROM item_table where item_id='" + subMenuItemId + "'", null);
                    if (cursor.moveToNext()) {
                        qunt = Integer.parseInt(cursor.getString(7));
                        price = Double.parseDouble(subMenumodelravis.get(position).getRestaurantPizzaItemPrice());
                        price = price / qunt;
                        price = (double) Math.round(price * 100) / 100;
                        qunt = qunt - 1;
                        price = price * qunt;
                        if (qunt == 0) {
                            database.delete_Item(subMenuItemId);
                            Ravifinalitem.cart_Item_number = Ravifinalitem.cart_Item_number - 1;
                            SubMenuActivity.tvCartItemCount.setText("" + Ravifinalitem.cart_Item_number);

                            holder.tvPlus.setVisibility(View.VISIBLE);
                            holder.llminusplus.setVisibility(View.GONE);
                        } else {
                            database.update_item(subMenuItemId, qunt, price);
                            holder.txtvaluesingle.setText("" + qunt);
                        }
                    }
                    database.close();
                }
            });
        } else if (subMenumodelravis.get(position).getExtraavailable().equalsIgnoreCase("No") && subMenumodelravis.get(position).getSizeavailable().equalsIgnoreCase("Yes")) {
            holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String subMenuItemId = subMenumodelravis.get(position).getId();
                    Intent intent = new Intent(context, Ravifinalitem.class);
                    intent.putExtra("tocall", "size");
                    intent.putExtra("itemid", subMenuItemId);
                    intent.putExtra("itemname", holder.tvMenuName.getText().toString());
                    intent.putExtra("price", "" + subMenumodelravis.get(position).getRestaurantPizzaItemPrice());
                    context.startActivity(intent);
                }
            });
        } else if (subMenumodelravis.get(position).getExtraavailable().equalsIgnoreCase("YES") && subMenumodelravis.get(position).getSizeavailable().equalsIgnoreCase("No")) {
            // holder.tvFrom.setVisibility(View.VISIBLE);
            holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String subMenuItemId = subMenumodelravis.get(position).getId();
                    Intent intent = new Intent(context, Ravifinalitem.class);
                    intent.putExtra("tocall", "extra");
                    intent.putExtra("itemid", subMenuItemId);
                    intent.putExtra("itemname", holder.tvMenuName.getText().toString());
                    intent.putExtra("price", "" + subMenumodelravis.get(position).getRestaurantPizzaItemPrice());
                    context.startActivity(intent);
                }
            });
        } else if (subMenumodelravis.get(position).getExtraavailable().equalsIgnoreCase("YES") && subMenumodelravis.get(position).getSizeavailable().equalsIgnoreCase("YES")) {
            //  holder.tvFrom.setVisibility(View.VISIBLE);
            holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String subMenuItemId = subMenumodelravis.get(position).getId();
                    Intent intent = new Intent(context, Ravifinalitem.class);
                    intent.putExtra("tocall", "both");
                    intent.putExtra("itemid", subMenuItemId);
                    intent.putExtra("itemname", holder.tvMenuName.getText().toString());
                    intent.putExtra("price", "" + subMenumodelravis.get(position).getRestaurantPizzaItemPrice());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return subMenumodelravis.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout mainLayout;
        private TextView tvMenuName, tvRestaurantPizzaDes, tvItemCost, tvPlus, tvFrom, txtminussingle, txtvaluesingle, txtplussingle;
        LinearLayout llminusplus;
        private CircleImageView categoryimage;

        public MyViewHolder(View itemView) {
            super(itemView);
            mainLayout = itemView.findViewById(R.id.main_layout);
            tvMenuName = itemView.findViewById(R.id.tv_menu_name);
            tvRestaurantPizzaDes = itemView.findViewById(R.id.tv_restaurant_pizza_des);
            tvItemCost = itemView.findViewById(R.id.tv_item_cost);
            //   tvFrom = itemView.findViewById(R.id.tv_from);
            txtminussingle = itemView.findViewById(R.id.txtminussingle);
            txtvaluesingle = itemView.findViewById(R.id.txtvaluesingle);
            txtplussingle = itemView.findViewById(R.id.txtplussingle);
            llminusplus = itemView.findViewById(R.id.llminusplus);
            tvPlus = itemView.findViewById(R.id.tv_plus);
            categoryimage = itemView.findViewById(R.id.categoryimage);
        }
    }
}