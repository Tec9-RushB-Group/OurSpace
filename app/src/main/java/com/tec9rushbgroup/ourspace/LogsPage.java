package com.tec9rushbgroup.ourspace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
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

    private void updateListView() {

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("Space/" + getIntent().getStringExtra("uid") + "/Logs");
        storageReference.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        String uid = getIntent().getStringExtra("uid");
                        String user1 = getIntent().getStringExtra("user1");
                        String user2 = getIntent().getStringExtra("user2");
                        List<StorageReference> items = listResult.getItems();
                        LogList adapter = new LogList(LogsPage.this, items, uid,getNumOfLogs(),user1,user2);
                        logsListView = findViewById(R.id.list_view_logs);
                        logsListView.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                    }
                });
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

        // Check if user is signed in (non-null) and update UI accordingly.
        spaceDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                spaceList.clear();
                if (firebaseUser != null) {
                    currentUserEmail = firebaseUser.getEmail();
                }
                for (DataSnapshot spaceSnapshot : snapshot.getChildren()) {
                    Space space = spaceSnapshot.getValue(Space.class);
                    if (firebaseUser != null) {
                        if (isCurrentUsersSpace(space)) {
                            spaceList.add(space);
                        }
                    } else {
                        spaceList.add(space);
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

                logsListView = findViewById(R.id.list_view_logs);
                if (logsListView != null) {
                    updateListView();
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }

        });
    }

    private boolean isCurrentUsersSpace(Space space) {
        if (space.getUser1().equals(currentUserEmail) || space.getUser2().equals(currentUserEmail)) {
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
    }


}