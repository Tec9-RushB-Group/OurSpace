package com.tec9rushbgroup.ourspace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class CreateSpace extends AppCompatActivity {
    private TextView welcomeTV,sloganTV;
    private Button backButton, inviteButton;
    private TextInputLayout email, spaceName;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    String TAG = "CreateSpace";
    private FirebaseDatabase database;
    private DatabaseReference databaseReference,databaseReference2;
    private List<Space> spaceList;
    private String currentUserEmail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_space);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        //For space list
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Spaces");
        databaseReference2 = database.getReference("User");

        spaceList = new ArrayList<>();

        welcomeTV = findViewById(R.id.welcome_text);
        sloganTV = findViewById(R.id.create_text);
        email = findViewById(R.id.email);
        backButton = findViewById(R.id.back_button);
        inviteButton = findViewById(R.id.invite_button);
        spaceName = findViewById(R.id.space_name);

        welcomeTV.setTypeface(Typeface.createFromAsset(getAssets(), "logo.ttf"));
        sloganTV.setTypeface(Typeface.createFromAsset(getAssets(),"slogan.ttf"));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateSpace.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validateForm()) {
                    currentUserEmail =firebaseUser.getEmail();
                    String user2 = email.getEditText().getText().toString();
                    String space_name = spaceName.getEditText().getText().toString();

                    if (isAbleToCreateSpace(user2,spaceList,space_name)){
                        String uid = databaseReference.push().getKey();
                        Space space = new Space(uid, currentUserEmail, user2, "./", space_name, true, true, true);
                        databaseReference.child(uid).setValue(space);
                        Toast.makeText(CreateSpace.this, "Space added", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(CreateSpace.this, Login.class);
                        startActivity(intent);
                        finish();
                    }

                }
            }
        });
    }


    private boolean isAbleToCreateSpace(String emailText,List<Space> spaceList, String name){
        boolean result = true;
        if (currentUserEmail.equals(emailText)){
            email.setError("You can not create space with yourself!");
            result = false;
        }
        for (Space space: spaceList){
            if(currentUserEmail.equals(space.user1)||currentUserEmail.equals(space.user2)){
                if (space.getName().equals(name)){
                    spaceName.setError("You already have a space with this name!");
                    result = false;
                }
                if ((currentUserEmail.equals(space.user1) && emailText.equals(space.user2)) ||
                        (currentUserEmail.equals(space.user2) && emailText.equals(space.user1)) ){
                    email.setError("You can only have one space with this user!");
                    result = false;
                }
            }
        }
        return result;
    }

    @Override
    protected void onStart() {
        super.onStart();

        currentUserEmail = firebaseUser.getEmail();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                spaceList.clear();

                for (DataSnapshot spaceSnapshot: snapshot.getChildren()){
                    Space space = spaceSnapshot.getValue(Space.class);
                    spaceList.add(space);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }


    private boolean validateForm() {
        boolean valid = true;
        String regex = "\\w+@\\w+(\\.[a-zA-z]+)+";
        String emailText = email.getEditText().getText().toString();
        if (TextUtils.isEmpty(emailText)) {
            email.setError("Required.");
            valid = false;
        } else if (!emailText.matches(regex)) {
            email.setError("Invalid Email address.");
            valid = false;
        } else {
            email.setError(null);
        }

        String spaceNameText = spaceName.getEditText().getText().toString();
        if (TextUtils.isEmpty(spaceNameText)) {
            spaceName.setError("Required.");
            valid = false;
        } else {
            spaceName.setError(null);
        }

        return valid;
    }

}
