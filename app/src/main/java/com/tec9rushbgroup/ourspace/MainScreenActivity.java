package com.tec9rushbgroup.ourspace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainScreenActivity extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String name = user.getDisplayName();

    String[] firstName = name.split(" ");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        ((TextView)findViewById(R.id.hello)).setText(getString(R.string.welcome_message,firstName));
    }

    public void onClick(View v){
        Intent i = new Intent(MainScreenActivity.this, SpaceActivity.class);
        startActivity(i);
    }

    public void createSpace(View v){
        Intent i = new Intent(MainScreenActivity.this, CreateSpace.class);
        startActivity(i);
    }


}