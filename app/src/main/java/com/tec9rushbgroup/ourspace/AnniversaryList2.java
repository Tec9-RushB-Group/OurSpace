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

public class AnniversaryList2 extends ArrayAdapter<Anniversary> {
    private Activity context;
    String TAG  = "AnniversaryList";
    int currentIndex ;
    int numOfAnniversaries;
    private List<Anniversary> anniversaries;
    private FirebaseDatabase database;
    private DatabaseReference spaceDatabaseReference;

    private String spaceUid,user1,user2;


    public AnniversaryList2(Activity context, List<Anniversary> list, String spaceUid, String user1, String user2, int i) {
        super(context, R.layout.spaces_list_layout, list);
        this.context = context;
        currentIndex = 0;
        numOfAnniversaries = i;
        this.spaceUid = spaceUid;

        anniversaries = list;
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

        Anniversary a = anniversaries.get(position);
        anniversaryDate.setText(" Date: "+a.getDate());
        anniversaryDescription.setText(" Description: "+a.getDescription());


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog(a);
            }
        });
        deleteButton.setEnabled(true);
        return listViewItem;
    }

    private void deleteAnniversary(Anniversary a){
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle("Please wait...");
        pd.setMessage("Deleting Anniversary...");
        pd.show();
        database = FirebaseDatabase.getInstance();
        database.getReference("Anniversaries").child(a.getUID()).setValue(null);
        database = FirebaseDatabase.getInstance();
        spaceDatabaseReference = database.getReference("Spaces");
        spaceDatabaseReference.child(spaceUid+"/numOfAnniversaries").setValue(numOfAnniversaries-1);
        pd.dismiss();
        Intent intent = new Intent(context, AnniversaryPage.class);
        intent.putExtra("uid", spaceUid);
        intent.putExtra("user1", user1);
        intent.putExtra("user2", user2);
        context.startActivity(intent);
        context.overridePendingTransition(0, 0);
        context.finish();
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }


    private void dialog(Anniversary a){
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAnniversary(a);
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
