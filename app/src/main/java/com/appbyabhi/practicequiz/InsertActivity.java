package com.appbyabhi.practicequiz;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class InsertActivity extends AppCompatActivity {
    ImageButton back, insertBtn;
    TextInputLayout ti_question, ti_optionA, ti_optionB, ti_optionC, ti_optionD;
    EditText et_question, et_optionA, et_optionB, et_optionC, et_optionD;
    String question, optionA, optionB, optionC, optionD, correctAns;
    int a_select = 0;
    SQLiteDatabase d;
    Spinner spinner_subject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        back = findViewById(R.id.backBtn_insertOperation);
        insertBtn = findViewById(R.id.insertBtn);
        spinner_subject = findViewById(R.id.spinner_subject);

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

        et_question.addTextChangedListener(createTextWatcher());
        et_optionA.addTextChangedListener(createTextWatcher());
        et_optionB.addTextChangedListener(createTextWatcher());
        et_optionC.addTextChangedListener(createTextWatcher());
        et_optionD.addTextChangedListener(createTextWatcher());

        String[] subjectList = getResources().getStringArray(R.array.subjects_list);
        ArrayList<String> subjectArrayList = new ArrayList<>();
        Collections.addAll(subjectArrayList, subjectList);
        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(InsertActivity.this, R.layout.custom_spinner, subjectArrayList);
        spinner_subject.setAdapter(subjectAdapter);

        d = openOrCreateDatabase("quiz_db",MODE_PRIVATE,null);
        d.execSQL("create table if not exists mcq (subject varchar(20),question varchar(200),option_a varchar(200),option_b varchar(200),option_c varchar(200),option_d varchar(200),ans varchar(200))");

        back.setOnClickListener(v -> finish());
        insertBtn.setOnClickListener(v -> {
            if (checkValid()){
                addToDB();
            }
        });
    }

    public void addToDB(){
            optionA = et_optionA.getText().toString().trim();
            optionB = et_optionB.getText().toString().trim();
            optionC = et_optionC.getText().toString().trim();
            optionD = et_optionD.getText().toString().trim();

            String[] answer = {optionA, optionB, optionC, optionD};
            AlertDialog.Builder builder = new AlertDialog.Builder(InsertActivity.this);
            builder.setTitle("Select Correct Answer");

            builder.setSingleChoiceItems(answer, 0, (dialog, which) -> {
                String ans;
                ans = answer[which];
                if(ans.equals(optionA))
                {
                    correctAns = optionA;
                    a_select = 1;
                }
                else if(ans.equals(optionB))
                {
                    correctAns = optionB;
                    a_select = 1;
                }
                else if(ans.equals(optionC))
                {
                    correctAns = optionC;
                    a_select = 1;
                }
                else if(ans.equals(optionD))
                {
                    correctAns = optionD;
                    a_select = 1;
                }
            });

            builder.setPositiveButton("Yes", (dialog, which) -> {
                if (spinner_subject.getSelectedItem().equals("ANDROID")) {
                    insert("ANDROID");
                } else if (spinner_subject.getSelectedItem().equals("BIGDATA")) {
                    insert("BIGDATA");
                } else if (spinner_subject.getSelectedItem().equals("PYTHON")) {
                    insert("PYTHON");
                }
            });
            builder.setNegativeButton("No", (dialog, which) -> {});
            builder.show();
    }

    public void insert(String subject_name){
        if (a_select == 0)
        {
            correctAns = optionA;
        }

        question = et_question.getText().toString().trim();
        optionA = et_optionA.getText().toString().trim();
        optionB = et_optionB.getText().toString().trim();
        optionC = et_optionC.getText().toString().trim();
        optionD = et_optionD.getText().toString().trim();

        try {
            d.execSQL("insert into mcq values ('"+subject_name+"','"+question+"','"+optionA+"','"+optionB+"','"+optionC+"','"+optionD+"','"+correctAns+"' )");
        }
        catch (Exception e)
        {
            System.out.println("Exception :"+e);
        }
        Toast.makeText(InsertActivity.this, "New Question Added Successfully", Toast.LENGTH_SHORT).show();
        et_question.setText("");
        et_optionA.setText("");
        et_optionB.setText("");
        et_optionC.setText("");
        et_optionD.setText("");
        a_select = 0;
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