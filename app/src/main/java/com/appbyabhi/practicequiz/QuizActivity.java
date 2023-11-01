package com.appbyabhi.practicequiz;

import android.app.NotificationManager;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class QuizActivity extends AppCompatActivity {
    RelativeLayout btn1_layout, btn2_layout, btn3_layout, btn4_layout;
    TextView subjectNameTxt, questionTxt;
    Button nextBtn, submitBtn, opt1, opt2, opt3, opt4;
    ImageButton back, answerBtn, resetBtn;
    int i, flag = 0, correctAns = 0, total = 0, click_done = 0;
    SQLiteDatabase d;
    SensorManager sm;
    Sensor accelerometer;
    Boolean isShaking = false;
    String answer;
    NotificationManager nm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        btn1_layout = findViewById(R.id.btn_1);
        btn2_layout = findViewById(R.id.btn_2);
        btn3_layout = findViewById(R.id.btn_3);
        btn4_layout = findViewById(R.id.btn_4);

        subjectNameTxt = findViewById(R.id.subject_txt);
        questionTxt = findViewById(R.id.question_txt);

        opt1 = findViewById(R.id.opt_1);
        opt2 = findViewById(R.id.opt_2);
        opt3 = findViewById(R.id.opt_3);
        opt4 = findViewById(R.id.opt_4);

        back = findViewById(R.id.backBtn_quiz);
        nextBtn = findViewById(R.id.nextBtn_quiz);
        submitBtn = findViewById(R.id.submitBtn_quiz);
        resetBtn = findViewById(R.id.resetBtn_quiz);
        answerBtn = findViewById(R.id.answerBtn_quiz);

        nextBtn.setEnabled(false);
        nextBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.lightGray, null)));

        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        d = openOrCreateDatabase("quiz_db", MODE_PRIVATE, null);



        if(sm != null){
            accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if(accelerometer != null){
                sm.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            }
        }

        // setting subject name title
        Intent previous = getIntent();
        String subNameIntent = previous.getStringExtra("passed_subject");
        if (subNameIntent != null) {
            subjectNameTxt.setText(subNameIntent);
        }

        try{
            Cursor c = d.rawQuery("select * from mcq where subject = '"+subNameIntent+"'",null);
            i = c.getCount();

            if (i == 0)
            {
                NotificationHandler.cancelNotification(QuizActivity.this);
                Toast.makeText(this, "No Question Available for "+subNameIntent+" Subject", Toast.LENGTH_SHORT).show();
                finish();
            }
            else {
                c.moveToFirst();
                total = i;
                recursionFunction(c);
                submitBtn.setOnClickListener(v -> {
                    Intent i1 = new Intent(QuizActivity.this, ScoreActivity.class);
                    i1.putExtra("Total", total);
                    i1.putExtra("Right", correctAns);
                    i1.putExtra("Subject", subNameIntent);
                    NotificationHandler.cancelNotification(QuizActivity.this);
                    startActivity(i1);
                    finish();
                });
            }
        }
        catch (Exception e)
        {
            System.out.println("Exception: "+e);
        }

        back.setOnClickListener(v -> {
            NotificationHandler.cancelNotification(this);
            finish();
        });

    }

    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            double acceleration = Math.sqrt(x * x + y * y + z * z);
            if (acceleration > 30) {
                if (!isShaking) {
                    isShaking = true;
                    checkFunction(opt1, opt2, opt3, opt4, answer);
                    nextBtn.setEnabled(true);
                    nextBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.yellow, null)));
                    click_done = 1;
                    flag = 1;
                }
            } else {
                isShaking = false;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    private void recursionFunction(Cursor c) {
        questionTxt.setText(c.getString(1));
        opt1.setText(c.getString(2));
        opt2.setText(c.getString(3));
        opt3.setText(c.getString(4));
        opt4.setText(c.getString(5));
        answer = c.getString(6);

        if(c.isLast())
        {
            nextBtn.setVisibility(View.GONE);
            submitBtn.setVisibility(View.VISIBLE);
        }

        opt1.setOnClickListener(v -> checkAnswer(opt1, btn1_layout));
        opt2.setOnClickListener(v -> checkAnswer(opt2, btn2_layout));
        opt3.setOnClickListener(v -> checkAnswer(opt3, btn3_layout));
        opt4.setOnClickListener(v -> checkAnswer(opt4, btn4_layout));

        answerBtn.setOnClickListener(v -> {
            checkFunction(opt1, opt2, opt3, opt4, answer);
            nextBtn.setEnabled(true);
            nextBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.yellow, null)));
            click_done = 1;
            flag = 1;
        });

        nextBtn.setOnClickListener(v -> {
            resetBtn.setVisibility(View.VISIBLE);
            clear();
            click_done=0;
            c.moveToNext();
            nextBtn.setEnabled(false);
            nextBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.lightGray, null)));
            flag=0;
            recursionFunction(c);

        });
        resetBtn.setOnClickListener(v -> {
            clear();
            flag=1;
            click_done=0;
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NotificationHandler.cancelNotification(this);
    }

    public void checkAnswer(Button b, RelativeLayout r){
        if (click_done==0)
        {
            click_done =1;
            String value = b.getText().toString().trim();
            if (value.equals(answer)) {
                if (flag==0)
                {
                    correctAns++;
                    flag=1;
                }
                r.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green, null)));
            } else {
                r.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red, null)));
            }
            checkFunction(opt1, opt2, opt3, opt4, answer);
            nextBtn.setEnabled(true);
            nextBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.yellow, null)));
        }
    }
    private void clear() {
        btn1_layout.setBackgroundTintList(null);
        btn2_layout.setBackgroundTintList(null);
        btn3_layout.setBackgroundTintList(null);
        btn4_layout.setBackgroundTintList(null);
        nextBtn.setEnabled(false);
        nextBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.lightGray, null)));
    }

    private void checkFunction(Button r1, Button r2, Button r3, Button r4, String answer) {
        String v1 = r1.getText().toString().trim();
        String v2 = r2.getText().toString().trim();
        String v3 = r3.getText().toString().trim();
        String v4 = r4.getText().toString().trim();
        if (v1.equals(answer))
        {
            btn1_layout.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green, null)));
        }
        else if (v2.equals(answer))
        {
            btn2_layout.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green, null)));
        }
        else if (v3.equals(answer))
        {
            btn3_layout.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green, null)));
        }
        else if (v4.equals(answer))
        {
            btn4_layout.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green, null)));
        }
    }
}