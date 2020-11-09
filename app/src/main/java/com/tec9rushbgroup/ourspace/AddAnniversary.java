package com.tec9rushbgroup.ourspace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AddAnniversary extends AppCompatActivity {
    private Button addAnniversary, backToAnniversaries;
    private TextView welcomeTV, sloganTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_anniversary);
        welcomeTV = findViewById(R.id.welcome_text);
        sloganTV = findViewById(R.id.slogan_text);
        welcomeTV.setTypeface(Typeface.createFromAsset(getAssets(), "logo.ttf"));
        sloganTV.setTypeface(Typeface.createFromAsset(getAssets(), "slogan.ttf"));

        //Buttons
        addAnniversary = findViewById(R.id.add_anniversary_button);
        backToAnniversaries = findViewById(R.id.back_to_anniversaries);

        String uid = getIntent().getStringExtra("uid");
        String user1 = getIntent().getStringExtra("user1");
        String user2 = getIntent().getStringExtra("user2");
        backToAnniversaries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddAnniversary.this, AnniversaryPage.class);
                intent.putExtra("uid",uid);
                intent.putExtra("user1",user1);
                intent.putExtra("user2",user2);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        });

    }
    @Override
    public void onBackPressed() {
        String uid = getIntent().getStringExtra("uid");
        String user1 = getIntent().getStringExtra("user1");
        String user2 = getIntent().getStringExtra("user2");
        Intent intent = new Intent(AddAnniversary.this, AnniversaryPage.class);
        intent.putExtra("uid",uid);
        intent.putExtra("user1",user1);
        intent.putExtra("user2",user2);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }
}