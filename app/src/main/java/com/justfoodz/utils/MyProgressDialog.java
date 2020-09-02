package com.justfoodz.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;

import com.justfoodz.R;

public class MyProgressDialog {

    private static ProgressDialog progressDialog;

    public static void show(Context context, int messageResourceId) {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
        }

        int style;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            style = R.style.AppCompatAlertDialogStyle;
        } else {
            //noinspection deprecation
            style = ProgressDialog.THEME_HOLO_LIGHT;
        }

        try {
            progressDialog = new ProgressDialog(context, style);
            progressDialog.setMessage(context.getResources().getString(messageResourceId));
            progressDialog.show();
        }catch (Exception e){

        }



    }



    public static void dismiss() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }


}