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
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class PhotoList extends ArrayAdapter<String> {
    private Activity context;
    private List<String> photos;
    String TAG  = "PhotoList";
    int currentIndex ;
    int numOfPhotos;


    public PhotoList(Activity context, List<String> list, int n) {
        super(context, R.layout.spaces_list_layout, list);
        this.context = context;
        this.photos = list;
        currentIndex = 0;
        numOfPhotos = n;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.photo_list_layout, null, true);
        ImageViewHelper image = listViewItem.findViewById(R.id.image);
        String s = photos.get(position);
        image.setImageURL(s);
        return listViewItem;
    }


}
