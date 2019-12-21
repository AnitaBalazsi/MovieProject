package com.example.movieproject.Fragments;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.FileUtils;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.movieproject.Activities.HomeActivity;
import com.example.movieproject.Helpers.DatabaseHelper;
import com.example.movieproject.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {
    private String currentUserEmail, currentUserName;
    private DatabaseHelper databaseHelper;
    private Button uploadPicture;
    private ImageView profilePicture;
    private Bitmap imageBitmap;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentUserEmail = ((HomeActivity) getActivity()).getCurrentUserEmail();
        databaseHelper = new DatabaseHelper(getContext());

        uploadPicture = getView().findViewById(R.id.uploadPictureButton);
        uploadPicture.setOnClickListener(this);

        setUserData();
    }

    private void setUserData() {
        TextView username = getView().findViewById(R.id.username);
        currentUserName = databaseHelper.getUsername(currentUserEmail);
        username.setText(currentUserName);

        profilePicture = getView().findViewById(R.id.profilePicture);
        Glide.with(getContext()).load(databaseHelper.getImage(currentUserName)).into(profilePicture);
    }

    @Override
    public void onClick(View v) {
        getImageFromGallery();
    }

    private void getImageFromGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            //store image
        }
    }


}
