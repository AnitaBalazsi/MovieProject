package com.example.movieproject.Fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.FileUtils;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.movieproject.Activities.HomeActivity;
import com.example.movieproject.Activities.LoginActivity;
import com.example.movieproject.Helpers.DatabaseHelper;
import com.example.movieproject.R;
import com.example.movieproject.Utilities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {
    private String currentUserEmail, currentUserName;
    private DatabaseHelper databaseHelper;
    private Bitmap profilePicture;

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

        Button uploadPicture = getView().findViewById(R.id.uploadPictureButton);
        uploadPicture.setOnClickListener(this);
        Button changePassword = getView().findViewById(R.id.changePassword);
        changePassword.setOnClickListener(this);
        TextView logOut = getView().findViewById(R.id.logOut);
        logOut.setOnClickListener(this);

        setUserData();
    }

    private void setUserData() {
        TextView username = getView().findViewById(R.id.username);
        currentUserName = databaseHelper.getUsername(currentUserEmail);
        username.setText(currentUserName);

        ImageView profilePicture = getView().findViewById(R.id.profilePicture);
        Glide.with(getContext()).load(databaseHelper.getImage(currentUserName)).into(profilePicture);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.uploadPictureButton:
                getImageFromGallery();
                break;
            case R.id.changePassword:
                showDialog();
                break;
            case R.id.logOut:
                Utilities.clearLoginData(getContext());

                //go to login screen
                Intent login = new Intent(getActivity(), LoginActivity.class);
                startActivity(login);
                break;
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.changePassword));
        builder.setView(R.layout.change_password);

        final AlertDialog dialog = builder.create();
        dialog.show();

        final EditText password = dialog.findViewById(R.id.passwordInput);
        final EditText confirmPassword = dialog.findViewById(R.id.confirmPassword);
        Button cancelButton = dialog.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button sendButton = dialog.findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs(password,confirmPassword)){
                    String newPassword = password.getText().toString().trim();
                    databaseHelper.changePassword(currentUserName,newPassword);

                    dialog.dismiss();
                    Toast.makeText(getContext(),getString(R.string.passwordChanged),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateInputs(EditText newPassword, EditText confirmPassword) {
        String password = newPassword.getText().toString().trim();
        if (password.isEmpty() || password.length() < 6){
            newPassword.setError(getString(R.string.passwordLengthError));
            newPassword.requestFocus();
            return false;
        }

        if (!password.equals(confirmPassword.getText().toString().trim())){
            confirmPassword.setError(getString(R.string.confirmPasswordError));
            confirmPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void getImageFromGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            try {
                profilePicture = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),data.getData());
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                profilePicture.compress(Bitmap.CompressFormat.JPEG,100,stream);

                databaseHelper.insertImage(currentUserName,stream.toByteArray());
                setUserData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
