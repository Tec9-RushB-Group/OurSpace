package com.tec9rushbgroup.ourspace;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class AnniversaryList extends ArrayAdapter<StorageReference> {
    private Activity context;
    String TAG  = "AnniversaryList";
    int currentIndex ;
    int numOfAnniversaries;
    private List<StorageReference> references;
    private FirebaseDatabase database;
    private DatabaseReference spaceDatabaseReference;

    private String uid,user1,user2;


    public AnniversaryList(Activity context, List<StorageReference> list, String uid, String user1, String user2,int i) {
        super(context, R.layout.spaces_list_layout, list);
        this.context = context;
        currentIndex = 0;
        numOfAnniversaries = i;
        this.uid = uid;
        references = list;
        this.user1 = user1;
        this.user2 = user2;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.anniversary_list_layout, null, true);
        TextView anniversaryDescription = listViewItem.findViewById(R.id.anniversary_description);
        TextView anniversaryDate = listViewItem.findViewById(R.id.anniversary_date);
        Button deleteButton = listViewItem.findViewById(R.id.delete_button);
        anniversaryDescription.setEnabled(false);
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
                        String filename = storageMetadata.getName();
                        String[] temp = filename.split("&");
                        String date = temp[2].substring(0,temp[2].indexOf('.'));
                        anniversaryDescription.setText(" Description: "+temp[1]);
                        anniversaryDate.setText(" Date: "+date);
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
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle("Please wait...");
        pd.setMessage("Deleting Anniversary...");
        pd.show();
        r.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                database = FirebaseDatabase.getInstance();
                spaceDatabaseReference = database.getReference("Spaces");
                spaceDatabaseReference.child(uid+"/numOfAnniversaries").setValue(numOfAnniversaries-1);
                pd.dismiss();
                Intent intent = new Intent(context, AnniversaryPage.class);
                intent.putExtra("uid", uid);
                intent.putExtra("user1", user1);
                intent.putExtra("user2", user2);
                context.startActivity(intent);
                context.overridePendingTransition(0, 0);
                context.finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                pd.dismiss();
                Intent intent = new Intent(context, AnniversaryPage.class);
                intent.putExtra("uid", uid);
                intent.putExtra("user1", user1);
                intent.putExtra("user2", user2);
                context.startActivity(intent);
                context.overridePendingTransition(0, 0);
                context.finish();
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
        d.setMessage("Would you like to delete this Anniversary?");
        d.show();
    }
}
