package com.example.dangfiztssi.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    ImageView imgLogo;
    TextView tvAppName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        tvAppName = (TextView) findViewById(R.id.tvAppName);

//        android.app.ActionBar actionBar = getActionBar();
//        actionBar.hide();

        Animation animation = new AlphaAnimation(0,1);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setDuration(2000);

        imgLogo.setAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);

                finish();
            }
        },3000);
    }
}
