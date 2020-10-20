package com.tec9rushbgroup.ourspace;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class CurrentSpace extends AppCompatActivity {

    String TAG = "CurrentSpace";
    private Button photosButton, logsButton, anniversaryButton; //Buttons

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
        photosButton = findViewById(R.id.photos);
        logsButton = findViewById(R.id.logs);
        anniversaryButton = findViewById(R.id.anniversary);


        // related to "activity_photos.xml", "activity_logs.xml", "activity_anniversary.xml", already created but empty.
        photosButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {}
        });

        logsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {}
        });

        anniversaryButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {}
        });
    }
}
