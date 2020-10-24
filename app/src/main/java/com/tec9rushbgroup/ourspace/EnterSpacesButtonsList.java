package com.tec9rushbgroup.ourspace;

import android.app.Activity;
import android.app.ActivityOptions;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class EnterSpacesButtonsList extends ArrayAdapter<Space> {
    private Activity context;
    private List<Space> spaceList;
    String TAG  = "ListButton";


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
        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(context, DeleteSpace.class);
                intent.putExtra("uid",spaceList.get(position).getSpaceUid());
                intent.putExtra("user1",spaceList.get(position).getUser1());
                intent.putExtra("user2",spaceList.get(position).getUser2());
                context.startActivity(intent);
                context.overridePendingTransition(0,0);
            }
        });
        currentSpaceName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(context, CurrentSpace.class);
                intent.putExtra("uid",spaceList.get(position).getSpaceUid());
                intent.putExtra("user1",spaceList.get(position).getUser1());
                intent.putExtra("user2",spaceList.get(position).getUser2());
                context.startActivity(intent);
                context.overridePendingTransition(0,0);
            }
        });
        return listViewItem;
    }


}
