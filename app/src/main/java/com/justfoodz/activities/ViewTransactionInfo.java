package com.justfoodz.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.justfoodz.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ViewTransactionInfo extends AppCompatActivity {

    TextView emailid, orderid, amount, name, mobileno, time, companyname, companyaddress, heading, download, textView4;
    ConstraintLayout cc;
    ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transaction_info);
        emailid = findViewById(R.id.emailid);
        orderid = findViewById(R.id.orderid);
        amount = findViewById(R.id.amount);
        textView4 = findViewById(R.id.textView4);
        name = findViewById(R.id.name);
        mobileno = findViewById(R.id.mobileno);
//        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        companyname = findViewById(R.id.companyname);
        companyaddress = findViewById(R.id.companyaddress);
        heading = findViewById(R.id.heading);
        download = findViewById(R.id.download);
        cc = findViewById(R.id.cc);
        iv_back = findViewById(R.id.iv_back);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewTransactionInfo.this, DigitalWallet.class);
                startActivity(intent);
            }
        });
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = cc.getRootView();
                v.setDrawingCacheEnabled(true);
                Bitmap b = v.getDrawingCache();
                String extr = Environment.getExternalStorageDirectory().toString();
//                String dirPath = Environment.getExternalStorageDirectory().toString() + "/JustFoodz";

                File myPath = new File(extr, getIntent().getStringExtra("transactionid") + ".jpg");
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(myPath);
                    b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                    Toast.makeText(ViewTransactionInfo.this, "Download Completed", Toast.LENGTH_SHORT).show();
                    // MediaStore.Images.Media.insertImage(getContentResolver(), b, "Screen", "screen");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        String transactionid = getIntent().getStringExtra("transactionid");
        String datee = getIntent().getStringExtra("date");
        String timeee = getIntent().getStringExtra("time");
        String money = getIntent().getStringExtra("walletmoney");
        String namee = getIntent().getStringExtra("name");
        String mobilenoo = getIntent().getStringExtra("mobileno");
        String emailidd = getIntent().getStringExtra("emailid");
        String companyaddressss = getIntent().getStringExtra("companyaddress");
        String companynameeee = getIntent().getStringExtra("companyname");
        String headinggg = getIntent().getStringExtra("heading");


        orderid.setText("Order Id:" + transactionid);
        emailid.setText(emailidd);
        mobileno.setText(mobilenoo);
        amount.setText(SplashScreenActivity.currency_symbol + money);
//        date.setText(datee+",");
        time.setText(datee + " " + timeee);
        name.setText(namee);
        companyname.setText(companynameeee);
        companyaddress.setText(companyaddressss);
        heading.setText(headinggg);
        textView4.setText("" + getIntent().getStringExtra("Billed_Heading"));


    }
}
