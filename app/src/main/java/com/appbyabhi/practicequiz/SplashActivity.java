package com.appbyabhi.practicequiz;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    ImageView upperCircle, lowerCircle;
    Timer timer1, timer2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        upperCircle = findViewById(R.id.upperCircle_splash);
        lowerCircle = findViewById(R.id.lowerCircle_splash);

        Animation moveUp = AnimationUtils.loadAnimation(this, R.anim.moveupright);
        Animation moveDown = AnimationUtils.loadAnimation(this, R.anim.movedownleft);

        Intent i = new Intent(SplashActivity.this, MainActivity.class);
        timer1 = new Timer();
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(i);
                finish();
            }
        },3000);

        timer2 = new Timer();
        timer2.schedule(new TimerTask() {
            @Override
            public void run() {
                upperCircle.startAnimation(moveUp);
                lowerCircle.startAnimation(moveDown);
            }
        },2000);
    }
}