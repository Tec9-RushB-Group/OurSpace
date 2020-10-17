package com.tec9rushbgroup.ourspace;

import android.app.Activity;
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
        TextView currentSpaceStatus = (TextView) listViewItem.findViewById(R.id.current_space_status);

        Space space = spaceList.get(position);

        currentSpaceName.setText(space.getName());
        currentSpaceStatus.setText(space.getSpace_stat().toString());

        return listViewItem;
    }
}
