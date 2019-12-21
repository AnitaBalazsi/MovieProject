package com.example.movieproject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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

    public static void clearLoginData(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.apply();
    }
}
