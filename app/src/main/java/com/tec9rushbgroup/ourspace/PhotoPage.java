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
import android.provider.ContactsContract;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.sql.Types.NULL;

public class PhotoPage extends AppCompatActivity {
    String TAG = "PhotoPage";
    private Button addButton, backToSpace;
    private TextView welcomeTV, sloganTV;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase database;
    private DatabaseReference spaceDatabaseReference, userDatabaseReference,photoViewReference;
    private List<User> userList;
    private List<Space> spaceList;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private Uri imageUri;
    private String currentUserEmail;
    private ListView photoListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_page);
        setUpEnvironment();
        //welcomeTV = findViewById(R.id.welcome_text);
        //sloganTV = findViewById(R.id.slogan_text);
        //welcomeTV.setTypeface(Typeface.createFromAsset(getAssets(), "logo.ttf"));
        //sloganTV.setTypeface(Typeface.createFromAsset(getAssets(), "slogan.ttf"));

        //BUTTON
        addButton = findViewById(R.id.add_photo);
        backToSpace = findViewById(R.id.back_to_space);
        photoListView = findViewById(R.id.list_view_photos);
        String uid = getIntent().getStringExtra("uid");
        String user1 = getIntent().getStringExtra("user1");
        String user2 = getIntent().getStringExtra("user2");

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });

        backToSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PhotoPage.this, CurrentSpace.class);
                intent.putExtra("uid",uid);
                intent.putExtra("user1",user1);
                intent.putExtra("user2",user2);
                //startActivity(intent, options.toBundle());
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        });
    }
    private void choosePicture() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.addCategory("android.intent.category.OPENABLE");
        intent.setType("image/*");
        startActivityForResult(intent,1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            uploadPicture();
        }
    }
    private void uploadPicture() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading Image...");
        pd.show();

        firebaseStorage = FirebaseStorage.getInstance();
        String uid = spaceDatabaseReference.push().getKey();
        storageReference = firebaseStorage.getReference().child("Space/"+getIntent().getStringExtra("uid")+"/Photos/" + uid);
        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        spaceDatabaseReference.child(getIntent().getStringExtra("uid")+"/numOfPhotos").setValue(getNumOfPhotos()+1);
                        updatePhotoView2();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(),"Failed to Upload",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        pd.setMessage("Progress: "+(int)progressPercent+"%");
                    }
                });
    }
    private User getCurrentUser() {
        if (firebaseUser.getEmail() != null) {
            for (User u : userList) {
                if (firebaseUser.getEmail().equals(u.getEmail())) {
                    return u;
                }
            }
        }
        return null;
    }

    private void updatePhotoView2(){

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("Space/"+getIntent().getStringExtra("uid")+"/Photos");
        storageReference.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        List<StorageReference> items = listResult.getItems();
                        List<String> list = new ArrayList<>();
                        for (StorageReference r: items){
                            list.add(r.toString());
                        }
                        String uid = getIntent().getStringExtra("uid");
                        PhotoList adapter = new PhotoList(PhotoPage.this, list,getNumOfPhotos(),items,uid);
                        photoListView = findViewById(R.id.list_view_photos);
                        photoListView.setAdapter(adapter);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                    }
                });
    }
    private int getNumOfPhotos() {
        if (firebaseUser.getEmail() != null) {
            for (Space u : spaceList) {
                if (u.getSpaceUid().equals(getIntent().getStringExtra("uid"))) {
                    return u.getNumOfPhotos();
                }
            }
        }
        return NULL;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        spaceDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                spaceList.clear();
                if (firebaseUser!=null){
                    currentUserEmail = firebaseUser.getEmail();
                }
                for (DataSnapshot spaceSnapshot : snapshot.getChildren()) {
                    Space space = spaceSnapshot.getValue(Space.class);
                    if (firebaseUser!=null){
                        if (isCurrentUsersSpace(space)) {
                            spaceList.add(space);
                        }
                    }else{
                        spaceList.add(space);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }

        });
        photoViewReference = spaceDatabaseReference.child(getIntent().getStringExtra("uid"));
        photoViewReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(getNumOfPhotos()!=0) {
                    photoListView = findViewById(R.id.list_view_photos);
                    if (photoListView != null) {
                        updatePhotoView2();
                    }
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

}