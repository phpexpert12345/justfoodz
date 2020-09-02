package com.justfoodz;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {


    private static final String TAG = "MyFirebaseIIDService";


    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Log.e(TAG, "Refreshed token:" + refreshedToken);
        SharedPreferences sp = getSharedPreferences("noti", MODE_PRIVATE);
        SharedPreferences.Editor edt = sp.edit();
        edt.putString("token", refreshedToken);
        edt.commit();



        sendRegistrationToServer(refreshedToken);
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * <p>
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * <p>
     * is initially generated so this is where you would retrieve the token.
     */



    // [START refresh_token]
  /*  @Override

    public void onTokenRefresh() {

        // Get updated InstanceID token.

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Log.e(TAG, "Refreshed token:" + refreshedToken);
        SharedPreferences sp = getSharedPreferences("noti", MODE_PRIVATE);
        SharedPreferences.Editor edt = sp.edit();
        edt.putString("token", refreshedToken);
        edt.commit();



        sendRegistrationToServer(refreshedToken);

    }*/



    private void sendRegistrationToServer(String token) {

        // TODO: Implement this method to send token to your app server.


    }
}
