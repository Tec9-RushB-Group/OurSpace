package com.tec9rushbgroup.ourspace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Profile  extends AppCompatActivity {

    String TAG = "Profile";
    private TextView welcomeTV, usernameTV;
    private Button changeUsernameButton, changePhotoButton,signOutButton;
    private TextInputLayout email, spaceName;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase database;
    private DatabaseReference spaceDatabaseReference, userDatabaseReference;
    private List<User> userList;
    private List<Space> spaceList;
    private String currentUserEmail;
    private ImageViewHelper profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile);

        // initialize environment
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        spaceDatabaseReference = database.getReference("Spaces");
        userDatabaseReference = database.getReference("User");
        spaceList = new ArrayList<>();
        userList = new ArrayList<>();


        signOutButton= findViewById(R.id.signout);
        changeUsernameButton =findViewById(R.id.change_username);
        changePhotoButton = findViewById(R.id.change_photo);
        welcomeTV = findViewById(R.id.welcome_text);
        profileImage = findViewById(R.id.profile_image);
        usernameTV = findViewById(R.id.display_name);
        usernameTV.setTypeface(Typeface.createFromAsset(getAssets(), "username.otf"));
        welcomeTV.setTypeface(Typeface.createFromAsset(getAssets(), "logo.ttf"));
        Uri uri = firebaseUser.getPhotoUrl();
        //set profile image
        if (uri != null) {
            String url = uri + "";
            profileImage.setImageURL(url);
        }

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance()
                        .signOut(Profile.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // user is now signed out
                                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                Intent intent = new Intent(Profile.this, Login.class);
                                Pair[] pairs = new Pair[3];
                                pairs[0] = new Pair<View, String>(welcomeTV, "logo_text");
                                pairs[1] = new Pair<View, String>(usernameTV, "slogan_text");
                                pairs[2] = new Pair<View, String>(changeUsernameButton, "sign_in_tran");
                                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Profile.this, pairs);
                                startActivity(intent,options.toBundle());
                                overridePendingTransition(0,0);
                            }
                        });
            }
        });

        //initialize
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_bar);
        //set dashboard selected
        bottomNavigationView.setSelectedItemId(R.id.bottom_nav_profile);
        //perform itemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.bottom_nav_dashboard:
                        Intent intent = new Intent(Profile.this, Login.class);
                        Pair[] pairs = new Pair[3];
                        pairs[0] = new Pair<View, String>(welcomeTV, "logo_text");
                        pairs[1] = new Pair<View, String>(usernameTV, "slogan_text");
                        pairs[2] = new Pair<View, String>(changeUsernameButton, "sign_in_tran");
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Profile.this, pairs);
                        startActivity(intent,options.toBundle());
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.bottom_nav_add:
                        Intent intent2 = new Intent(Profile.this, CreateSpace.class);
                        Pair[] pairs2 = new Pair[3];
                        pairs2[0] = new Pair<View, String>(welcomeTV, "logo_text");
                        pairs2[1] = new Pair<View, String>(usernameTV, "slogan_text");
                        pairs2[2] = new Pair<View, String>(changeUsernameButton, "sign_in_tran");
                        ActivityOptions options2 = ActivityOptions.makeSceneTransitionAnimation(Profile.this, pairs2);
                        startActivity(intent2,options2.toBundle());
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.bottom_nav_profile:
                        return true;
                }
                return false;
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
                }
                if (usernameTV != null && firebaseUser != null) {
                    String displayName = findDisplayName(firebaseUser.getEmail());
                    usernameTV.setText(displayName);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }

        });
        /*
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
                    }else{
                        spaceList.add(space);
                    }
                }
                spaceListView = findViewById(R.id.list_view_spaces);
                if (spaceListView != null) {
                    EnterSpacesButtonsList adapter = new EnterSpacesButtonsList(Login.this, spaceList);
                    spaceListView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }

        });

         */

    }

    private String findDisplayName(String emailText) {
        //Log.i(TAG, "findDisplayName()");
        //Log.i(TAG, "findDisplayName(): userList: " + userList.toString());
        for (User user : userList) {
            //Log.i(TAG, "findDisplayName() : for loop");
            if (user.getEmail().equals(emailText)) {
                //Log.i(TAG, "emailText: " + emailText);
                //Log.i(TAG, "user.getEmail(): " + user.getEmail());
                //Log.i(TAG, "user.getUserName(): " + user.getUserName());
                if (user.getUserName() ==null){
                    break;
                }
                return user.getUserName();
            }
        }
        return "No UserName";
    }

}