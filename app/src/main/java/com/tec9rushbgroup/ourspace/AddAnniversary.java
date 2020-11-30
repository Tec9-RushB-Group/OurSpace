package com.tec9rushbgroup.ourspace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AddAnniversary extends AppCompatActivity {
    private Button addAnniversary, backToAnniversaries, btnDatePicker;
    private TextView welcomeTV, sloganTV;
    private int year;
    private int month;
    private int day;
    private TextInputLayout description;
    private boolean dateSet;
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
        setUpEnvironment();
        setContentView(R.layout.activity_add_anniversary);
        dateSet = false;
        btnDatePicker = findViewById(R.id.btnDatePicker);

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);


        welcomeTV = findViewById(R.id.welcome_text);
        sloganTV = findViewById(R.id.slogan_text);
        welcomeTV.setTypeface(Typeface.createFromAsset(getAssets(), "logo.ttf"));
        sloganTV.setTypeface(Typeface.createFromAsset(getAssets(), "slogan.ttf"));

        //Buttons
        addAnniversary = findViewById(R.id.add_anniversary_button);
        backToAnniversaries = findViewById(R.id.back_to_anniversaries);
        description = findViewById(R.id.anniversary_description);
        String uid = getIntent().getStringExtra("uid");
        String user1 = getIntent().getStringExtra("user1");
        String user2 = getIntent().getStringExtra("user2");
        addAnniversary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String descriptionText = description.getEditText().getText().toString();
                if (!dateSet){
                    description.setError("Please pick a date!");
                    return;
                }
                if (descriptionText.equals("")){
                    description.setError("Please write a description!");
                    return;
                }
                descriptionText = description.getEditText().getText().toString();
                final ProgressDialog pd = new ProgressDialog(AddAnniversary.this);
                pd.setTitle("Please wait...");
                pd.setMessage("Creating Anniversary...");
                pd.show();
                addAnniversary.setEnabled(false);
                backToAnniversaries.setEnabled(false);
                btnDatePicker.setEnabled(false);
                description.setEnabled(false);
                //create a new text file to LOCAL.
                String uid = userDatabaseReference.push().getKey();
                fileName = uid+"&"+descriptionText+"&"+ year + " " + (month+1) + " " + day +".txt";
                FileOutputStream fos = null;
                File filePath = null;

                try{
                    fos = openFileOutput(fileName, MODE_PRIVATE);
                    fos.write(descriptionText.getBytes());
                    filePath = getFilesDir();
                }catch (IOException e){
                    e.printStackTrace();
                }finally {
                    if(fos != null){
                        try{
                            fos.close();
                        }catch(IOException e){
                            e.printStackTrace();
                        }
                    }
                }

                file = Uri.fromFile(new File(filePath.toString()+"/"+fileName));
                storageReference = firebaseStorage.getReference().child("Space/"+getIntent().getStringExtra("uid")+"/Anniversary/"+fileName);
                UploadTask uploadTask = storageReference.putFile(file);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        addAnniversary.setEnabled(true);
                        backToAnniversaries.setEnabled(true);
                        btnDatePicker.setEnabled(true);
                        description.setEnabled(true);
                        description.setError("Network Error");
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        String uid = getIntent().getStringExtra("uid");
                        String user1 = getIntent().getStringExtra("user1");
                        String user2 = getIntent().getStringExtra("user2");
                        Intent intent = new Intent(AddAnniversary.this, AnniversaryPage.class);
                        intent.putExtra("uid",uid);
                        intent.putExtra("user1",user1);
                        intent.putExtra("user2",user2);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        finish();
                    }
                });

            }
        });

        backToAnniversaries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid = getIntent().getStringExtra("uid");
                String user1 = getIntent().getStringExtra("user1");
                String user2 = getIntent().getStringExtra("user2");
                Intent intent = new Intent(AddAnniversary.this, AnniversaryPage.class);
                intent.putExtra("uid", uid);
                intent.putExtra("user1", user1);
                intent.putExtra("user2", user2);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(AddAnniversary.this,dateSetListener,year,month,day);
                datePickerDialog.show();//显示DatePickerDialog组件
            }
        });

    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int y, int m, int d) {


            year = y;
            month = m;
            day = d;

            updateDate();

        }

        private void updateDate() {
            dateSet = true;
            btnDatePicker.setText("Date：" + year + "-" + (month+1) + "-" + day);
        }
    };

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
    public void onBackPressed() {
        String uid = getIntent().getStringExtra("uid");
        String user1 = getIntent().getStringExtra("user1");
        String user2 = getIntent().getStringExtra("user2");
        Intent intent = new Intent(AddAnniversary.this, AnniversaryPage.class);
        intent.putExtra("uid", uid);
        intent.putExtra("user1", user1);
        intent.putExtra("user2", user2);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
}