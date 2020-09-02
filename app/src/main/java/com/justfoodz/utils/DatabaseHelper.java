package com.justfoodz.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.justfoodz.models.Citymodel;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "justFoodz.db";
    private static final int DATABASE_VERSION = 1;
    private Context context;

    /*countery details*/
    private static final String CityTable = "CityTables";
    private static final String CityAutoID = "CityAutoIDs";
    private static final String CityID = "id";
    private static final String City_name = "city_name";
    private static final String Seo_url_call = "seo_url_call";
    private static final String City_where = "wheres";


    private static final String COUNTRYTABLE = "create table " + CityTable + "(" + CityAutoID + "  INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL," + CityID + " TEXT," +
            "" + City_name + " TEXT," + Seo_url_call + " TEXT," + City_where + " TEXT)";

    /*countery details end*/


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(COUNTRYTABLE);
        ;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CityTable);


    }


    public void saveCityDetails(Citymodel model_country) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CityID, model_country.getId());
        values.put(City_name, model_country.getCity_name());
        values.put(Seo_url_call, model_country.getSeo_url_call());
        values.put(City_where, model_country.getWhere());


        long rowid = db.update(CityTable, values, City_name + "='" + model_country.getCity_name() + "'", null);
        if (rowid == 0) {
            db.insert(CityTable, null, values);
        }
        db.close();
    }


    public ArrayList<Citymodel> getCityListing() {
        ArrayList<Citymodel> model_cart = new ArrayList<Citymodel>();
        String query = "select * from " + CityTable;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.getCount() > 0) {

            if (cursor.moveToFirst()) {
                do {
                    Citymodel citymodel = new Citymodel();
                    citymodel.setId(cursor.getString(cursor.getColumnIndex(CityID)));
                    citymodel.setCity_name(cursor.getString(cursor.getColumnIndex(City_name)));
                    citymodel.setSeo_url_call(cursor.getString(cursor.getColumnIndex(Seo_url_call)));
                    citymodel.setWhere(cursor.getString(cursor.getColumnIndex(City_where)));
                    model_cart.add(citymodel);
                } while (cursor.moveToNext());
            }
            db.close();
            return model_cart;
        } else {
            db.close();
            return model_cart;
        }


    }

}
