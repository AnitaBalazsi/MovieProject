package com.example.movieproject.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.movieproject.Classes.User;
import com.example.movieproject.Helpers.DatabaseHelper;
import com.example.movieproject.R;
import com.example.movieproject.Utilities;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText nameInput, emailInput, passwordInput, confirmPasswordInput;
    private Button sendButton;
    private String username, email, password, confirmPassword;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initializeVariables();
    }

    private void initializeVariables() {
        databaseHelper = new DatabaseHelper(this);

        nameInput = findViewById(R.id.usernameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPassword);

        sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Utilities.hideKeyboard(this);
        if (validateInputs()){
            User user = new User(username,email,password);
            if (databaseHelper.checkIfRegistered(user)){
                Utilities.displayErrorSnackbar(findViewById(R.id.viewContainer), getString(R.string.alreadyRegistered));
            } else {
                databaseHelper.insertUser(user);
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        }
    }

    private boolean validateInputs() {
        username = nameInput.getText().toString().trim();
        email = emailInput.getText().toString().trim();
        password = passwordInput.getText().toString().trim();
        confirmPassword = confirmPasswordInput.getText().toString().trim();

        if (username.isEmpty()){
            nameInput.setError(getString(R.string.emptyInputError));
            nameInput.requestFocus();
            return false;
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailInput.setError(getString(R.string.emailInputError));
            emailInput.requestFocus();
            return false;
        }

        if (password.isEmpty() || password.length() < 6){
            passwordInput.setError(getString(R.string.passwordLengthError));
            passwordInput.requestFocus();
            return false;
        }

        if (!password.equals(confirmPassword)){
            confirmPasswordInput.setError(getString(R.string.confirmPasswordError));
            confirmPasswordInput.requestFocus();
            return false;
        }

        return true;
    }
}
