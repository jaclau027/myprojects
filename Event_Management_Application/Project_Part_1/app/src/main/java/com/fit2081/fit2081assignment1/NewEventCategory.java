package com.fit2081.fit2081assignment1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Random;
import java.util.StringTokenizer;

public class NewEventCategory extends AppCompatActivity {

    EditText etCategoryId;

    EditText etCategoryName;

    EditText etEventCount;

    Switch isActive;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event_category);

        etCategoryId = findViewById(R.id.autoCompleteTextView);
        etCategoryName = findViewById(R.id.editTextCategoryName);
        etEventCount = findViewById(R.id.editTextEventCount);
        isActive = findViewById(R.id.switch1);

        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.SEND_SMS,
                android.Manifest.permission.RECEIVE_SMS,
                android.Manifest.permission.READ_SMS
        }, 0);

        MyBroadCastReceiver myBroadCastReceiver = new MyBroadCastReceiver();

        registerReceiver(myBroadCastReceiver, new IntentFilter(SMSReceiver.SMS_FILTER), RECEIVER_EXPORTED);

    }

    class MyBroadCastReceiver extends BroadcastReceiver {
        /*
         * This method 'onReceive' will get executed every time class SMSReceive sends a broadcast
         * */
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra(SMSReceiver.SMS_MSG_KEY);
            // check how many semicolon
            int count = 0;
            for (int i = 0; i < msg.length(); i++) {
                if (msg.charAt(i) == ';') {
                    count++;
                }
            }

            // only execute the following if the sms starts with "category:"
            if (msg.startsWith("category:")) {
                if (count == 2) {
                    String[] sT = msg.split(";",-1);
                    String categoryName = sT[0];
                    if (!categoryName.equals("")) {
                        categoryName = categoryName.substring(9);
                    }
                    String eventCount = sT[1];
                    String active = sT[2];

                    // fill up the blank if the user's input is blank
                    if (categoryName.equals("")) {
                        categoryName = "";
                    }
                    if (eventCount.equals("")) {
                        eventCount = "";
                    }
                    if (active.equals("")) {
                        active = "";
                    }

                    // check if the event count is integer or not, false if it contains character and equals to 0
                    boolean isInteger = true;
                    for (int i = 0; i < eventCount.length(); i++) {
                        if (!Character.isDigit(eventCount.charAt(i)) || eventCount.equals("0")) {
                            isInteger = false;
                        }
                    }

                    // set the sms input in the edit text box if it met the conditions
                    if (!categoryName.equals("") && (isInteger) && (active.equals("TRUE") || active.equals("FALSE") || active.equals(""))) {
                        etCategoryName.setText(categoryName);
                        etEventCount.setText(String.valueOf(eventCount));
                        isActive.setChecked(active.equals("TRUE"));
                        String message = "Event category valid";
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).cancel();
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                    else { // input invalid
                        String message = "Event category invalid";
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).cancel();
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                }
                else { // wrong number of semicolon
                    String message = "Unknown or invalid command";
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).cancel();
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            }
            else if (!msg.startsWith("category:") && !msg.startsWith("event:")) {
                String message = "Unknown or invalid command";
                Toast.makeText(context, message, Toast.LENGTH_SHORT).cancel();
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void saveButtonClick(View view) {
        String categoryIdString = autoGeneratedId();
        String categoryNameString = etCategoryName.getText().toString();
        String eventCountString = etEventCount.getText().toString();
        boolean activeBool = isActive.isChecked();

        int countInteger = 0;
        String message = "";
        // invalid category cannot be saved
        if (categoryNameString.equals("") || eventCountString.equals("0")){
            message = "Category invalid";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
        else {
            if (!eventCountString.equals("")) {
                countInteger = Integer.parseInt(eventCountString);;
            } // save if valid input
            saveDataToSharedPreference(categoryIdString, categoryNameString, countInteger, activeBool);
            message = String.format("Category saved successfully: %s", categoryIdString);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

            etCategoryId.setText(categoryIdString);
        }
    }

    private void saveDataToSharedPreference(String categoryIdValue, String categoryNameValue, int countValue, boolean activeValue) {
        // initialise shared preference class variable to access Android's persistent storage
        SharedPreferences sharedPreferences = getSharedPreferences("UNIQUE_FILE_NAME", MODE_PRIVATE);

        // use .edit function to access file using Editor variable
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // save key-value pairs to the shared preference file
        editor.putString("KEY_CATEGORY_ID", categoryIdValue);
        editor.putString("KEY_CATEGORY_NAME", categoryNameValue);
        editor.putInt("KEY_COUNT", countValue);
        editor.putBoolean("KEY_IS_ACTIVE", activeValue);

        // use editor.apply() to save data to the file asynchronously (in background without freezing the UI)
        // doing in background is very common practice for any File Input/Output operations
        editor.apply();

        // or
        // editor.commit()
        // commit try to save data in the same thread/process as of our user interface
    }


    public String autoGeneratedId() {
        // This method will generate category ID.
        Random r = new Random();
        String Id = "C";
        for (int i = 0; i < 2; i++) {
            Id += (char) (r.nextInt(26) + 'a');
        }
        Id = Id.toUpperCase() + "-";
        for (int i = 0; i < 4; i++) {
            Id += r.nextInt(10);
        }
        return Id;
    }




}