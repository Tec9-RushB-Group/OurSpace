package com.tec9rushbgroup.ourspace;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class CreateSpace extends AppCompatActivity {
    private TextView welcomeTV;
    private Button backButton, inviteButton;
    private TextInputLayout getEmail;
    private FirebaseAuth auth;
    private FirebaseUser user;
    String TAG = "CreateSpace";
    private DatabaseReference databaseSpaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_space);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseSpaces = database.getReference();

        welcomeTV = findViewById(R.id.create_space_text);
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
        String user2 = getEmail.getEditText().getText().toString();

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
