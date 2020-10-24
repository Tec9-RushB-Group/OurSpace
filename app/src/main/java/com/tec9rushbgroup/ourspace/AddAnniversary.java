package com.tec9rushbgroup.ourspace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AddAnniversary extends AppCompatActivity {
    private Button addAnniversary, backToAnniversaries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_anniversary);

        //Buttons
        addAnniversary = findViewById(R.id.add_anniversary_button);
        backToAnniversaries = findViewById(R.id.back_to_anniversaries);

        backToAnniversaries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddAnniversary.this, AnniversaryPage.class);
                startActivity(intent);
            }
        });
    }
}