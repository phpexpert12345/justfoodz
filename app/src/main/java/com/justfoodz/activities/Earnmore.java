package com.justfoodz.activities;


import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.justfoodz.R;
import com.justfoodz.utils.Network;

public class Earnmore extends AppCompatActivity {

    private WebView wbthan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earnmore);
        statusBarColor();
        wbthan = findViewById(R.id.wbthan);
        if (Network.isNetworkCheck(this)) {
//            thankYouOrder();
            wbthan.loadUrl("https://www.justfoodz.com/loyalty_web_view.php");
            WebSettings webSettings = wbthan.getSettings();
            webSettings.setJavaScriptEnabled(true);
        } else {
            Toast.makeText(this, getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }
    }

    private void statusBarColor() {
        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color

    }
}
