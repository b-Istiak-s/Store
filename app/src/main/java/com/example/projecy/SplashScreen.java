package com.example.projecy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    private int splashTimeOut = 3000; // Time in mili second

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.splash_screen);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar= getSupportActionBar();
        actionBar.hide();

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashScreen.this,HomeActivity.class);
            startActivity(intent);
            finish();
        },splashTimeOut);
    }
}