package com.fit2081.fit2081assignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

public class Dashboard extends AppCompatActivity {
    private DrawerLayout drawerlayout;

    private NavigationView navigationView;

    Toolbar toolbar;

    EditText etEventId;

    EditText etCategoryId;

    EditText etEventName;

    EditText etTicketsAvailable;

    Switch isActive;

    Gson gson = new Gson();

    ArrayList<Event> data = new ArrayList<>();

    ArrayList<EventCategory> listCategory = new ArrayList<>();

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        etEventId = findViewById(R.id.editTextEventid);
        etCategoryId = findViewById(R.id.editTextCategoryId_dashboard);
        etEventName = findViewById(R.id.editTextEventName);
        etTicketsAvailable = findViewById(R.id.editTextTickets);
        isActive = findViewById(R.id.switch3);

        drawerlayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerlayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerlayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new MyNavigationListener());


//        recycleview
//        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveButtonClick(view);
                Snackbar.make(view, "Save button clicked.", Snackbar.LENGTH_LONG)
                        .setAction("Undo", undoOnClickListener).show();
            }
        });
        getSupportFragmentManager().beginTransaction().replace(
                R.id.fragmentContainerViewCategoryDashboard, new FragmentListCategory()).commit();
    }

    View.OnClickListener undoOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ArrayList<Event> eventArrayList = readEventPreferences();
            int position = eventArrayList.size() - 1;
            String categoryId = eventArrayList.get(position).getCategory();
            eventArrayList.remove(eventArrayList.get(position));
            saveDataToEventSharedPreference(eventArrayList);

            ArrayList<EventCategory> categoryArrayList = readCategoryPreferences();
            for (EventCategory category :categoryArrayList) {
                if (category.getCategory().equals(categoryId)) {
                    category.setCount(category.getCount() - 1);
                    saveDataToCategorySharedPreference(categoryArrayList);
                }
            }
            getSupportFragmentManager().beginTransaction().replace(
                    R.id.fragmentContainerViewCategoryDashboard, new FragmentListCategory()).commit();

        }
    };

    class MyNavigationListener implements NavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // get the id of the selected item
            int id = item.getItemId();

            if (id == R.id.view_all_categories) {
                Intent intent = new Intent(Dashboard.this, ListCategoryActivity.class);
                startActivity(intent);
            } else if (id == R.id.add_category) {
                Intent intent = new Intent(Dashboard.this, NewEventCategory.class);
                startActivity(intent);
            } else if (id == R.id.view_all_events) {
                Intent intent = new Intent(Dashboard.this, ListEventActivity.class);
                startActivity(intent);
            } else if (id == R.id.logout) {
                Intent intent = new Intent(Dashboard.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            // close the drawer
            drawerlayout.closeDrawers();
            // tell the OS
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.clear_event_form) {
            clearForm();
        } else if (item.getItemId() == R.id.delete_all_categories) {
            deleteAllCategories();
            getSupportFragmentManager().beginTransaction().replace(
                    R.id.fragmentContainerViewCategoryDashboard, new FragmentListCategory()).commit();
        } else if (item.getItemId() == R.id.delete_all_events) {
            deleteAllEvents();

        } else if (item.getItemId() ==  R.id.refresh) {
            getSupportFragmentManager().beginTransaction().replace(
                    R.id.fragmentContainerViewCategoryDashboard, new FragmentListCategory()).commit();        }
        return true;
    }

    public void deleteAllEvents() {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        ArrayList<Event> eventArrayList = readEventPreferences();
        ArrayList<String> catId = new ArrayList<>();
        for (Event event : eventArrayList) {
            catId.add(event.getCategory());
        }
        ArrayList<EventCategory> categoryArrayList = readCategoryPreferences();
        for (EventCategory category :categoryArrayList) {
            for (String Id : catId) {
                if (category.getCategory().equals(Id)) {
                    category.setCount(category.getCount() - 1);
                }
            }
        }
        saveDataToCategorySharedPreference(categoryArrayList);
        getSupportFragmentManager().beginTransaction().replace(
                R.id.fragmentContainerViewCategoryDashboard, new FragmentListCategory()).commit();
        edit.remove("event_key");
        edit.apply();
    }

    public void deleteAllCategories() {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.remove("category_key");
        edit.apply();
    }


    public ArrayList<EventCategory> readCategoryPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);

        String arrayListStringRestored = sharedPreferences.getString("category_key", "[]");
        Type type = new TypeToken<ArrayList<EventCategory>>() {
        }.getType();
        listCategory = gson.fromJson(arrayListStringRestored, type);

        return listCategory;
    }

    public ArrayList<Event> readEventPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);

        String arrayListStringRestored = sharedPreferences.getString("event_key", "[]");
        Type type = new TypeToken<ArrayList<Event>>() {
        }.getType();
        data = gson.fromJson(arrayListStringRestored, type);

        return data;
    }
    public void saveButtonClick(View view) {
        String eventIdString = autoGeneratedId();
        String categoryIdString = etCategoryId.getText().toString();
        String eventNameString = etEventName.getText().toString();
        String ticketsString = etTicketsAvailable.getText().toString();
        boolean activeBool = isActive.isChecked();

//        adapter.notifyDataSetChanged();
        int ticketsInteger = 0;
        String message = "";
        boolean exist = false;
        boolean alphanumeric = false;
        // check the input of the user if invalid, toast error
        ArrayList<Event> eventArrayList = readEventPreferences();
        ArrayList<EventCategory> categoryArrayList = readCategoryPreferences();

        if (!ticketsString.equals("")) {
            ticketsInteger = Integer.parseInt(ticketsString);
        }
        for (EventCategory category :categoryArrayList) {
            if (category.getCategory().equals(categoryIdString)) {
                exist = true;
            }
        }
        for(char character : eventNameString.toCharArray()) {
            if(Character.isAlphabetic(character)) {
                alphanumeric = true;
            }
        }
            if (categoryIdString.equals("") || !exist) {
                message = "Category does not exist.";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            } else if (eventNameString.equals("") || !alphanumeric) {
                message = "Invalid event name.";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            } else if (ticketsInteger < 0) {
                message = "Invalid 'Tickets available.";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            } else {
                for (EventCategory category :categoryArrayList) {
                    if (category.getCategory().equals(categoryIdString)) {
                        category.setCount(category.getCount() + 1);
                        saveDataToCategorySharedPreference(categoryArrayList);
                        getSupportFragmentManager().beginTransaction().replace(
                                R.id.fragmentContainerViewCategoryDashboard, new FragmentListCategory()).commit();
                    }
                }
                // input valid, save it to shared preference
                    Event item = new Event(eventIdString, categoryIdString, eventNameString, ticketsInteger, activeBool);
                    eventArrayList.add(item);
                    saveDataToEventSharedPreference(eventArrayList);
                    message = String.format("Event saved: %s to %s", eventIdString, categoryIdString);
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

                    etEventId.setText(eventIdString);
                }
            }

    private void saveDataToCategorySharedPreference (ArrayList<EventCategory> data) {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        String arrayListString = gson.toJson(data);

        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("category_key", arrayListString);
        edit.apply();

    }

        private void saveDataToEventSharedPreference (ArrayList<Event> data) {
            SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
            String arrayListString = gson.toJson(data);

            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putString("event_key", arrayListString);
            edit.apply();


            //        // initialise shared preference class variable to access Android's persistent storage
//        SharedPreferences sharedPreferences = getSharedPreferences("UNIQUE_FILE_NAME", MODE_PRIVATE);
//
//        // use .edit function to access file using Editor variable
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//        // save key-value pairs to the shared preference file
//        editor.putString("KEY_EVENT_ID", eventIdValue);
//        editor.putString("KEY_CATEGORY_ID", categoryIdValue);
//        editor.putString("KEY_EVENT_NAME", eventNameValue);
//        editor.putInt("KEY_TICKETS", ticketsValue);
//        editor.putBoolean("KEY_IS_ACTIVE_EVENT", activeValue);
//
//        // use editor.apply() to save data to the file asynchronously (in background without freezing the UI)
//        // doing in background is very common practice for any File Input/Output operations
//        editor.apply();
//
//        // or
//        // editor.commit()
//        // commit try to save data in the same thread/process as of our user interface
        }

        public String autoGeneratedId () {
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

        public void clearForm () {
            etEventId.setText("");
            etCategoryId.setText("");
            etEventName.setText("");
            etTicketsAvailable.setText("");
            isActive.setChecked(false);
        }


}