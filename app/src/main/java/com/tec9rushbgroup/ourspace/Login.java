package com.tec9rushbgroup.ourspace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class Login extends AppCompatActivity {


    private FirebaseAuth auth;
    private static final int RC_SIGN_IN = 123;
    String TAG = "onActivityResult";
    private FirebaseUser user;
    private Button googleSignButton,singInButton,signOutButton,newUserButton;
    private TextView welcomeTV,continueTV;
    private TextInputLayout username,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        auth = FirebaseAuth.getInstance();
        // already signed in
        if (auth.getCurrentUser() != null) {
            setContentView(R.layout.activity_logedin);
            signOutButton = findViewById(R.id.signout);
            signOutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AuthUI.getInstance()
                            .signOut(Login.this)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                public void onComplete(@NonNull Task<Void> task) {
                                    // user is now signed out
                                    user = FirebaseAuth.getInstance().getCurrentUser();
                                    startActivity(new Intent(Login.this, Login.class));
                                    finish();
                                }
                            });
                }
            });

        }
        // not signed in
        else {
            setContentView(R.layout.activity_login);

            welcomeTV = findViewById(R.id.welcome_text);
            continueTV = findViewById(R.id.continue_text);
            welcomeTV.setTypeface(Typeface.createFromAsset(getAssets(),"logo.ttf"));
            continueTV.setTypeface(Typeface.createFromAsset(getAssets(),"slogan.ttf"));
            googleSignButton = findViewById(R.id.google_sign_button);
            newUserButton = findViewById(R.id.new_user_button);
            username = findViewById(R.id.username);
            password = findViewById(R.id.password);
            singInButton = findViewById(R.id.sign_in_button);

            newUserButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Login.this,SignUp.class);
                    Pair[] pairs = new Pair[6];
                    pairs[0] = new Pair<View,String>(welcomeTV,"logo_text");
                    pairs[1] = new Pair<View,String>(continueTV,"slogan_text");
                    pairs[2] = new Pair<View,String>(username,"username_tran");
                    pairs[3] = new Pair<View,String>(password,"password_tran");
                    pairs[4] = new Pair<View,String>(singInButton,"sign_in_tran");
                    pairs[5] = new Pair<View,String>(newUserButton,"sign_up_tran");

                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this,pairs);
                    startActivity(intent,options.toBundle());

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
                    user = FirebaseAuth.getInstance().getCurrentUser();
                }
            });

        }

    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        //updateUI(currentUser);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG,"requestCode: "+requestCode+"-------resultCode: "+ resultCode+"------data: "+data + "");
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                user = FirebaseAuth.getInstance().getCurrentUser();
                startActivity(new Intent(Login.this, Login.class));
                //Toast.makeText(Login.this, "successfully signed in! New User? : " + response.isNewUser(), Toast.LENGTH_SHORT).show();
                finish();
            } else {
                //Toast.makeText(Login.this, "sign in failed", Toast.LENGTH_SHORT).show();

            }
        }
    }
}