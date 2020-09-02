package com.justfoodz.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {


    public static boolean networkStatus(Context context) {

        return (ConnectionDetector.isWifiAvailable(context) || ConnectionDetector.isMobileNetworkAvailable(context));

    }

    public static boolean isMobileNetworkAvailable(Context ctx) {

        ConnectivityManager connecManager = (ConnectivityManager) ctx

                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo myNetworkInfo = connecManager

                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (myNetworkInfo.isConnected()) {

            return true;

        } else {

            return false;

        }

    }

    public static boolean isWifiAvailable(Context ctx) {

        ConnectivityManager myConnManager = (ConnectivityManager) ctx

                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo myNetworkInfo = myConnManager

                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (myNetworkInfo.isConnected())

            return true;

        else

            return false;

    }

}