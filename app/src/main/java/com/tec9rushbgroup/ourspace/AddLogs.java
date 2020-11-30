package com.tec9rushbgroup.ourspace;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Types.NULL;

public class AddLogs extends AppCompatActivity {
    private Button addLog, backToLogs;
    private TextView welcomeTV, sloganTV;
    private TextInputLayout title, content;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase database;
    private DatabaseReference spaceDatabaseReference, userDatabaseReference;
    private List<User> userList;
    private List<Space> spaceList;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private String fileName;
    private Uri file;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_logs);
        setUpEnvironment();

        // set up TV
        welcomeTV = findViewById(R.id.welcome_text);
        sloganTV = findViewById(R.id.slogan_text);
        welcomeTV.setTypeface(Typeface.createFromAsset(getAssets(), "logo.ttf"));
        sloganTV.setTypeface(Typeface.createFromAsset(getAssets(), "slogan.ttf"));

        // text variable need to pass.
        title = findViewById(R.id.log_title);
        content = findViewById(R.id.log_content);
        //Buttons
        addLog = findViewById(R.id.add_log_button);
        backToLogs = findViewById(R.id.back_to_logs);

        String uid = getIntent().getStringExtra("uid");
        String user1 = getIntent().getStringExtra("user1");
        String user2 = getIntent().getStringExtra("user2");
        backToLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddLogs.this, LogsPage.class);
                intent.putExtra("uid",uid);
                intent.putExtra("user1",user1);
                intent.putExtra("user2",user2);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        });

        addLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog pd = new ProgressDialog(AddLogs.this);
                pd.setTitle("Please wait...");
                pd.setMessage("Creating Log...");
                pd.show();
                addLog.setEnabled(false);
                backToLogs.setEnabled(false);
                title.setEnabled(false);
                content.setEnabled(false);
                //create a new text file to LOCAL.
                String titleText = title.getEditText().getText().toString();
                String contentText = content.getEditText().getText().toString();
                String LUid = userDatabaseReference.push().getKey();
                if (LUid == null){
                    pd.dismiss();
                    addLog.setEnabled(true);
                    backToLogs.setEnabled(true);
                    title.setEnabled(true);
                    content.setEnabled(true);
                    title.setError("Network Error");
                    content.setError("Network Error");
                    return;
                }
                Log log = new Log(titleText,contentText,getIntent().getStringExtra("uid"),LUid);
                database = FirebaseDatabase.getInstance();
                database.getReference("Logs").child(LUid).setValue(log);
                database = FirebaseDatabase.getInstance();
                spaceDatabaseReference = database.getReference("Spaces");
                int num = getNumOfLogs() +1;
                spaceDatabaseReference.child(getIntent().getStringExtra("uid")+"/numOfLogs").setValue(num);

                pd.dismiss();
                String uid = getIntent().getStringExtra("uid");
                String user1 = getIntent().getStringExtra("user1");
                String user2 = getIntent().getStringExtra("user2");
                Intent intent = new Intent(AddLogs.this, LogsPage.class);
                intent.putExtra("uid",uid);
                intent.putExtra("user1",user1);
                intent.putExtra("user2",user2);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();

            }
        });
    }
    @Override
    public void onBackPressed() {
        String uid = getIntent().getStringExtra("uid");
        String user1 = getIntent().getStringExtra("user1");
        String user2 = getIntent().getStringExtra("user2");
        Intent intent = new Intent(AddLogs.this, LogsPage.class);
        intent.putExtra("uid",uid);
        intent.putExtra("user1",user1);
        intent.putExtra("user2",user2);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }


    private void setUpEnvironment(){
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        spaceDatabaseReference = database.getReference("Spaces");
        userDatabaseReference = database.getReference("User");
        spaceList = new ArrayList<>();
        userList = new ArrayList<>();
    }

    private int getNumOfLogs() {
        if (firebaseUser.getEmail() != null) {
            for (Space u : spaceList) {
                if (u.getSpaceUid().equals(getIntent().getStringExtra("uid"))) {
                    return u.getNumOfLogs();
                }
            }
        }
        return NULL;
    }

}