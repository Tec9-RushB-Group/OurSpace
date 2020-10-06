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

public class Login extends AppCompatActivity {


    private static final int RC_SIGN_IN = 123;
    String TAG = "Login";

    private Button googleSignButton, signInButton, signOutButton, newUserButton, forgetPasswordButton, createSpaceButton;
    private TextView welcomeTV, continueTV, usernameTV;
    private TextInputLayout email, password;
    private ImageViewHelper profileImage;
    //spaces list
    private ListView spaceListView;
    private String currentUserEmail;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase database;
    private DatabaseReference spaceDatabaseReference, userDatabaseReference;
    private List<User> userList;
    private List<Space> spaceList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        spaceListView = findViewById(R.id.list_view_spaces);
        // initialize environment
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        spaceDatabaseReference = database.getReference("Spaces");
        userDatabaseReference = database.getReference("User");
        spaceList = new ArrayList<>();
        userList = new ArrayList<>();

        // already signed in
        if (firebaseUser != null) {
            boolean emailVerified = firebaseUser.isEmailVerified();
            Log.i(TAG, "isEmailVerified: " + emailVerified);
            //go to verifyEmail Screen
            if (!emailVerified) {
                Intent intent = new Intent(Login.this, VerifyEmail.class);
                startActivity(intent);
            }
            //go to dashboard
            else {
                setContentView(R.layout.activity_logedin);
                welcomeTV = findViewById(R.id.welcome_text);
                welcomeTV.setTypeface(Typeface.createFromAsset(getAssets(), "logo.ttf"));
                signOutButton = findViewById(R.id.signout);
                createSpaceButton = findViewById(R.id.create_space);
                profileImage = findViewById(R.id.profile_image);
                usernameTV = findViewById(R.id.display_name);
                usernameTV.setTypeface(Typeface.createFromAsset(getAssets(), "username.otf"));
                Uri uri = firebaseUser.getPhotoUrl();
                //set profile image
                if (uri != null) {
                    String url = uri + "";
                    profileImage.setImageURL(url);
                }
                //set display name
                Log.i(TAG, "firebaseUser.getEmail(): " + firebaseUser.getEmail());


                createSpaceButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Login.this, CreateSpace.class);
                        Pair[] pairs = new Pair[3];
                        pairs[0] = new Pair<View, String>(welcomeTV, "logo_text");
                        pairs[1] = new Pair<View, String>(usernameTV, "slogan_text");
                        pairs[2] = new Pair<View, String>(signOutButton, "sign_in_tran");
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
                        startActivity(intent, options.toBundle());
                    }
                });

                signOutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AuthUI.getInstance()
                                .signOut(Login.this)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    public void onComplete(@NonNull Task<Void> task) {
                                        // user is now signed out
                                        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                        Intent intent = new Intent(Login.this, Login.class);
                                        Pair[] pairs = new Pair[3];
                                        pairs[0] = new Pair<View, String>(welcomeTV, "logo_text");
                                        pairs[1] = new Pair<View, String>(usernameTV, "slogan_text");
                                        pairs[2] = new Pair<View, String>(signOutButton, "sign_in_tran");
                                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
                                        startActivity(intent, options.toBundle());
                                        finish();
                                    }
                                });
                    }
                });
            }
        }
        // not signed in
        else {
            setContentView(R.layout.activity_login);
            welcomeTV = findViewById(R.id.welcome_text);
            continueTV = findViewById(R.id.continue_text);
            welcomeTV.setTypeface(Typeface.createFromAsset(getAssets(), "logo.ttf"));
            continueTV.setTypeface(Typeface.createFromAsset(getAssets(), "slogan.ttf"));
            googleSignButton = findViewById(R.id.google_sign_button);
            newUserButton = findViewById(R.id.new_user_button);
            email = findViewById(R.id.email);
            password = findViewById(R.id.password);
            signInButton = findViewById(R.id.sign_in_button);
            forgetPasswordButton = findViewById(R.id.forget_button);
            forgetPasswordButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Login.this, ForgetPassword.class);
                    Pair[] pairs = new Pair[5];
                    pairs[0] = new Pair<View, String>(welcomeTV, "logo_text");
                    pairs[1] = new Pair<View, String>(continueTV, "slogan_text");
                    pairs[2] = new Pair<View, String>(email, "email_tran");
                    pairs[3] = new Pair<View, String>(signInButton, "sign_in_tran");
                    pairs[4] = new Pair<View, String>(newUserButton, "sign_up_tran");
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
                    startActivity(intent, options.toBundle());
                }
            });

            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String emailText = email.getEditText().getText().toString();
                    String passwordText = password.getEditText().getText().toString();
                    if (!validateForm()) {
                        return;
                    }
                    signIn(emailText, passwordText);
                }
            });
            newUserButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Login.this, SignUp.class);
                    Pair[] pairs = new Pair[6];
                    pairs[0] = new Pair<View, String>(welcomeTV, "logo_text");
                    pairs[1] = new Pair<View, String>(continueTV, "slogan_text");
                    pairs[2] = new Pair<View, String>(email, "email_tran");
                    pairs[3] = new Pair<View, String>(password, "password_tran");
                    pairs[4] = new Pair<View, String>(signInButton, "sign_in_tran");
                    pairs[5] = new Pair<View, String>(newUserButton, "sign_up_tran");

                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
                    startActivity(intent, options.toBundle());

                }
            });
            googleSignButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<AuthUI.IdpConfig> providers = Arrays.asList(
                            new AuthUI.IdpConfig.GoogleBuilder().build());

                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(providers)
                                    .build(),
                            RC_SIGN_IN);
                    firebaseUser = firebaseAuth.getCurrentUser();

                }
            });

        }

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
        //updateUI(currentUser);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            Log.i(TAG, "requestCode: " + requestCode + "-------resultCode: " + resultCode + "------data: " + data + "response: " + response);
            if (resultCode == RESULT_OK) {
                // Successfully signed in
                if (response.isNewUser()) {
                    String uid = userDatabaseReference.push().getKey();
                    User user = new User(firebaseUser.getEmail(), firebaseUser.getDisplayName());
                    userDatabaseReference.child(uid).setValue(user);
                    Log.i(TAG, "update google user");
                }
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                Intent intent = new Intent(Login.this, Login.class);
                Pair[] pairs = new Pair[5];
                pairs[0] = new Pair<View, String>(welcomeTV, "logo_text");
                pairs[1] = new Pair<View, String>(continueTV, "slogan_text");
                pairs[2] = new Pair<View, String>(email, "email_tran");
                pairs[3] = new Pair<View, String>(signInButton, "sign_in_tran");
                pairs[4] = new Pair<View, String>(newUserButton, "sign_up_tran");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
                startActivity(intent, options.toBundle());
                //Toast.makeText(Login.this, "successfully signed in! New User? : " + response.isNewUser(), Toast.LENGTH_SHORT).show();
                finish();
            } else {
                //Toast.makeText(Login.this, "sign in failed", Toast.LENGTH_SHORT).show();

            }
        }
    }


    private boolean validateForm() {
        boolean valid = true;
        String regex = "\\w+@\\w+(\\.[a-zA-z]+)+";
        TextInputLayout emailField = findViewById(R.id.email);
        String emailText = email.getEditText().getText().toString();
        if (TextUtils.isEmpty(emailText)) {
            emailField.setError("Required.");
            valid = false;
        } else if (!emailText.matches(regex)) {
            emailField.setError("Invalid Email address.");
            valid = false;
        } else {
            emailField.setError(null);
        }

        TextInputLayout passwordField = findViewById(R.id.password);
        String passwordText = password.getEditText().getText().toString();

        if (TextUtils.isEmpty(passwordText)) {
            passwordField.setError("Required.");
            valid = false;
        } else {
            passwordField.setError(null);
        }

        return valid;
    }

    private boolean isCurrentUsersSpace(Space space) {
        if (space.getUser1().equals(currentUserEmail) || space.getUser2().equals(currentUserEmail)) {
            return true;
        }
        return false;
    }


    private void signIn(String email, String password) {


        // [START sign_in_with_email]
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithEmail:failure", task.getException());
                            updateUI(null);

                        }

                        // [START_EXCLUDE]

                    }
                });
        // [END sign_in_with_email]
    }

    private void updateUI(FirebaseUser user) {
        TextInputLayout passwordField = findViewById(R.id.password);
        TextInputLayout emailField = findViewById(R.id.email);
        if (user != null) {
            Intent intent = new Intent(Login.this, Login.class);
            Pair[] pairs = new Pair[5];
            pairs[0] = new Pair<View, String>(welcomeTV, "logo_text");
            pairs[1] = new Pair<View, String>(continueTV, "slogan_text");
            pairs[2] = new Pair<View, String>(email, "email_tran");
            pairs[3] = new Pair<View, String>(signInButton, "sign_in_tran");
            pairs[4] = new Pair<View, String>(newUserButton, "sign_up_tran");
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            emailField.setError("LogIn filed (Invalid email/password)");
            passwordField.setError("LogIn filed (Invalid email/password)");
        }
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