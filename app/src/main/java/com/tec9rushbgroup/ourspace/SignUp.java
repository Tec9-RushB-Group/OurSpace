package com.tec9rushbgroup.ourspace;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {
    private FirebaseAuth auth;
    private TextView welcomeTV,signUpTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        auth = FirebaseAuth.getInstance();
        welcomeTV = findViewById(R.id.welcome_text);
        signUpTV = findViewById(R.id.sign_up_text);
        welcomeTV.setTypeface(Typeface.createFromAsset(getAssets(),"logo.ttf"));
        signUpTV.setTypeface(Typeface.createFromAsset(getAssets(),"slogan.ttf"));
    }
}