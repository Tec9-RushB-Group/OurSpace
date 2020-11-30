package com.tec9rushbgroup.ourspace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
    private void updateListView() {

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("Space/" + getIntent().getStringExtra("uid") + "/Anniversary");
        storageReference.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        String uid = getIntent().getStringExtra("uid");
                        String user1 = getIntent().getStringExtra("user1");
                        String user2 = getIntent().getStringExtra("user2");
                        List<StorageReference> items = listResult.getItems();
                        AnniversaryList adapter = new AnniversaryList(AnniversaryPage.this, items, uid,user1,user2,getNumOfAnniversaries());
                        listView = findViewById(R.id.list_view_anniversary);
                        listView.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                    }
                });
    }
    private int getNumOfAnniversaries() {
        if (firebaseUser.getEmail() != null) {
            for (Space u : spaceList) {
                if (u.getSpaceUid().equals(getIntent().getStringExtra("uid"))) {
                    return u.getNumOfAnniversaries();
                }
            }
        }
        return NULL;
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

    @Override
    public void onStart() {
        super.onStart();
        listView = findViewById(R.id.list_view_anniversary);
        if (listView != null) {
            updateListView();
        }
        database = FirebaseDatabase.getInstance();
        spaceDatabaseReference = database.getReference("Spaces");
        listViewReference = spaceDatabaseReference.child(getIntent().getStringExtra("uid"));
        listViewReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                listView = findViewById(R.id.list_view_anniversary);
                if (listView != null) {
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