package com.tec9rushbgroup.ourspace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

public class DeleteSpace extends AppCompatActivity {
    private FirebaseStorage firebaseStorage;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase database;
    private DatabaseReference spaceDatabaseReference, userDatabaseReference;
    private List<User> userList;
    private List<Space> spaceList;
    private String currentUserEmail;
    private TextView welcomeTV, sloganTV;
    private Button yesButton,noButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_space);
        setUpEnvironment();
        welcomeTV = findViewById(R.id.welcome_text);
        sloganTV = findViewById(R.id.slogan_text);
        welcomeTV.setTypeface(Typeface.createFromAsset(getAssets(), "logo.ttf"));
        sloganTV.setTypeface(Typeface.createFromAsset(getAssets(), "slogan.ttf"));
        yesButton = findViewById(R.id.yes_button);
        noButton = findViewById(R.id.no_button);
        String uid = getIntent().getStringExtra("uid");
        String user1 = getIntent().getStringExtra("user1");
        String user2 = getIntent().getStringExtra("user2");
        yesButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                spaceDatabaseReference.child(uid).setValue(null);
                Intent intent = new Intent(DeleteSpace.this, Login.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });
        noButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(DeleteSpace.this, Login.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });





    }

    private void setUpEnvironment(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        spaceDatabaseReference = database.getReference("Spaces");
        userDatabaseReference = database.getReference("User");
        spaceList = new ArrayList<>();
        userList = new ArrayList<>();
        currentUserEmail = firebaseUser.getEmail();
    }
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
        // Check if user is signed in (non-null) and update UI accordingly.
        spaceDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                spaceList.clear();
                if (firebaseUser!=null){
                    currentUserEmail = firebaseUser.getEmail();
                }
                for (DataSnapshot spaceSnapshot : snapshot.getChildren()) {
                    Space space = spaceSnapshot.getValue(Space.class);
                    if (firebaseUser!=null){
                        if (isCurrentUsersSpace(space)) {
                            spaceList.add(space);
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }

        });
        //updateUI(currentUser);
    }
    private boolean isCurrentUsersSpace(Space space) {
        if (space.getUser1().equals(currentUserEmail) || space.getUser2().equals(currentUserEmail)) {
            return true;
        }
        return false;
    }
    private String getSloganString(){
        String user;
        String user1 = getIntent().getStringExtra("user1");
        String user2 = getIntent().getStringExtra("user2");
        if (currentUserEmail.equals(user1)){
            user = user2;
        }else {
            user = user1;
        }
        //Log.i(TAG,"email1 : " +user1 );
        //Log.i(TAG,"email2 : " +user2 );
        String username = "";
        for (User u : userList){
            if (user1.equals(u.getEmail())){
                //Log.i(TAG,"name1 : " +user.getUserName() );
                if(u.getUserName().equals("")){
                    username = u.getEmail();
                }else {
                    username = u.getUserName();
                }
            }
        }
        String result = "Are you sure to delete the space with " + username ;
        return result;
    }

}