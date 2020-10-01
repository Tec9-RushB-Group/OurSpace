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
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class SignUp extends AppCompatActivity {
    private FirebaseAuth auth;
    private TextView welcomeTV,signUpTV;
    private Button haveAnAccButton,signUpButton;
    private TextInputLayout password,email,confirm, username;
    String TAG = "SignUp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        auth = FirebaseAuth.getInstance();
        welcomeTV = findViewById(R.id.welcome_text);
        signUpTV = findViewById(R.id.sign_up_text);
        haveAnAccButton = findViewById(R.id.have_an_acc_button);
        signUpButton = findViewById(R.id.sign_up_button);
        password = findViewById(R.id.password);
        //fullName = findViewById(R.id.name);
        email = findViewById(R.id.email);
        confirm = findViewById(R.id.confirm);
        username = findViewById(R.id.username);

        //set fonts
        welcomeTV.setTypeface(Typeface.createFromAsset(getAssets(),"logo.ttf"));
        signUpTV.setTypeface(Typeface.createFromAsset(getAssets(),"slogan.ttf"));


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateForm()) {
                    return;
                }
                String emailText = email.getEditText().getText().toString();
                String passwordText = password.getEditText().getText().toString();
                Log.d(TAG, "email: "+emailText+"   password: "+passwordText);
                createAccount(emailText,passwordText);
            }

        });



        haveAnAccButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this,Login.class);
                Pair[] pairs = new Pair[6];
                pairs[0] = new Pair<View,String>(welcomeTV,"logo_text");
                pairs[1] = new Pair<View,String>(signUpTV,"slogan_text");
                pairs[2] = new Pair<View,String>(email,"email_tran");
                pairs[3] = new Pair<View,String>(password,"password_tran");
                pairs[4] = new Pair<View,String>(signUpButton,"sign_in_tran");
                pairs[5] = new Pair<View,String>(haveAnAccButton,"sign_up_tran");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp.this,pairs);
                startActivity(intent,options.toBundle());
            }
        });

    }



    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        //updateUI(currentUser);
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
        } else if(passwordText.length()<=5){
            passwordField.setError("Password too short.");
            valid = false;
        }
        else {
            passwordField.setError(null);
        }

        TextInputLayout confirmField = findViewById(R.id.confirm);
        String confirmText = confirmField.getEditText().getText().toString();
        if (TextUtils.isEmpty(passwordText)) {
            confirmField.setError("Required.");
            valid = false;
        }
        else if(!TextUtils.equals(passwordText,confirmText)){
            confirmField.setError("Those passwords didn't match.");
            valid = false;
        }
        else {
            confirmField.setError(null);
        }

        return valid;
    }
    private void createAccount(String email, String password) {

        TextInputLayout emailField = findViewById(R.id.email);
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            emailField.setError("Invalid Email address/Existed Email Address.");
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        //hideProgressBar();
                        // [END_EXCLUDE]
                    }
                });
        //Log.w(TAG, "createUserWithEmail:failure", task.getException());
       // [END create_user_with_email]
    }

    private void updateUI(FirebaseUser user) {

        if (user != null) {

            Intent intent = new Intent(SignUp.this,Login.class);
            Pair[] pairs = new Pair[6];
            pairs[0] = new Pair<View,String>(welcomeTV,"logo_text");
            pairs[1] = new Pair<View,String>(signUpTV,"slogan_text");
            pairs[2] = new Pair<View,String>(email,"email_tran");
            pairs[3] = new Pair<View,String>(password,"password_tran");
            pairs[4] = new Pair<View,String>(signUpButton,"sign_in_tran");
            pairs[5] = new Pair<View,String>(haveAnAccButton,"sign_up_tran");

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp.this,pairs);
            startActivity(intent,options.toBundle());

        }
    }


}