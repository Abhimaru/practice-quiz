package com.appbyabhi.practicequiz;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Collections;

public class UpdateActivity extends AppCompatActivity {
    ImageButton back, updateBtn;
    TextInputLayout ti_question, ti_optionA, ti_optionB, ti_optionC, ti_optionD;
    EditText et_question, et_optionA, et_optionB, et_optionC, et_optionD;
    String question, optionA, optionB, optionC, optionD, org_question, ans_db, selected;
    int test = 0;
    ArrayList<String> al = new ArrayList<>();
    SQLiteDatabase d;
    Spinner spinner_subject, spinner_question;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        back = findViewById(R.id.backBtn_updateOperation);
        updateBtn = findViewById(R.id.updateBtn);
        spinner_subject = findViewById(R.id.spinner_subject);
        spinner_question = findViewById(R.id.spinner_question);

        ti_question = findViewById(R.id.question_textInput);
        ti_optionA = findViewById(R.id.option_a_textInput);
        ti_optionB = findViewById(R.id.option_b_textInput);
        ti_optionC = findViewById(R.id.option_c_textInput);
        ti_optionD = findViewById(R.id.option_d_textInput);
        et_question = findViewById(R.id.question_txt);
        et_optionA = findViewById(R.id.option_a_txt);
        et_optionB = findViewById(R.id.option_b_txt);
        et_optionC = findViewById(R.id.option_c_txt);
        et_optionD = findViewById(R.id.option_d_txt);

        question = et_question.getText().toString().trim();
        optionA = et_optionA.getText().toString().trim();
        optionB = et_optionB.getText().toString().trim();
        optionC = et_optionC.getText().toString().trim();
        optionD = et_optionD.getText().toString().trim();

        et_question.addTextChangedListener(createTextWatcher());
        et_optionA.addTextChangedListener(createTextWatcher());
        et_optionB.addTextChangedListener(createTextWatcher());
        et_optionC.addTextChangedListener(createTextWatcher());
        et_optionD.addTextChangedListener(createTextWatcher());

        String[] subjectList = getResources().getStringArray(R.array.subjects_list);
        ArrayList<String> subjectArrayList = new ArrayList<>();
        Collections.addAll(subjectArrayList, subjectList);
        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(UpdateActivity.this, R.layout.custom_spinner, subjectArrayList);
        spinner_subject.setAdapter(subjectAdapter);

        d = openOrCreateDatabase("quiz_db",MODE_PRIVATE,null);

        back.setOnClickListener(v -> finish());

        spinner_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner_question.setVisibility(View.VISIBLE);
                selected = spinner_subject.getSelectedItem().toString();

                Cursor c = d.rawQuery("select * from mcq where subject = '"+selected+"'",null);
                if (c.getCount()>0) {
                    al.clear();
                    while (c.moveToNext()) {
                        String q = c.getString(1);
                        al.add(q);
                        ArrayAdapter<String> ad = new ArrayAdapter<>(UpdateActivity.this, R.layout.custom_spinner, al);
                        spinner_question.setAdapter(ad);
                    }
                }
                else{
                    makeVisible(false);
                    Toast.makeText(UpdateActivity.this, "No Questions in "+selected+" Subject", Toast.LENGTH_SHORT).show();
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
                String question = UpdateActivity.this.spinner_question.getSelectedItem().toString();
                makeVisible(true);
                Cursor c = d.rawQuery("select * from mcq where question = '"+question+"'",null);
                if (c.getCount()>0) {
                    while (c.moveToNext()) {
                        String db_question = c.getString(1);
                        org_question = db_question;
                        String db_optionA = c.getString(2);
                        String db_optionB = c.getString(3);
                        String db_optionC = c.getString(4);
                        String db_optionD = c.getString(5);
                        et_question.setText(db_question);
                        et_optionA.setText(db_optionA);
                        et_optionB.setText(db_optionB);
                        et_optionC.setText(db_optionC);
                        et_optionD.setText(db_optionD);
                    }
                }
                c.close();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        updateBtn.setOnClickListener(v -> {
            question = et_question.getText().toString().trim();
            optionA = et_optionA.getText().toString().trim();
            optionB = et_optionB.getText().toString().trim();
            optionC = et_optionC.getText().toString().trim();
            optionD = et_optionD.getText().toString().trim();

            if (checkValid()){
                String[] options = {optionA, optionB, optionC, optionD};
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
                builder.setTitle("Choose Correct Answer: ");
                builder.setSingleChoiceItems(options, 0, (dialog, which) -> {
                    ans_db = options[which];
                    test = 1;
                    Toast.makeText(UpdateActivity.this, "" + ans_db, Toast.LENGTH_SHORT).show();
                });
                builder.setPositiveButton("Confirm", (dialog, which) -> {
                    if (test == 0) {
                        ans_db = options[0];
                    }
                    try {
                        d.execSQL("update mcq set question = '" + question + "',option_a = '" + optionA + "',option_b = '" + optionB + "',option_c = '" + optionC + "',option_d = '" + optionD + "', ans = '" + ans_db + "' where question = '" + org_question + "'");
                    } catch (Exception e) {
                        System.out.println("Exception :" + e);
                    }
                    Cursor c = d.rawQuery("select * from mcq where subject = '"+selected+"'",null);
                    if (c.getCount()>0) {
                        al.clear();
                        while (c.moveToNext()) {
                            String q1 = c.getString(1);
                            al.add(q1);
                            ArrayAdapter<String> ad = new ArrayAdapter<>(UpdateActivity.this, R.layout.custom_spinner, al);
                            spinner_question.setAdapter(ad);
                        }
                    }
                    c.close();
                });
                builder.setNegativeButton("NO", (dialog, which) -> {

                });
                builder.show();
            }
        });
    }

    public void makeVisible(Boolean flag){
        if(flag){
            ti_question.setVisibility(View.VISIBLE);
            ti_optionA.setVisibility(View.VISIBLE);
            ti_optionB.setVisibility(View.VISIBLE);
            ti_optionC.setVisibility(View.VISIBLE);
            ti_optionD.setVisibility(View.VISIBLE);
            updateBtn.setVisibility(View.VISIBLE);
        }else {
            ti_question.setVisibility(View.GONE);
            ti_optionA.setVisibility(View.GONE);
            ti_optionB.setVisibility(View.GONE);
            ti_optionC.setVisibility(View.GONE);
            ti_optionD.setVisibility(View.GONE);
            updateBtn.setVisibility(View.GONE);
        }
    }

    public boolean checkValid() {
        question = et_question.getText().toString().trim();
        optionA = et_optionA.getText().toString().trim();
        optionB = et_optionB.getText().toString().trim();
        optionC = et_optionC.getText().toString().trim();
        optionD = et_optionD.getText().toString().trim();

        if(question.isEmpty()) {
            ti_question.setError("Question can't be empty");
            return false;
        }

        if(optionA.isEmpty()) {
            ti_optionA.setError("Option can't be empty");
            return false;
        }

        if(optionB.isEmpty()) {
            ti_optionB.setError("Option can't be empty");
            return false;
        }

        if(optionC.isEmpty()) {
            ti_optionC.setError("Option can't be empty");
            return false;
        }

        if(optionD.isEmpty()) {
            ti_optionD.setError("Option can't be empty");
            return false;
        }

        ti_question.setError(null);
        ti_optionA.setError(null);
        ti_optionB.setError(null);
        ti_optionC.setError(null);
        ti_optionD.setError(null);
        return true;
    }

    private TextWatcher createTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence,
                                          int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int
                    i, int i1, int i2) {
                checkValid();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }
}