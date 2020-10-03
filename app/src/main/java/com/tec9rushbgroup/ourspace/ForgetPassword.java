package com.tec9rushbgroup.ourspace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {
    private TextView welcomeTV,forgetTV;
    private TextInputLayout email;
    private Button sendButton,backButton;
    private FirebaseAuth auth;
    String TAG = "ForgetPassword";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        auth = FirebaseAuth.getInstance();
        welcomeTV = findViewById(R.id.welcome_text);
        forgetTV = findViewById(R.id.forget_password_text);
        email = findViewById(R.id.email);
        sendButton = findViewById(R.id.send_button);
        backButton = findViewById(R.id.back_button);


        welcomeTV.setTypeface(Typeface.createFromAsset(getAssets(),"logo.ttf"));
        forgetTV.setTypeface(Typeface.createFromAsset(getAssets(),"slogan.ttf"));

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailText = email.getEditText().getText().toString();
                if (!validateForm()) {
                    return;
                }
                auth.sendPasswordResetEmail(emailText)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    email.setEnabled(false);
                                    sendButton.setEnabled(false);
                                    sendButton.setText("Email sent");
                                    sendButton.setBackgroundColor(Color.parseColor("#EEEEEE"));

                                }else{
                                    email.setError("Send filed (Invalid email/User does not exist)");
                                }
                            }
                        });
            }

        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgetPassword.this,Login.class);
                Pair[] pairs = new Pair[5];
                pairs[0] = new Pair<View,String>(welcomeTV,"logo_text");
                pairs[1] = new Pair<View,String>(forgetTV,"slogan_text");
                pairs[2] = new Pair<View,String>(email,"email_tran");
                pairs[3] = new Pair<View,String>(sendButton,"sign_in_tran");
                pairs[4] = new Pair<View,String>(backButton,"sign_up_tran");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ForgetPassword.this,pairs);
                startActivity(intent,options.toBundle());
            }

        });


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

        return valid;
    }
}