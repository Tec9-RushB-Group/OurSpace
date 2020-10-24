package com.tec9rushbgroup.ourspace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AddLogs extends AppCompatActivity {
    private Button addLog, backToLogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_logs);

        //Buttons
        addLog = findViewById(R.id.add_log_button);
        backToLogs = findViewById(R.id.back_to_logs);

        backToLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddLogs.this, LogsPage.class);
                startActivity(intent);
            }
        });
    }
}