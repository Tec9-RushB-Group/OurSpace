package com.tec9rushbgroup.ourspace;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

    private FirebaseStorage firebaseStorage; //initialize
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase database;
    private DatabaseReference spaceDatabaseReference, userDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_space);

        // setup database.
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        spaceDatabaseReference = database.getReference("Spaces");
        userDatabaseReference = database.getReference("User");

        // connect to xml.
        logsButton = findViewById(R.id.logs);
        anniversaryButton = findViewById(R.id.anniversaries);
        backButton = findViewById(R.id.back_home_button);
        String uid = getIntent().getStringExtra("uid");
        Log.i(TAG,"space uid: " + uid);

        // for logs, -> "activity_logs_page.xml"
        logsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CurrentSpace.this, LogsPage.class);
                startActivity(intent);
            }
        });

        //for anniversaries, "activity_anniversary_page.xml"
        anniversaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CurrentSpace.this, AnniversaryPage.class);
                startActivity(intent);
            }
        });

        //Back to dashboard.
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CurrentSpace.this, Login.class);
                startActivity(intent);
            }
        });


    }
}
