package com.tec9rushbgroup.ourspace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static java.sql.Types.NULL;

public class LogsPage extends AppCompatActivity {

    private Button addButton, backToSpace;
    private ListView logsListView;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase database;
    private DatabaseReference spaceDatabaseReference, userDatabaseReference, listViewReference;
    private List<User> userList;
    private List<Space> spaceList;
    private List<Log> logList;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private String currentUserEmail;
    private TextView welcomeTV, sloganTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs_page);
        welcomeTV = findViewById(R.id.welcome_text);
        sloganTV = findViewById(R.id.slogan_text);
        welcomeTV.setTypeface(Typeface.createFromAsset(getAssets(), "logo.ttf"));
        sloganTV.setTypeface(Typeface.createFromAsset(getAssets(), "slogan.ttf"));
        //BUTTON
        addButton = findViewById(R.id.add_log_page);
        backToSpace = findViewById(R.id.back_to_space);
        setUpEnvironment();
        String uid = getIntent().getStringExtra("uid");
        String user1 = getIntent().getStringExtra("user1");
        String user2 = getIntent().getStringExtra("user2");

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogsPage.this, AddLogs.class);
                intent.putExtra("uid", uid);
                intent.putExtra("user1", user1);
                intent.putExtra("user2", user2);
                intent.putExtra("num", spaceList.get(0).getNumOfLogs());
                //startActivity(intent, options.toBundle());
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        backToSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogsPage.this, CurrentSpace.class);
                intent.putExtra("uid", uid);
                intent.putExtra("user1", user1);
                intent.putExtra("user2", user2);
                //startActivity(intent, options.toBundle());
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        String uid = getIntent().getStringExtra("uid");
        String user1 = getIntent().getStringExtra("user1");
        String user2 = getIntent().getStringExtra("user2");
        Intent intent = new Intent(LogsPage.this, CurrentSpace.class);
        intent.putExtra("uid", uid);
        intent.putExtra("user1", user1);
        intent.putExtra("user2", user2);
        //startActivity(intent, options.toBundle());
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
    @Override
    public void onStart() {
        super.onStart();
        database = FirebaseDatabase.getInstance();
        database.getReference("Logs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                logList.clear();
                if (firebaseUser != null) {
                    currentUserEmail = firebaseUser.getEmail();
                }
                for (DataSnapshot spaceSnapshot : snapshot.getChildren()) {
                    Log log = spaceSnapshot.getValue(Log.class);
                    if (firebaseUser != null) {
                        if (isCurrentUsersLog(log)) {
                            logList.add(log);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }

        });

        listViewReference = spaceDatabaseReference.child(getIntent().getStringExtra("uid"));
        listViewReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                spaceList.clear();
                Space s = snapshot.getValue(Space.class);
                spaceList.add(s);
                logsListView = findViewById(R.id.list_view_logs);
                if (logsListView != null) {
                    String uid = getIntent().getStringExtra("uid");
                    String user1 = getIntent().getStringExtra("user1");
                    String user2 = getIntent().getStringExtra("user2");
                    LogList2 adapter = new LogList2(LogsPage.this, logList, uid,user1,user2,s.getNumOfLogs());
                    logsListView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }

        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateView();
            }
        }, 800);


    }
    private void updateView(){
        logsListView = findViewById(R.id.list_view_logs);
        if (logsListView != null) {
            String uid = getIntent().getStringExtra("uid");
            String user1 = getIntent().getStringExtra("user1");
            String user2 = getIntent().getStringExtra("user2");
            LogList2 adapter = new LogList2(LogsPage.this, logList, uid,user1,user2,spaceList.get(0).getNumOfLogs());
            logsListView.setAdapter(adapter);
        }
    }

    private boolean isCurrentUsersSpace(Space space) {
        if (space.getUser1().equals(currentUserEmail) || space.getUser2().equals(currentUserEmail)) {
            return true;
        }
        return false;
    }
    private boolean isCurrentUsersLog(Log log) {
        if (log.getSpaceUID().equals(getIntent().getStringExtra("uid"))) {
            return true;
        }
        return false;
    }

    private void setUpEnvironment() {
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        spaceDatabaseReference = database.getReference("Spaces");
        userDatabaseReference = database.getReference("User");
        spaceList = new ArrayList<>();
        userList = new ArrayList<>();
        logList = new ArrayList<>();
    }


}