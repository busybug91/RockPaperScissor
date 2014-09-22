package com.example.nitin.rockpapersissor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Yifei on 9/21/14.
 */
public class SplashActivity extends Activity {


    //String KEY_LOGIN_NAME = "IS_USER_LOGIN";

//    private boolean isLogin() {
//        return session.pref.getBoolean(KEY_LOGIN_NAME, false);
//    }

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UserSessionManager session;

        session = new UserSessionManager(getApplicationContext());

        // Check for login status Using SharedPreference
        if (!session.checkLogin()) {
            Log.d("Splash Activity ", "User not logged in so Login Screen will appear");
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        else {
            Log.d("Splash Activity ", "User is logged in so Home Screen will appear");
            Intent intent = new Intent(SplashActivity.this, PlayActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

}
