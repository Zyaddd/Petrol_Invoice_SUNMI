package com.sunmi.printerhelper.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.sunmi.printerhelper.R;

public class IntroductoryActivity extends AppCompatActivity {
    ImageView logo, splashImg_color;
    LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introductory_avctivity);


        splashImg_color = findViewById(R.id.img_bg_color);
        lottieAnimationView = findViewById(R.id.lottie);
        logo = findViewById(R.id.splash_logo_img);

        splashImg_color.animate().translationY(-2300).setDuration(1000).setStartDelay(4000);
        logo.animate().translationY(-2300).setDuration(1000).setStartDelay(4000);
        lottieAnimationView.animate().translationY(-1400).setDuration(1000).setStartDelay(4000);

        Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                Intent startApp = new Intent(getApplicationContext(), ATPLPrintTicketActivity.class);
                startActivity(startApp);
            }
        };

        handler.postDelayed(r, 4000);
    }
}