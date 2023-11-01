package com.appbyabhi.practicequiz;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ScoreActivity extends AppCompatActivity {
    ImageButton home, share;
    TextView right, wrong, percent;
    int rightAns, totalAns, wrongAns;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        home = findViewById(R.id.homeBtn_score);
        share = findViewById(R.id.shareBtn_score);
        right = findViewById(R.id.rightAns_score);
        wrong = findViewById(R.id.wrongAns_score);
        percent = findViewById(R.id.percent_score);

        Animation fade = AnimationUtils.loadAnimation(this, R.anim.fade);

        right.startAnimation(fade);
        wrong.startAnimation(fade);
        percent.startAnimation(fade);

        Intent i = getIntent();
        totalAns = i.getIntExtra("Total",0);
        rightAns = i.getIntExtra("Right",0);
        String sub = i.getStringExtra("Subject");
        wrongAns = totalAns - rightAns;

        right.setText(rightAns + " / " + totalAns);
        wrong.setText(wrongAns + " / " + totalAns);
        percent.setText(((rightAns * 100) / totalAns) + "%");

        home.setOnClickListener(v -> finish());

        share.setOnClickListener(v -> {
            assert sub != null;
            String shareText = "Hello, I've scored " + rightAns + " out of " + totalAns + " in " + sub.toLowerCase() + " quiz!";
            shareTextToApps(shareText);
        });
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void shareTextToApps(String shareText) {
        Intent instagramIntent = createShareIntent("com.instagram.android", shareText);
        Intent whatsappIntent = createShareIntent("com.whatsapp", shareText);
        Intent linkedinIntent = createShareIntent("com.linkedin.android", shareText);
        Intent gmailIntent = createShareIntent("com.google.android.gm", shareText);

        Intent[] targetedShareIntents = {whatsappIntent, linkedinIntent, gmailIntent};

        Intent chooserIntent = Intent.createChooser(instagramIntent, "Share using");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents);

        if (chooserIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooserIntent);
        } else {
            // Handle the case where none of the specified apps are installed
            Toast.makeText(this, "application Not installed", Toast.LENGTH_SHORT).show();
        }
    }

    private Intent createShareIntent(String packageName, String shareText) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.setPackage(packageName);
        intent.putExtra(Intent.EXTRA_TEXT, shareText);
        return intent;
    }
}