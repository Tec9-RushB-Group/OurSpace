package com.tec9rushbgroup.ourspace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class Login extends AppCompatActivity {

    private static int SPLASH_SCREEN = 4000;
    private FirebaseAuth auth;
    private static final int RC_SIGN_IN = 123;
    private TextView logo,slogan;
    private Animation topAnim,bottomAnim;
    String TAG = "onActivityResult";
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // already signed in
            setContentView(R.layout.activity_logedin);
            b = findViewById(R.id.signout);
            b.setOnClickListener(new View.OnClickListener() {
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

        } else {
            // not signed in
            setContentView(R.layout.activity_login);
            b = findViewById(R.id.b1);
            b.setOnClickListener(new View.OnClickListener() {
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



        //test
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
                Toast.makeText(Login.this, "successfully signed in! New User? : " + response.isNewUser(), Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(Login.this, "sign in failed", Toast.LENGTH_SHORT).show();

            }
        }
    }
}