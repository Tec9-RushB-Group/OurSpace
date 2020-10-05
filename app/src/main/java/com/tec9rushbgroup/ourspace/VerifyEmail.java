package com.tec9rushbgroup.ourspace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VerifyEmail extends AppCompatActivity {
    private FirebaseAuth auth;
    private TextView welcomeTV,continueTV;
    private Button sendVerificationButton,verifiedButton;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_verify_email);
        welcomeTV = findViewById(R.id.welcome_text);
        continueTV = findViewById(R.id.verify_text);
        welcomeTV.setTypeface(Typeface.createFromAsset(getAssets(),"logo.ttf"));
        continueTV.setTypeface(Typeface.createFromAsset(getAssets(),"slogan.ttf"));
        sendVerificationButton = findViewById(R.id.send_verification_button);
        verifiedButton = findViewById(R.id.verified_button);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        sendVerificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.sendEmailVerification()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    sendVerificationButton.setEnabled(false);
                                    sendVerificationButton.setText("Email sent");
                                    sendVerificationButton.setBackgroundColor(Color.parseColor("#EEEEEE"));

                                }else{
                                    sendVerificationButton.setText("Unknown Error. Send Again");
                                }
                            }
                        });
            }
        });

        verifiedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance()
                        .signOut(VerifyEmail.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // user is now signed out
                                user = FirebaseAuth.getInstance().getCurrentUser();
                                Intent intent = new Intent(VerifyEmail.this,Login.class);
                                Pair[] pairs = new Pair[4];
                                pairs[0] = new Pair<View,String>(welcomeTV,"logo_text");
                                pairs[1] = new Pair<View,String>(continueTV,"slogan_text");
                                pairs[2] = new Pair<View,String>(sendVerificationButton,"sign_in_tran");
                                pairs[3] = new Pair<View,String>(verifiedButton,"sign_up_tran");
                                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(VerifyEmail.this,pairs);
                                startActivity(intent,options.toBundle());
                                finish();
                            }
                        });

            }
        });




    }
}