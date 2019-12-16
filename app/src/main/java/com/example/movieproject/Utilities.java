package com.example.movieproject;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

public class Utilities {
    public static void displayErrorSnackbar(View view, String text){
        Snackbar snackbar = Snackbar.make(view,text,Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(view.getContext(),R.color.colorRed));
        snackbar.show();
    }

    public static void hideKeyboard (Activity activity){
        if (activity.getCurrentFocus() != null){
            //if keyboard is visible
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }
}
