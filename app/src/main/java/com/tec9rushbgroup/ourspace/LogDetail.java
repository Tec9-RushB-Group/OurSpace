package com.tec9rushbgroup.ourspace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LogDetail extends AppCompatActivity {
    private TextView welcomeTV, sloganTV;
    private Button backButton;
    private TextView contentView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_detail);
        welcomeTV = findViewById(R.id.welcome_text);
        sloganTV = findViewById(R.id.slogan_text);
        welcomeTV.setTypeface(Typeface.createFromAsset(getAssets(), "logo.ttf"));
        sloganTV.setTypeface(Typeface.createFromAsset(getAssets(), "slogan.ttf"));
        backButton = findViewById(R.id.back_button);
        contentView = findViewById(R.id.log_content);

        contentView.setTypeface(Typeface.createFromAsset(getAssets(), "slogan.ttf"));
        String content = getIntent().getStringExtra("content");
        String uid = getIntent().getStringExtra("uid");
        String description = getIntent().getStringExtra("description");
        contentView.setText(content);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogDetail.this, LogsPage.class);
                intent.putExtra("uid", uid);
                intent.putExtra("content", content);
                intent.putExtra("description", description);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });



    }
}