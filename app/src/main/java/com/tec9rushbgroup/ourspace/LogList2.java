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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class LogList2 extends ArrayAdapter<Log> {
    private Activity context;
    String TAG  = "LogList";
    int currentIndex ;
    int numOfLogs;
    private List<Log> logs;
    private FirebaseDatabase database;
    private DatabaseReference spaceDatabaseReference;

    private String spaceUid,user1,user2;


    public LogList2(Activity context, List<Log> list, String uid, String user1, String user2,int n) {
        super(context, R.layout.spaces_list_layout, list);
        this.context = context;
        currentIndex = 0;
        numOfLogs = n;
        this.spaceUid = uid;
        logs = list;
        this.user1 = user1;
        this.user2 = user2;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.log_list_layout, null, true);
        Button logDescription = listViewItem.findViewById(R.id.log_description);
        Button deleteButton = listViewItem.findViewById(R.id.delete_button);
        logDescription.setEnabled(false);
        deleteButton.setEnabled(false);
        Log l = logs.get(position);
        logDescription.setText(l.getTitle());
        String content = l.getContent();
        logDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LogDetail.class);
                intent.putExtra("uid", spaceUid);
                intent.putExtra("user1", user1);
                intent.putExtra("user2", user2);
                intent.putExtra("content", content);
                intent.putExtra("description", l.getTitle());
                context.startActivity(intent);
                context.overridePendingTransition(0, 0);
                context.finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog(l);
            }
        });
        logDescription.setEnabled(true);
        deleteButton.setEnabled(true);

        return listViewItem;
    }

    private void deleteLog(Log l){
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle("Please wait...");
        pd.setMessage("Deleting Log...");
        pd.show();
        database = FirebaseDatabase.getInstance();
        database.getReference("Logs").child(l.getUID()).setValue(null);
        database = FirebaseDatabase.getInstance();
        spaceDatabaseReference = database.getReference("Spaces");
        int num = numOfLogs-1;
        spaceDatabaseReference.child(spaceUid+"/numOfLogs").setValue(num);
        pd.dismiss();
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }


    private void dialog(Log l){
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteLog(l);
            }
        });
        d.setNegativeButton("No", new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface arg0,int arg1){
                arg0.dismiss();
            }
        });
        d.setMessage("Would you like to delete this Log?");
        d.show();
    }
}
