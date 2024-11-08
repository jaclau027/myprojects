package com.fit2081.fit2081assignment1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.fit2081.fit2081assignment1.provider.ManagementViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.StringTokenizer;

public class NewEventCategory extends AppCompatActivity {

    EditText etCategoryId;

    EditText etCategoryName;

    EditText etEventCount;

    Switch isActive;

    EditText etLocation;

    Gson gson = new Gson();

    ArrayList<EventCategory> listCategory = new ArrayList<>();

    ArrayList<Event> listEvent = new ArrayList<>();

    private ManagementViewModel managementViewModel;

    private CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event_category);

        managementViewModel = new ViewModelProvider(this).get(ManagementViewModel.class);

        etCategoryId = findViewById(R.id.autoCompleteTextView);
        etCategoryName = findViewById(R.id.editTextCategoryName);
        etEventCount = findViewById(R.id.editTextEventCount);
        isActive = findViewById(R.id.switch1);
        etLocation = findViewById(R.id.editTextLocation);


        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.SEND_SMS,
                android.Manifest.permission.RECEIVE_SMS,
                android.Manifest.permission.READ_SMS
        }, 0);

        MyBroadCastReceiver myBroadCastReceiver = new MyBroadCastReceiver();

        registerReceiver(myBroadCastReceiver, new IntentFilter(SMSReceiver.SMS_FILTER), RECEIVER_EXPORTED);

    }

    // from previous assignment, can ignore.
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
//    public ArrayList<EventCategory> readCategoryPreferences() {
//        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
//
//        String arrayListStringRestored = sharedPreferences.getString("category_key", "[]");
//        Type type = new TypeToken<ArrayList<EventCategory>>() {
//        }.getType();
//        listCategory = gson.fromJson(arrayListStringRestored, type);
//
//        return listCategory;
//    }

    public void saveButtonClick(View view) {
        String categoryIdString = autoGeneratedId();
        String categoryNameString = etCategoryName.getText().toString();
        String eventCountString = etEventCount.getText().toString();
        boolean activeBool = isActive.isChecked();
        String locationString = etLocation.getText().toString();

//        ArrayList<EventCategory> categoryArrayList = readCategoryPreferences();
        int countInteger = 0;
        String message = "";
        boolean alphanumeric = false;
        if (!eventCountString.equals("")) {
            countInteger = Integer.parseInt(eventCountString);;
        }

        // check if category name is alphabetical or alphanumeric
        for(char character : categoryNameString.toCharArray()) {
            if(Character.isAlphabetic(character)) {
                alphanumeric = true;
            }
        }
        // invalid category cannot be saved
        if (categoryNameString.equals("") || !alphanumeric) {
            message = "Invalid category name. ";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else if (countInteger < 0) {
            message = "Invalid Event Count. ";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
        else {
            EventCategory category = new EventCategory(categoryIdString, categoryNameString, countInteger, activeBool, locationString);
            managementViewModel.insertCategory(category);

            message = String.format("Category saved successfully: %s", categoryIdString);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

            etCategoryId.setText(categoryIdString);

            // return back to the dashboard
            Intent intent = new Intent(NewEventCategory.this, Dashboard.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
//
//            // Finish the current activity
//            finish();



        }
//             // save if valid input
//            EventCategory item = new EventCategory(categoryIdString, categoryNameString, countInteger, activeBool);
//            categoryArrayList.add(item);
//            Log.d("ItemListSize", "Item list size: " + categoryArrayList.size());
////            adapter.notifyDataSetChanged();
//            saveDataToSharedPreference();
//            message = String.format("Category saved successfully: %s", categoryIdString);
//            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//
//            etCategoryId.setText(categoryIdString);
//
//            // return back to the dashboard
//            Intent intent = new Intent(NewEventCategory.this, Dashboard.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            startActivity(intent);
//
//            // Finish the current activity
//            finish();
//        }

    }

//    private void saveDataToSharedPreference() {
//        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences",MODE_PRIVATE);
//        String arrayListString = gson.toJson(listCategory);
//
//        SharedPreferences.Editor edit = sharedPreferences.edit();
//        edit.putString("category_key", arrayListString);
//        edit.apply();
//
//    }


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