package com.justfoodz.activities;


import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.justfoodz.R;

public class EventActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivBack;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        initialization();
        initializedListener();
        statusBarColor();
    }


    private void initialization() {
        ivBack = findViewById(R.id.iv_back);
        webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl("http://www.justicketz.com/");
        webView.getSettings().setJavaScriptEnabled(true);
    }

    private void initializedListener() {
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                HomeActivity.mDrawerLayout.closeDrawer(HomeActivity.rl_main_left_drawer);
                break;
            default:
                break;
        }
    }

    private void statusBarColor() {
        Window window = this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

