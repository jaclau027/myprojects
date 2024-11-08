package com.fit2081.fit2081assignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    EditText etUsername;

    EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.editTextUsernameLogin);
        etPassword = findViewById(R.id.editTextPasswordLogin);
        SharedPreferences sharedPreferences = getSharedPreferences("UNIQUE_FILE_NAME", MODE_PRIVATE);
        etUsername.setText(sharedPreferences.getString("KEY_USER_NAME", "DEFAULT")); // set the username registered

    }

    public void loginButtonClick(View view) {
        String usernameString = etUsername.getText().toString();
        String passwordString = etPassword.getText().toString();

        SharedPreferences sharedPreferences = getSharedPreferences("UNIQUE_FILE_NAME", MODE_PRIVATE);

        sharedPreferences.getString("KEY_USER_NAME", "DEFAULT");
        sharedPreferences.getString("KEY_PASSWORD", "DEFAULT");
        sharedPreferences.getString("KEY_CONFIRM_PASSWORD", "DEFAULT");

        String message;

        // check if the username and password is the same as registered
        if (usernameString.equals(sharedPreferences.getString("KEY_USER_NAME", "DEFAULT")) && passwordString.equals(sharedPreferences.getString("KEY_PASSWORD", "DEFAULT"))) {
            message = "Authentication success.";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, Dashboard.class);

            // finally launch the activity using startActivity method
            startActivity(intent);
        } else {
            message = "Authentication failure: Username or Password incorrect.";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

    }
    public void registerButtonClick(View view) {
        // update the UI using retrieved values
        Intent intent = new Intent(this, MainActivity.class);

        // finally launch the activity using startActivity method
        startActivity(intent);
    }

}