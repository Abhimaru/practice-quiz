package com.appbyabhi.practicequiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class SubjectActivity extends AppCompatActivity {
    ImageButton back;
    Button android, bigdata, python;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        back = findViewById(R.id.backBtn_subject);
        android = findViewById(R.id.androidBtn);
        bigdata = findViewById(R.id.bigdataBtn);
        python = findViewById(R.id.pythonBtn);
        back.setOnClickListener(v -> finish());

        android.setOnClickListener(view -> {
            NotificationHandler.displayNotification(this, "ANDROID");
            Intent i = new Intent(SubjectActivity.this, QuizActivity.class);
            i.putExtra("passed_subject","ANDROID");
            startActivity(i);
        });

        bigdata.setOnClickListener(view -> {
            NotificationHandler.displayNotification(this, "BIGDATA");
            Intent i = new Intent(SubjectActivity.this, QuizActivity.class);
            i.putExtra("passed_subject","BIGDATA");
            startActivity(i);
        });

        python.setOnClickListener(view -> {
            NotificationHandler.displayNotification(this, "PYTHON");
            Intent i = new Intent(SubjectActivity.this, QuizActivity.class);
            i.putExtra("passed_subject","PYTHON");
            startActivity(i);
        });
    }
}