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
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class CreateSpace extends AppCompatActivity {
    private TextView welcomeTV;
    private Button backButton, inviteButton;
    private EditText getEmail;
    private FirebaseAuth auth;
    private FirebaseUser user;
    String TAG = "CreateSpace";

    DatabaseReference databaseSpaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_space);

        databaseSpaces = FirebaseDatabase.getInstance().getReference("Spaces");
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        welcomeTV = findViewById(R.id.welcome_text);
        getEmail = findViewById(R.id.get_email);
        backButton = findViewById(R.id.back_button);
        inviteButton = findViewById(R.id.invite_button);

        welcomeTV.setTypeface(Typeface.createFromAsset(getAssets(),"logo.ttf"));

        //for "Back to Main Botton".
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateSpace.this,Login.class);
                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View,String>(welcomeTV,"logo_text");
                pairs[1] = new Pair<View,String>(backButton,"sign_up_tran");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(CreateSpace.this,pairs);
                startActivity(intent,options.toBundle());
            }
        });

        //for edit text and inviteButton.
        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_email();
            }
        });
    }
    private void search_email(){
        String user1 = user.getEmail();
        String user2 = getEmail.getText().toString();

        if(!TextUtils.isEmpty(user2)){

            String id = databaseSpaces.push().getKey();
            Space space = new Space(user1, user2, "./", "Test_space", true, true, true);
            databaseSpaces.child(id).setValue(space);

            Toast.makeText(CreateSpace.this, "Space added", Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(CreateSpace.this, "You should enter an email!", Toast.LENGTH_LONG).show();
        }
    }
}
