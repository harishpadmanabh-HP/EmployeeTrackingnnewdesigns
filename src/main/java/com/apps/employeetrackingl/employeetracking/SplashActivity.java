package com.apps.employeetrackingl.employeetracking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SplashActivity extends AppCompatActivity {
    Button next;
    private static int SPLASH_TIME_OUT = 3000;

    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //next =  findViewById(R.id.button);
//
////
//        Thread background = new Thread()
//        {
//            public void run() {
//
//                try {
//                    // Thread will sleep for 5 seconds
//                    sleep(3000);
//
//                    // After 5 seconds redirect to another intent
//                    Intent i=new Intent(SplashActivity.this,LoginActivity.class);
//                    startActivity(i);
//
//                    //Remove activity
//                    finish();
//
//                } catch (Exception e) {
//
//                    System.out.println(e);
//                }
//            }
//        };
//
//        // start thread
//        background.start();

        int Userid;
        prefs = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
        Userid= prefs.getInt("userid", 0);
        if(Userid>0){
            Intent i = new Intent(getApplicationContext(),NavActivity.class);
            startActivity(i);
            finish();
        }else{
            // Means  u are not logged in than go to your login pageview from here

            new Handler().postDelayed(new Runnable() {

                /*
                 * Showing splash screen with a timer. This will be useful when you
                 * want to show case your app logo / company
                 */

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(i);

                    // close this activity
                    finish();
                }
            }, SPLASH_TIME_OUT);


        }}
}

