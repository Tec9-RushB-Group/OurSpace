package com.tec9rushbgroup.ourspace;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
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
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.sql.Types.NULL;

public class PhotoList extends ArrayAdapter<String> {
    private Activity context;
    String TAG  = "PhotoList";
    int currentIndex ;
    int numOfPhotos;
    private List<StorageReference> references;
    private FirebaseDatabase database;
    private DatabaseReference spaceDatabaseReference;

    private String uid;


    public PhotoList(Activity context, List<String> list, int n,List<StorageReference> references,String uid) {
        super(context, R.layout.spaces_list_layout, list);
        this.context = context;
        currentIndex = 0;
        numOfPhotos = n;
        this.references = references;
        this.uid = uid;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.photo_list_layout, null, true);
        ImageViewHelper image = listViewItem.findViewById(R.id.image);
        Button deleteButton = listViewItem.findViewById(R.id.delete_photo_button);
        StorageReference r = references.get(position);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePhoto(r);
            }
        });
        r.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                image.setImageURL(uri+"");
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String[] images = new String[]{uri+""};
                        ArrayList<String> pictureList = new ArrayList<>();
                        pictureList.addAll(Arrays.asList(images));
                        PhotoBrowseActivity.startWithElement(context, pictureList, 0, image);
                        /*
                        LayoutInflater inflater = context.getLayoutInflater();
                        View imgEntryView = inflater.inflate(R.layout.dialog_photo, null);
                        final AlertDialog dialog = new AlertDialog.Builder(context).create();
                        ImageViewHelper img = imgEntryView.findViewById(R.id.large_image);
                        image.setDrawingCacheEnabled(true);
                        Bitmap bitmap = Bitmap.createBitmap(image.getDrawingCache());
                        image.setDrawingCacheEnabled(false);
                        img.setScaleType(ImageView.ScaleType. FIT_CENTER);
                        img.setImageBitmap(bitmap);

                        dialog.setView(imgEntryView); // 自定义dialog
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        imgEntryView.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View paramView) {
                                dialog.cancel();
                            }
                        });
                        dialog.show();

                         */
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });
        return listViewItem;
    }


    private void deletePhoto(StorageReference r){
        r.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                database = FirebaseDatabase.getInstance();
                spaceDatabaseReference = database.getReference("Spaces");
                spaceDatabaseReference.child(uid+"/numOfPhotos").setValue(numOfPhotos-1);
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

}
