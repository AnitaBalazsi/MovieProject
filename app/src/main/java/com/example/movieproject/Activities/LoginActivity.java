package com.example.movieproject.Activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.movieproject.Helpers.DatabaseHelper;
import com.example.movieproject.Helpers.MovieDbService;
import com.example.movieproject.R;
import com.example.movieproject.Utilities;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private EditText emailInput, passwordInput;
    private String email,password;
    public static String username;
    private DatabaseHelper databaseHelper;
    private CheckBox rememberMe;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Utilities.setStatusbarColor(this);
        initializeVariables();
        startMovieService();
    }

    private void startMovieService() {
        Intent intent = new Intent(getApplicationContext(), MovieDbService.class);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager)getSystemService(Activity.ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), AlarmManager.INTERVAL_HALF_DAY, pendingIntent);
    }

    private void initializeVariables() {
        TextView createAccountButton = findViewById(R.id.createAccount);
        createAccountButton.setOnClickListener(this);

        TextView loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        rememberMe = findViewById(R.id.rememberMe);
        rememberMe.setOnCheckedChangeListener(this);

        sharedPreferences = getApplicationContext().getSharedPreferences("LoginData", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        emailInput.setText(sharedPreferences.getString("email",null));
        passwordInput.setText(sharedPreferences.getString("password", null));
        rememberMe.setChecked(sharedPreferences.getBoolean("rememberMe",false));

        databaseHelper = new DatabaseHelper(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginButton:
                if (validateInputs()){
                    if (databaseHelper.checkEmailPassword(email,password)){
                        username = databaseHelper.getUsername(email);

                        Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                        intent.putExtra("email",email);
                        startActivity(intent);
                    } else {
                        Utilities.displayErrorSnackbar(findViewById(R.id.viewContainer),getString(R.string.loginInputError));
                    }
                }
                break;
            case R.id.createAccount:
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                break;
        }
    }

    private boolean validateInputs() {
        email = emailInput.getText().toString().trim();
        password = passwordInput.getText().toString().trim();
        if (email.isEmpty()){
            emailInput.setError(getString(R.string.emailInputError));
            emailInput.requestFocus();
            return false;
        }

        if (password.isEmpty()){
            passwordInput.setError(getString(R.string.emptyInputError));
            passwordInput.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //store data in shared preferences
        if (isChecked){
            editor.putString("email",emailInput.getText().toString().trim());
            editor.putString("password",passwordInput.getText().toString().trim());
            editor.putBoolean("rememberMe",rememberMe.isChecked());
            editor.apply();
        } else {
            Utilities.clearLoginData(this);
        }
    }
}
