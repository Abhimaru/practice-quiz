package com.appbyabhi.practicequiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

public class AdminOperationActivity extends AppCompatActivity {
    ImageButton back;
    Button insert, update, delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_operation);
        back = findViewById(R.id.backBtn_adminOperation);
        insert = findViewById(R.id.insertBtn);
        update = findViewById(R.id.updateBtn);
        delete = findViewById(R.id.deleteBtn);
        back.setOnClickListener(v -> finish());
        insert.setOnClickListener(v -> {
            Intent i = new Intent(AdminOperationActivity.this, InsertActivity.class);
            startActivity(i);
        });

        update.setOnClickListener(v -> {
            Intent i = new Intent(AdminOperationActivity.this, UpdateActivity.class);
            startActivity(i);
        });

        delete.setOnClickListener(v -> {
            Intent i = new Intent(AdminOperationActivity.this, DeleteActivity.class);
            startActivity(i);
        });
    }
}