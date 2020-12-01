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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
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
    private List<User> userList;
    private List<Space> spaceList;
    private String currentUserEmail;
    private Space currentSpace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_space);

        setUpEnvironment();

        welcomeTV = findViewById(R.id.welcome_text);
        sloganTV = findViewById(R.id.slogan_text);
        welcomeTV.setTypeface(Typeface.createFromAsset(getAssets(), "logo.ttf"));
        sloganTV.setTypeface(Typeface.createFromAsset(getAssets(), "slogan.ttf"));
        // connect to xml.
        logsButton = findViewById(R.id.logs_button);
        anniversaryButton = findViewById(R.id.anniversaries_button);
        backButton = findViewById(R.id.back_home_button);
        photosButton = findViewById(R.id.photos_button);
        String uid = getIntent().getStringExtra("uid");
        String user1 = getIntent().getStringExtra("user1");
        String user2 = getIntent().getStringExtra("user2");
        Log.i(TAG,"space uid: " + uid);

        photosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid = getIntent().getStringExtra("uid");
                String user1 = getIntent().getStringExtra("user1");
                String user2 = getIntent().getStringExtra("user2");
                Intent intent = new Intent(CurrentSpace.this, PhotoPage.class);
                intent.putExtra("uid",uid);
                intent.putExtra("user1",user1);
                intent.putExtra("user2",user2);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        });
        // for logs, -> "activity_logs_page.xml"
        logsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid = getIntent().getStringExtra("uid");
                String user1 = getIntent().getStringExtra("user1");
                String user2 = getIntent().getStringExtra("user2");
                Intent intent = new Intent(CurrentSpace.this, LogsPage.class);
                intent.putExtra("uid",uid);
                intent.putExtra("user1",user1);
                intent.putExtra("user2",user2);
                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(welcomeTV, "logo_text");
                pairs[1] = new Pair<View, String>(sloganTV, "slogan_text");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(CurrentSpace.this, pairs);
               // startActivity(intent, options.toBundle());
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();

            }
        });

        //for anniversaries, "activity_anniversary_page.xml"
        anniversaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid = getIntent().getStringExtra("uid");
                String user1 = getIntent().getStringExtra("user1");
                String user2 = getIntent().getStringExtra("user2");
                Intent intent = new Intent(CurrentSpace.this, AnniversaryPage.class);
                intent.putExtra("uid",uid);
                intent.putExtra("user1",user1);
                intent.putExtra("user2",user2);
                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(welcomeTV, "logo_text");
                pairs[1] = new Pair<View, String>(sloganTV, "slogan_text");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(CurrentSpace.this, pairs);
                startActivity(intent);
                //startActivity(intent, options.toBundle());
                overridePendingTransition(0,0);
                finish();

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
                finish();
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        userDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot spaceSnapshot : snapshot.getChildren()) {
                    User user = spaceSnapshot.getValue(User.class);
                    userList.add(user);
                   // Log.i(TAG,"user added : " +user.getEmail() );
                }
                if (sloganTV!=null){
                    sloganTV.setText(getSloganString());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }

        });

    }


    private String getSloganString(){
        String user1 = getIntent().getStringExtra("user1");
        String user2 = getIntent().getStringExtra("user2");
        //Log.i(TAG,"email1 : " +user1 );
        //Log.i(TAG,"email2 : " +user2 );
        String username1 = "",username2 = "";
        for (User user : userList){
            if (user1.equals(user.getEmail())){
                //Log.i(TAG,"name1 : " +user.getUserName() );
                if(user.getUserName().equals("")){
                    username1 = user.getEmail();
                }else {
                    username1 = user.getUserName();
                }
            }else if(user2.equals(user.getEmail())){
               // Log.i(TAG,"name2 : " +user.getUserName() );
                if(user.getUserName().equals("")){
                    username2 = user.getEmail();
                }else {
                    username2 = user.getUserName();
                }
            }
        }
        String result = "The Space of "+ username1 +" and " + username2 ;
        return result;
    }
    private void setUpEnvironment(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        spaceDatabaseReference = database.getReference("Spaces");
        userDatabaseReference = database.getReference("User");
        spaceList = new ArrayList<>();
        userList = new ArrayList<>();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CurrentSpace.this, Login.class);
        Pair[] pairs = new Pair[2];
        pairs[0] = new Pair<View, String>(welcomeTV, "logo_text");
        pairs[1] = new Pair<View, String>(sloganTV, "slogan_text");
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(CurrentSpace.this, pairs);
        //startActivity(intent, options.toBundle());
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }

}
