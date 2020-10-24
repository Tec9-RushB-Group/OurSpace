package com.tec9rushbgroup.ourspace;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AddLogs extends AppCompatActivity {
    private Button addLog, backToLogs;
    private TextView welcomeTV, sloganTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_logs);
        welcomeTV = findViewById(R.id.welcome_text);
        sloganTV = findViewById(R.id.slogan_text);
        welcomeTV.setTypeface(Typeface.createFromAsset(getAssets(), "logo.ttf"));
        sloganTV.setTypeface(Typeface.createFromAsset(getAssets(), "slogan.ttf"));
        //Buttons
        addLog = findViewById(R.id.add_log_button);
        backToLogs = findViewById(R.id.back_to_logs);

        String uid = getIntent().getStringExtra("uid");
        String user1 = getIntent().getStringExtra("user1");
        String user2 = getIntent().getStringExtra("user2");
        backToLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddLogs.this, LogsPage.class);
                intent.putExtra("uid",uid);
                intent.putExtra("user1",user1);
                intent.putExtra("user2",user2);
                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(welcomeTV, "logo_text");
                pairs[1] = new Pair<View, String>(sloganTV, "slogan_text");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AddLogs.this, pairs);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });
    }
}