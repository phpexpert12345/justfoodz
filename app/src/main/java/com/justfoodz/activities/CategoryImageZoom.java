package com.justfoodz.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.justfoodz.R;

public class CategoryImageZoom extends AppCompatActivity {
    ImageView catergoryimagre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_image_zoom);
        catergoryimagre=(ImageView)findViewById(R.id.catergoryimagre);

        Bitmap bitmap = (Bitmap) getIntent().getParcelableExtra("image");
        catergoryimagre.setImageBitmap(bitmap);

    }
}
