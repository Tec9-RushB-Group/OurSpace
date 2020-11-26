package com.tec9rushbgroup.ourspace;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class EnterSpacesButtonsList extends ArrayAdapter<Space> {
    private Activity context;
    private List<Space> spaceList;
    String TAG = "ListButton";
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase database;
    private DatabaseReference spaceDatabaseReference, userDatabaseReference;
    private List<User> userList;
    private String currentUserEmail;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    public EnterSpacesButtonsList(Activity context, List<Space> spaceList) {
        super(context, R.layout.spaces_list_layout, spaceList);
        this.context = context;
        this.spaceList = spaceList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.spaces_list_layout, null, true);
        Button currentSpaceName = (Button) listViewItem.findViewById(R.id.current_space_name);
        Space space = spaceList.get(position);
        currentSpaceName.setText(space.getName());
        Button deleteButton = listViewItem.findViewById(R.id.delete_space_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseStorage = FirebaseStorage.getInstance();
                storageReference = firebaseStorage.getReference().child("/Space/" + spaceList.get(position).getSpaceUid());
                dialog(position, storageReference);
            }
        });
        currentSpaceName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CurrentSpace.class);
                intent.putExtra("uid", spaceList.get(position).getSpaceUid());
                intent.putExtra("user1", spaceList.get(position).getUser1());
                intent.putExtra("user2", spaceList.get(position).getUser2());
                context.startActivity(intent);
                context.overridePendingTransition(0, 0);
                context.finish();
            }
        });
        return listViewItem;
    }

    private void dialog(int position, StorageReference r) {
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                r.child("Logs").listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        List<StorageReference> items = listResult.getItems();
                        for (StorageReference tmp : items) {
                            tmp.delete();
                        }
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Uh-oh, an error occurred!
                            }
                        });
                r.child("Photos").listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        List<StorageReference> items = listResult.getItems();
                        for (StorageReference tmp : items) {
                            tmp.delete();
                        }

                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Uh-oh, an error occurred!
                            }
                        });
                database = FirebaseDatabase.getInstance();
                spaceDatabaseReference = database.getReference("Spaces");
                spaceDatabaseReference.child(spaceList.get(position).getSpaceUid()).setValue(null);
            }
        });
        d.setNegativeButton("No", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
            }
        });
        d.setMessage("Would you like to delete this Space?");
        d.show();
    }

}
