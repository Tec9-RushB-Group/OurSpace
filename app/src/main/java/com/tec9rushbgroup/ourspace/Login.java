package com.tec9rushbgroup.ourspace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Typeface;
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

import java.util.Arrays;
import java.util.List;

public class Login extends AppCompatActivity {


    private FirebaseAuth auth;
    private static final int RC_SIGN_IN = 123;
    String TAG = "Login";
    private FirebaseUser user;
    private Button googleSignButton,signInButton,signOutButton,newUserButton;
    private TextView welcomeTV,continueTV;
    private TextInputLayout email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        auth = FirebaseAuth.getInstance();
        // already signed in
        if (auth.getCurrentUser() != null) {
            user = auth.getCurrentUser();
            //setContentView(R.layout.activity_logedin);
            Intent i = new Intent(this, MainScreenActivity.class);
            startActivity(i);


            /*
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
            */

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
            email = findViewById(R.id.email);
            password = findViewById(R.id.password);
            signInButton = findViewById(R.id.sign_in_button);
            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String emailText = email.getEditText().getText().toString();
                    String passwordText = password.getEditText().getText().toString();
                    if (!validateForm()) {
                        return;
                    }
                    signIn(emailText,passwordText);
                }
            });
            newUserButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Login.this,SignUp.class);
                    Pair[] pairs = new Pair[6];
                    pairs[0] = new Pair<View,String>(welcomeTV,"logo_text");
                    pairs[1] = new Pair<View,String>(continueTV,"slogan_text");
                    pairs[2] = new Pair<View,String>(email,"email_tran");
                    pairs[3] = new Pair<View,String>(password,"password_tran");
                    pairs[4] = new Pair<View,String>(signInButton,"sign_in_tran");
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


    private boolean validateForm() {
        boolean valid = true;
        String regex = "\\w+@\\w+(\\.[a-zA-z]+)+";
        TextInputLayout emailField = findViewById(R.id.email);
        String emailText = email.getEditText().getText().toString();
        if (TextUtils.isEmpty(emailText)) {
            emailField.setError("Required.");
            valid = false;
        }else if(!emailText.matches(regex)){
            emailField.setError("Invalid Email address.");
            valid = false;
        }
        else {
            emailField.setError(null);
        }

        TextInputLayout passwordField = findViewById(R.id.password);
        String passwordText = password.getEditText().getText().toString();

        if (TextUtils.isEmpty(passwordText)) {
            passwordField.setError("Required.");
            valid = false;
        }
        else {
            passwordField.setError(null);
        }

        return valid;
    }
    private void signIn(String email, String password) {


        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
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
            Intent intent = new Intent(Login.this,Login.class);
            startActivity(intent);
        }else{
            emailField.setError("LogIn filed (Invalid email/password)");
            passwordField.setError("LogIn filed (Invalid email/password)");
        }
    }

}