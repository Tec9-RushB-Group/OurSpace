package com.tec9rushbgroup.ourspace;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TintTypedArray;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class CurrentSpace extends AppCompatActivity {

    String TAG = "CurrentSpace";
    private Button photosButton, logsButton, anniversaryButton,backButton; //Buttons
    private TextView welcomeTV, sloganTV;
    private FirebaseStorage firebaseStorage; //initialize
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase database;
    private DatabaseReference spaceDatabaseReference, userDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_space);

        welcomeTV = findViewById(R.id.welcome_text);
        sloganTV = findViewById(R.id.slogan_text);
        welcomeTV.setTypeface(Typeface.createFromAsset(getAssets(), "logo.ttf"));
        sloganTV.setTypeface(Typeface.createFromAsset(getAssets(), "slogan.ttf"));
        // setup database.
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        spaceDatabaseReference = database.getReference("Spaces");
        userDatabaseReference = database.getReference("User");

        // connect to xml.
        logsButton = findViewById(R.id.logs_button);
        anniversaryButton = findViewById(R.id.anniversaries_button);
        backButton = findViewById(R.id.back_home_button);
        String uid = getIntent().getStringExtra("uid");
        Log.i(TAG,"space uid: " + uid);

        // for logs, -> "activity_logs_page.xml"
        logsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CurrentSpace.this, LogsPage.class);
                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(welcomeTV, "logo_text");
                pairs[1] = new Pair<View, String>(sloganTV, "slogan_text");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(CurrentSpace.this, pairs);
               // startActivity(intent, options.toBundle());
                startActivity(intent);
                overridePendingTransition(0,0);

            }
        });

        //for anniversaries, "activity_anniversary_page.xml"
        anniversaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CurrentSpace.this, AnniversaryPage.class);
                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(welcomeTV, "logo_text");
                pairs[1] = new Pair<View, String>(sloganTV, "slogan_text");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(CurrentSpace.this, pairs);
                startActivity(intent);
                //startActivity(intent, options.toBundle());
                overridePendingTransition(0,0);

            }
        });

        //Back to dashboard.
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CurrentSpace.this, Login.class);
                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(welcomeTV, "logo_text");
                pairs[1] = new Pair<View, String>(sloganTV, "slogan_text");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(CurrentSpace.this, pairs);
                //startActivity(intent, options.toBundle());
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });


    }
}
