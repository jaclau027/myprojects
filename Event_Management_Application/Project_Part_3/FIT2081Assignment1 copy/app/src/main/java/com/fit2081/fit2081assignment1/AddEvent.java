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

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

public class AddEvent extends AppCompatActivity {

    EditText etEventId;

    EditText etCategoryId;

    EditText etEventName;

    EditText etTicketsAvailable;

    Switch isActive;

    Gson gson;

    ArrayList<Event> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        etEventId = findViewById(R.id.autoCompleteTextView2);
        etCategoryId = findViewById(R.id.editTextCategoryId);
        etEventName = findViewById(R.id.editTextEventName);
        etTicketsAvailable = findViewById(R.id.editTextTickets);
        isActive = findViewById(R.id.switch2);

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
            // check if it starts from "event:"
            if (msg.startsWith("event:")) {
                if (count == 3) {
                    String[] sT = msg.split(";", -1);
                    String eventName = sT[0];
                    if (!eventName.equals("")) {
                        eventName = eventName.substring(6); // take the category name
                    }
                    String categoryId = sT[1];
                    String tickets = sT[2];
                    String active = sT[3];

                    // if the input is empty, set them to blank
                    if (eventName.equals("")) {
                        eventName = "";
                    }
                    if (categoryId.equals("")) {
                        categoryId = "";
                    }
                    if (tickets.equals("")) {
                        tickets = "";
                    }
                    if (active.equals("")) {
                        active = "";
                    }


                    // check if the integer is integer or character, return false if it is not integer or 0 or less else return true
                    boolean isInteger = true;
                    for (int i = 0; i < tickets.length(); i++) {
                        if (!Character.isDigit(tickets.charAt(i)) || tickets.equals("0")) {
                            isInteger = false;
                        }
                    }

                    // set the input in the edit text box if the conditions are met, event is valid
                    if (!categoryId.equals("") && !eventName.equals("") && isInteger && (active.equals("TRUE") || active.equals("FALSE") || active.equals(""))) {
                        etCategoryId.setText(categoryId);
                        etEventName.setText(eventName);
                        etTicketsAvailable.setText(String.valueOf(tickets));
                        isActive.setChecked(active.equals("TRUE"));
                        String message = "Event valid";
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).cancel();
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    } else { // invalid event
                        String message = "Event invalid";
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).cancel();
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                } else { // incorrect number of semicolon
                    String message = "Unknown or invalid command";
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).cancel();
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            } else if (!msg.startsWith("category:") && !msg.startsWith("event:")) {
                String message = "Unknown or invalid command";
                Toast.makeText(context, message, Toast.LENGTH_SHORT).cancel();
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void saveButtonClick(View view) {
        String eventIdString = autoGeneratedId();
        String categoryIdString = etCategoryId.getText().toString();
        String eventNameString = etEventName.getText().toString();
        String ticketsString = etTicketsAvailable.getText().toString();
        boolean activeBool = isActive.isChecked();

        int ticketsInteger = 0;
        String message = "";
        // check the input of the user if invalid, toast error
        if (categoryIdString.equals("") || eventNameString.equals("") || ticketsString.equals("0")){
            message = "Event invalid";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else {
            if (!ticketsString.equals("")) {
                ticketsInteger = Integer.parseInt(ticketsString);
            } // input valid, save it to shared preference
            Event event = new Event(eventIdString, categoryIdString, eventNameString, ticketsInteger, activeBool);
            data.add(event);
            message = String.format("Event saved: %s to %s", eventIdString, categoryIdString);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            etEventId.setText(eventIdString);
        }
    }

    private void saveDataToSharedPreference() {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences",MODE_PRIVATE);
        String arrayListString = gson.toJson(data);

        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("event_key", arrayListString);
        edit.apply();

        // or
        // editor.commit()
        // commit try to save data in the same thread/process as of our user interface
    }


    public String autoGeneratedId() {
        // method to generate ID for event
        Random r = new Random();
        String Id = "E"; // starts with E
        for (int i = 0; i < 2; i++) {
            Id += (char) (r.nextInt(26) + 'a');
        }
        Id = Id.toUpperCase() + "-";
        for (int i = 0; i < 5; i++) {
            Id += r.nextInt(10);
        }
        return Id;
    }
}