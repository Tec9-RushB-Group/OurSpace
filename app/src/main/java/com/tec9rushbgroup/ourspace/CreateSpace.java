package com.tec9rushbgroup.ourspace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;


public class CreateSpace extends AppCompatActivity {
    String TAG = "CreateSpace";
    private TextView welcomeTV, sloganTV;
    private Button inviteButton;
    private TextInputLayout email, spaceName;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase database;
    private DatabaseReference spaceDatabaseReference, userDatabaseReference;
    private List<User> userList;
    private List<Space> spaceList;
    private String currentUserEmail;
    private BottomNavigationView bottomNavigationView;
    private FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_space);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // initialize environment
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        spaceDatabaseReference = database.getReference("Spaces");
        userDatabaseReference = database.getReference("User");
        spaceList = new ArrayList<>();
        userList = new ArrayList<>();

        welcomeTV = findViewById(R.id.welcome_text);
        sloganTV = findViewById(R.id.create_text);
        email = findViewById(R.id.email);

        inviteButton = findViewById(R.id.invite_button);
        spaceName = findViewById(R.id.space_name);

        welcomeTV.setTypeface(Typeface.createFromAsset(getAssets(), "logo.ttf"));
        sloganTV.setTypeface(Typeface.createFromAsset(getAssets(), "slogan.ttf"));

        //initialize
        bottomNavigationView = findViewById(R.id.bottom_nav_bar);
        //set dashboard selected
        bottomNavigationView.setSelectedItemId(R.id.bottom_nav_add);
        //perform itemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.bottom_nav_dashboard:
                        Intent intent = new Intent(CreateSpace.this, Login.class);
                        Pair[] pairs = new Pair[3];
                        pairs[0] = new Pair<View, String>(welcomeTV, "logo_text");
                        pairs[1] = new Pair<View, String>(sloganTV, "slogan_text");
                        pairs[2] = new Pair<View, String>(inviteButton, "sign_in_tran");
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(CreateSpace.this, pairs);
                        startActivity(intent, options.toBundle());
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.bottom_nav_profile:
                        Intent intent2 = new Intent(CreateSpace.this, Profile.class);
                        Pair[] pairs2 = new Pair[3];
                        pairs2[0] = new Pair<View, String>(welcomeTV, "logo_text");
                        pairs2[1] = new Pair<View, String>(sloganTV, "slogan_text");
                        pairs2[2] = new Pair<View, String>(inviteButton, "sign_in_tran");
                        ActivityOptions options2 = ActivityOptions.makeSceneTransitionAnimation(CreateSpace.this, pairs2);
                        startActivity(intent2, options2.toBundle());
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.bottom_nav_add:
                        return true;
                }
                return false;
            }
        });

        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validateForm()) {
                    currentUserEmail = firebaseUser.getEmail();
                    String user2 = email.getEditText().getText().toString();
                    String space_name = spaceName.getEditText().getText().toString();

                    if (isAbleToCreateSpace(user2, space_name)) {
                        String uid = spaceDatabaseReference.push().getKey();
                        Space space = new Space(uid, currentUserEmail, user2, "./", space_name, true, true, true);
                        spaceDatabaseReference.child(uid).setValue(space);
                        Intent intent = new Intent(CreateSpace.this, Login.class);
                        Pair[] pairs = new Pair[3];
                        pairs[0] = new Pair<View, String>(welcomeTV, "logo_text");
                        pairs[1] = new Pair<View, String>(sloganTV, "slogan_text");
                        pairs[2] = new Pair<View, String>(inviteButton, "sign_in_tran");
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(CreateSpace.this, pairs);
                        startActivity(intent, options.toBundle());
                        finish();
                    }

                }
            }
        });
    }


    private boolean isAbleToCreateSpace(String emailText, String name) {
        boolean result = true;
        if (currentUserEmail.equals(emailText)) {
            email.setError("You can not create space with yourself!");
            result = false;
        }
        boolean tempResult = false;
        for (User user : userList) {
            if (user.getEmail().equals(emailText)) {
                tempResult = true;
                break;
            }
        }
        if (!tempResult) {
            email.setError("This user does not exist!");
            result = false;
            return result;
        }
        for (Space space : spaceList) {
            if (currentUserEmail.equals(space.user1) || currentUserEmail.equals(space.user2)) {
                if (space.getName().equals(name)) {
                    spaceName.setError("You already have a space with this name!");
                    result = false;
                }
                if ((currentUserEmail.equals(space.user1) && emailText.equals(space.user2)) ||
                        (currentUserEmail.equals(space.user2) && emailText.equals(space.user1))) {
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

        userDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot spaceSnapshot : snapshot.getChildren()) {
                    User user = spaceSnapshot.getValue(User.class);
                    userList.add(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }

        });
        spaceDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                spaceList.clear();

                for (DataSnapshot spaceSnapshot : snapshot.getChildren()) {
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
