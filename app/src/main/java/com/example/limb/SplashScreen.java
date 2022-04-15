package com.example.limb;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Locale;

public class SplashScreen extends Activity {
    private  static  int SPLASH_SCREEN = 2000;

    Animation topAnim, bottomAnim;
    ImageView image;
    TextView logo;
    boolean connected2 = false;

    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) { }
        return connected;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.push_dowm);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.push_up);
        image = findViewById(R.id.imageView);
        logo = findViewById(R.id.textView);

        image.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);

        final Handler handler = new Handler();
        final int delay = 1500;
        final boolean[] b = {false};


        handler.postDelayed(new Runnable(){
            public void run() {
                if (b[0] == false) {
                    Intent intent = new Intent(SplashScreen.this, EmailPassword.class);
                    Pair[] pairs = new Pair[2];
                    pairs[0] = new Pair<View, String>(image, "logo_image");
                    pairs[1] = new Pair<View, String>(logo, "logo_text");

                    if (connected2==false) {
                        if (isConnected()) {
                            b[0] = true;
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashScreen.this, pairs);
                            startActivity(intent, options.toBundle());
                        } else {
                            String ctg1 = getString(R.string.noInternet);
                            Toast.makeText(SplashScreen.this, ctg1, Toast.LENGTH_LONG).show();
                        }
                    }

                    handler.postDelayed(this, delay);
                }
            }
        }, delay);


        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){

            }
        },SPLASH_SCREEN);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    protected void onPause() {
        super.onPause();
        connected2=true;
    }

    protected void onStop() {
        super.onStop();
        connected2=true;
    }

    protected void onStart() {
        super.onStart();
        connected2=false;
    }


    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang",lang);
        editor.apply();
    }
    public void loadLocale(){
        SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = preferences.getString("My_Lang","");
        setLocale(language);
        String lc = getString(R.string.location);
        SharedPreferences.Editor editor = getSharedPreferences("Locale", MODE_PRIVATE).edit();
        editor.putString("local", lc);
        editor.apply();
    }
}
