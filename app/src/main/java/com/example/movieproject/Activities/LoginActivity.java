package com.example.movieproject.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.movieproject.DatabaseHelper;
import com.example.movieproject.R;
import com.example.movieproject.Utilities;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText emailInput, passwordInput;
    private String email, password;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeVariables();
    }

    private void initializeVariables() {
        TextView createAccountButton = findViewById(R.id.createAccount);
        createAccountButton.setOnClickListener(this);

        TextView loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        databaseHelper = new DatabaseHelper(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginButton:
                if (validateInputs()){
                    if (databaseHelper.checkEmailPassword(email,password)){
                        startActivity(new Intent(LoginActivity.this,HomeActivity.class));
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
}
