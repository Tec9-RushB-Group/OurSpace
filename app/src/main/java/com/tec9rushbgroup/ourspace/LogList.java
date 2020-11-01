package com.tec9rushbgroup.ourspace;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static java.sql.Types.NULL;

public class LogList extends ArrayAdapter<StorageReference> {
    private Activity context;
    String TAG  = "LogList";
    int currentIndex ;
    int numOfLogs;
    private List<StorageReference> references;
    private FirebaseDatabase database;
    private DatabaseReference spaceDatabaseReference;

    private String uid;


    public LogList(Activity context, List<StorageReference> list,String uid,int n) {
        super(context, R.layout.spaces_list_layout, list);
        this.context = context;
        currentIndex = 0;
        numOfLogs = n;
        this.uid = uid;
        references = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.log_list_layout, null, true);
        Button logDescription = listViewItem.findViewById(R.id.log_description);
        Button deleteButton = listViewItem.findViewById(R.id.delete_button);
        logDescription.setVisibility(View.INVISIBLE);
        logDescription.setEnabled(false);
        deleteButton.setVisibility(View.INVISIBLE);
        deleteButton.setEnabled(false);
        StorageReference r = references.get(position);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog(r);
            }
        });
        r.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {
                final long ONE_MEGABYTE = 1024 * 1024;
                r.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        String content = new String(bytes);
                        String description = storageMetadata.getName();
                        description = description.substring(0, description.length()-4);
                        logDescription.setText(description);
                        logDescription.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(context, LogDetail.class);
                                intent.putExtra("uid", uid);
                                intent.putExtra("content", content);
                                intent.putExtra("description", logDescription.getText().toString());
                                context.startActivity(intent);
                                context.overridePendingTransition(0, 0);
                            }
                        });
                        logDescription.setVisibility(View.VISIBLE);
                        logDescription.setEnabled(true);
                        deleteButton.setVisibility(View.VISIBLE);
                        deleteButton.setEnabled(true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
            }
        });

        return listViewItem;
    }

    private void deleteLog(StorageReference r){
        r.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                database = FirebaseDatabase.getInstance();
                spaceDatabaseReference = database.getReference("Spaces");
                spaceDatabaseReference.child(uid+"/numOfLogs").setValue(numOfLogs-1);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }


    private void dialog(StorageReference r){
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteLog(r);
            }
        });
        d.setNegativeButton("No", new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface arg0,int arg1){
                arg0.dismiss();
            }
        });
        d.setMessage("Are you sure to delete this Log?");
        d.show();
    }
}
