package com.fit2081.fit2081assignment1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    EditText etUsername;

    EditText etPassword;

    EditText etConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = findViewById(R.id.editTextUsername);
        etPassword = findViewById(R.id.editTextPassword);
        etConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR); // auto orientation changes
    }




    public void registerButtonClick(View view) {
        // using the referenced UI elements we extract values into plain text format
        String usernameString = etUsername.getText().toString();
        String passwordString = etPassword.getText().toString();
        String confirmPasswordString = etConfirmPassword.getText().toString();

        String message;

        // check non-empty fields and confirm password equals to password
        if (!usernameString.equals("") && !passwordString.equals("") && !confirmPasswordString.equals("") && confirmPasswordString.equals(passwordString)) {
            message = "Registration is successful.";
            saveDataToSharedPreference(usernameString, passwordString, confirmPasswordString);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

            // update the UI using retrieved values
            Intent intent = new Intent(this, Login.class);

            // finally launch the activity using startActivity method
            startActivity(intent);
        } else {
            message = "Registration is unsuccessful, retry please.";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

    }

    private void saveDataToSharedPreference(String usernameValue, String passwordValue, String confirmPasswordValue) {
        // initialise shared preference class variable to access Android's persistent storage
        SharedPreferences sharedPreferences = getSharedPreferences("UNIQUE_FILE_NAME", MODE_PRIVATE);

        // use .edit function to access file using Editor variable
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // save key-value pairs to the shared preference file
        editor.putString("KEY_USER_NAME", usernameValue);
        editor.putString("KEY_PASSWORD", passwordValue);
        editor.putString("KEY_CONFIRM_PASSWORD", confirmPasswordValue);

        // use editor.apply() to save data to the file asynchronously (in background without freezing the UI)
        // doing in background is very common practice for any File Input/Output operations
        editor.apply();

        // or
        // editor.commit()
        // commit try to save data in the same thread/process as of our user interface
    }

    public void loginButtonClick(View view) {
        // update the UI using retrieved values
        Intent intent = new Intent(this, Login.class);

        // finally launch the activity using startActivity method
        startActivity(intent);
    }

}