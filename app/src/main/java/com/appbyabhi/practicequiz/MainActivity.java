package com.appbyabhi.practicequiz;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    ImageView upperCircle, lowerCircle;
    Animation slideUp, slideDown;
    Button adminBtn, quizBtn, contactBtn;
    SQLiteDatabase d;
    Timer t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        upperCircle = findViewById(R.id.upperCircle_main);
        lowerCircle = findViewById(R.id.lowerCircle_main);

        adminBtn = findViewById(R.id.adminBtn);
        quizBtn = findViewById(R.id.takeQuizBtn);
        contactBtn = findViewById(R.id.contactBtn);

        d = openOrCreateDatabase("quiz_db",MODE_PRIVATE,null);
        d.execSQL("create table if not exists mcq (subject varchar(20),question varchar(200),option_a varchar(200),option_b varchar(200),option_c varchar(200),option_d varchar(200),ans varchar(200))");

        quizBtn.setOnClickListener(v -> {
            startAnimationReverse();
            t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this, SubjectActivity.class);
                    startActivity(intent);
                }
            },200);
        });

        adminBtn.setOnClickListener(v -> {
            startAnimationReverse();
            t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this, AdminLoginActivity    .class);
                    startActivity(intent);
                }
            },200);
        });

        contactBtn.setOnClickListener(v -> {
            String phone = "0987654321";
            makePhoneCall(phone);
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        startAnimation();
    }

    private void startAnimation(){
        slideUp = AnimationUtils.loadAnimation(this, R.anim.slideup);
        slideDown = AnimationUtils.loadAnimation(this, R.anim.slidedown);
        upperCircle.startAnimation(slideDown);
        lowerCircle.startAnimation(slideUp);
    }

    private void startAnimationReverse(){
        slideUp = AnimationUtils.loadAnimation(this, R.anim.slideupreverse);
        slideDown = AnimationUtils.loadAnimation(this, R.anim.slidedownreverse);
        upperCircle.startAnimation(slideDown);
        lowerCircle.startAnimation(slideUp);
    }

    private void makePhoneCall(String phoneNumber) {
        if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        }
    }
}