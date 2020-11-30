package com.tec9rushbgroup.ourspace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
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

public class AnniversaryPage extends AppCompatActivity {

    private Button addButton, backToSpace;
    private TextView welcomeTV, sloganTV;
    private ListView listView;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase database;
    private DatabaseReference spaceDatabaseReference, userDatabaseReference,listViewReference;
    private String currentUserEmail;
    private List<User> userList;
    private List<Anniversary> anniversaryList;
    private List<Space> spaceList;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private Integer numOfAnniversaries;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anniversary_page);
        setUpEnvironment();
        welcomeTV = findViewById(R.id.welcome_text);
        sloganTV = findViewById(R.id.slogan_text);
        welcomeTV.setTypeface(Typeface.createFromAsset(getAssets(), "logo.ttf"));
        sloganTV.setTypeface(Typeface.createFromAsset(getAssets(), "slogan.ttf"));
        //BUTTON
        addButton = findViewById(R.id.add_anniversary_page);
        backToSpace = findViewById(R.id.back_to_space_button);
        String uid = getIntent().getStringExtra("uid");
        String user1 = getIntent().getStringExtra("user1");
        String user2 = getIntent().getStringExtra("user2");
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AnniversaryPage.this, AddAnniversary.class);
                intent.putExtra("uid",uid);
                intent.putExtra("user1",user1);
                intent.putExtra("user2",user2);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        });

        backToSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AnniversaryPage.this, CurrentSpace.class);
                intent.putExtra("uid",uid);
                intent.putExtra("user1",user1);
                intent.putExtra("user2",user2);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        });
    }

    private void updateListView2() {
        String uid = getIntent().getStringExtra("uid");
        String user1 = getIntent().getStringExtra("user1");
        String user2 = getIntent().getStringExtra("user2");


        AnniversaryList2 adapter = new AnniversaryList2(AnniversaryPage.this, anniversaryList, uid,user1,user2,anniversaryList.size());
        listView = findViewById(R.id.list_view_anniversary);
        listView.setAdapter(adapter);
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
        anniversaryList = new ArrayList<>();
    }

    @Override
    public void onStart() {
        super.onStart();
        database.getReference("Anniversaries").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                anniversaryList.clear();
                if (firebaseUser != null) {
                    currentUserEmail = firebaseUser.getEmail();
                }
                for (DataSnapshot spaceSnapshot : snapshot.getChildren()) {
                    Anniversary anniversary = spaceSnapshot.getValue(Anniversary.class);
                    if (firebaseUser != null) {
                        if (isCurrentUsersAnniversary(anniversary)) {
                            anniversaryList.add(anniversary);
                        }
                    }
                }
                updateListView2();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }

        });


        database = FirebaseDatabase.getInstance();
        spaceDatabaseReference = database.getReference("Spaces");
        listViewReference = spaceDatabaseReference.child(getIntent().getStringExtra("uid"));
        listViewReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                listView = findViewById(R.id.list_view_anniversary);
                if (listView != null) {
                    updateListView2();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }

        });
    }


    private boolean isCurrentUsersAnniversary(Anniversary anniversary) {
        String uid = getIntent().getStringExtra("uid");
        if (anniversary.getSpaceUID().equals(uid)) {
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        String uid = getIntent().getStringExtra("uid");
        String user1 = getIntent().getStringExtra("user1");
        String user2 = getIntent().getStringExtra("user2");
        Intent intent = new Intent(AnniversaryPage.this, CurrentSpace.class);
        intent.putExtra("uid",uid);
        intent.putExtra("user1",user1);
        intent.putExtra("user2",user2);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }
}