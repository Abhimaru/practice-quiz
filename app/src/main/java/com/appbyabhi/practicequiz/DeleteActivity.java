package com.appbyabhi.practicequiz;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class DeleteActivity extends AppCompatActivity {
    ImageButton back, deleteBtn;
    String select, del;
    int check = 0;
    SQLiteDatabase d;
    ArrayList<String> al = new ArrayList<>();
    Spinner spinner_subject, spinner_question;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        back = findViewById(R.id.backBtn_deleteOperation);
        deleteBtn = findViewById(R.id.deleteBtn);
        spinner_subject = findViewById(R.id.spinner_subject);
        spinner_question = findViewById(R.id.spinner_question);
        d = openOrCreateDatabase("quiz_db",MODE_PRIVATE,null);

        String[] subjectList = getResources().getStringArray(R.array.subjects_list);
        ArrayList<String> subjectArrayList = new ArrayList<>();
        Collections.addAll(subjectArrayList, subjectList);
        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(DeleteActivity.this, R.layout.custom_spinner, subjectArrayList);
        spinner_subject.setAdapter(subjectAdapter);

        back.setOnClickListener(v -> finish());

        spinner_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner_question.setVisibility(View.VISIBLE);
                deleteBtn.setVisibility(View.VISIBLE);
                select = spinner_subject.getSelectedItem().toString();
                Cursor c = d.rawQuery("select * from mcq where subject = '"+select+"'",null);
                if (c.getCount()>0) {
                    al.clear();
                    while (c.moveToNext()) {
                        String q = c.getString(1);
                        al.add(q);
                        ArrayAdapter<String> ad = new ArrayAdapter<>(DeleteActivity.this, R.layout.custom_spinner, al);
                        spinner_question.setAdapter(ad);
                    }
                }
                else{
                    spinner_question.setVisibility(View.INVISIBLE);
                    deleteBtn.setVisibility(View.INVISIBLE);
                    Toast.makeText(DeleteActivity.this, "No Questions in "+select+" Subject", Toast.LENGTH_SHORT).show();
                }
                c.close();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_question.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                del = spinner_question.getSelectedItem().toString().trim();
                check=1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        deleteBtn.setOnClickListener(v -> {
            try {
                d.execSQL("delete from mcq where question = '"+del+"'");
                Cursor c = d.rawQuery("select * from mcq where subject = '"+select+"'",null);
                if (c.getCount()>0) {
                    al.clear();
                    while (c.moveToNext()) {
                        String q = c.getString(1);
                        al.add(q);
                        ArrayAdapter<String> ad = new ArrayAdapter<>(DeleteActivity.this, R.layout.custom_spinner, al);
                        spinner_question.setAdapter(ad);
                    }
                }
                else
                {
                    spinner_question.setVisibility(View.INVISIBLE);
                    deleteBtn.setVisibility(View.INVISIBLE);
                    Toast.makeText(DeleteActivity.this, "No Questions in "+select+" Subject", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(DeleteActivity.this, "Delete Operation Successful", Toast.LENGTH_SHORT).show();
                c.close();
            }
            catch (Exception e)
            {
                System.out.println("Exception: "+e);
            }
        });
    }
}