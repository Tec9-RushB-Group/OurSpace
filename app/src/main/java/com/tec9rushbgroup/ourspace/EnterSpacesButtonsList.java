package com.tec9rushbgroup.ourspace;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class EnterSpacesButtonsList extends ArrayAdapter<Space> {
    private Activity context;
    private List<Space> spaceList;

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
        currentSpaceName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent intent = new Intent(context, CurrentSpace.class);
                intent.putExtra("uid",spaceList.get(position).getSpaceUid());
                context.startActivity(intent);
                context.overridePendingTransition(0,0);
            }
        });
        return listViewItem;
    }
}
