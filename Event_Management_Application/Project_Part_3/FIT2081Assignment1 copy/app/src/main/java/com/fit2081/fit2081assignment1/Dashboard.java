package com.fit2081.fit2081assignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GestureDetectorCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.fit2081.fit2081assignment1.provider.ManagementViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    private ManagementViewModel managementViewModel;

    CategoryAdapter categoryAdapter;

    private LiveData<List<Event>> allEventsLiveData;

    private LiveData<List<EventCategory>> allCategoriesLiveData;


    private List<EventCategory> categoryList;

    private EventCategory cat;

    private String message;

    private boolean add = false;

    private volatile boolean mIsRunning = true;

    private Thread thread;

    TextView tvX, tvY, tvRawX, tvRawY, tvEventType, tvGesture;

    private View touchpad;

    private GestureDetectorCompat mDetector;

    // help detect multi touch gesture like pinch-in, pinch-out specifically used for zoom functionality
    private ScaleGestureDetector mScaleDetector;

    Handler uiHandler = new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        managementViewModel = new ViewModelProvider(this).get(ManagementViewModel.class);



        etEventId = findViewById(R.id.editTextEventid);
        etCategoryId = findViewById(R.id.editTextCategoryId_dashboard);
        etEventName = findViewById(R.id.editTextEventName);
        etTicketsAvailable = findViewById(R.id.editTextTickets);
        isActive = findViewById(R.id.switch3);

        drawerlayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Assignment 3");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerlayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerlayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new MyNavigationListener());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveButtonClick(view);
            }
        });
//         update the fragment
        getSupportFragmentManager().beginTransaction().replace(
                R.id.fragmentContainerViewCategoryDashboard, new FragmentListCategory()).commit();


        touchpad = findViewById(R.id.touchpad);
        tvGesture = findViewById(R.id.tvGesture);

        CustomGestureDetector customGestureDetector = new CustomGestureDetector();

        mDetector = new GestureDetectorCompat(this, customGestureDetector);

        mDetector.setOnDoubleTapListener(customGestureDetector);

        touchpad.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                mDetector.onTouchEvent(event);

                return true;
            }
        });
    }

    class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener {

        public boolean onDoubleTap(@NonNull MotionEvent e) {
            tvGesture.setText("onDoubleTap");
             saveEventTap(e, findViewById(R.id.touchpad));
            return super.onDoubleTap(e);
        }

        private void saveEventTap(MotionEvent event, View view) {
            saveButtonClick(view);


        }

        public void onLongPress(@NonNull MotionEvent e) {
            tvGesture.setText("onLongPress");
            clearForm();
            super.onLongPress(e);
        }

    }

        View.OnClickListener undoOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            managementViewModel.undoEvent();

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

        } 
        return true;
    }

    public void deleteAllEvents() {
        managementViewModel.deleteAllEvents();

        String message = "";
        message = "All events are deleted.";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void deleteAllCategories() {
        managementViewModel.deleteAllCategories();

        String message = "";
        message = "All categories are deleted.";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    public void saveButtonClick(View view) {
        String eventIdString = autoGeneratedId();
        String categoryIdString = etCategoryId.getText().toString().toUpperCase();
        String eventNameString = etEventName.getText().toString();
        String ticketsString = etTicketsAvailable.getText().toString();
        boolean activeBool = isActive.isChecked();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {

            categoryList = managementViewModel.getCategoryById(categoryIdString);


            if (categoryList.size() == 1) {
                int ticketsInteger = 0;
                boolean alphanumeric = false;
                add = false;

                cat = categoryList.get(0);

                if (!ticketsString.equals("")) {
                    ticketsInteger = Integer.parseInt(ticketsString);
                }
                for (char character : eventNameString.toCharArray()) {
                    if (Character.isAlphabetic(character)) {
                        alphanumeric = true;
                    }
                }
                if (categoryIdString.equals("") && eventNameString.equals("")) {
                    message = "Invalid event.";
                    add = false;
//                    Toast.makeText(Dashboard.this, message, Toast.LENGTH_SHORT).show();
                } else if (categoryIdString.equals("")) {
                    message = "Category does not exist.";
                    add = false;
//                    Toast.makeText(Dashboard.this, message, Toast.LENGTH_SHORT).show();
                } else if (eventNameString.equals("") || !alphanumeric) {
                    message = "Invalid event name.";
                    add = false;
//                    Toast.makeText(Dashboard.this, message, Toast.LENGTH_SHORT).show();
                } else if (ticketsInteger < 0) {
                    message = "Invalid Tickets available.";
                    add = false;
//                    Toast.makeText(Dashboard.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Event event = new Event(eventIdString, categoryIdString, eventNameString, ticketsInteger, activeBool);
                    managementViewModel.insertEvent(event);
                    cat.setCount(cat.getCount() + 1);
                    managementViewModel.updateCategory(cat);
                    message = String.format("Event saved: %s to %s", eventIdString, categoryIdString);
                    add = true;
                    Snackbar.make(view, "Save button clicked.", Snackbar.LENGTH_LONG)
                            .setAction("Undo", undoOnClickListener).show();
//                    Toast.makeText(Dashboard.this, message, Toast.LENGTH_SHORT).show();
                    uiHandler.post(() -> {

                        etEventId.setText(eventIdString);
                    });
                }
            }
            else {
                message = "Category does not exist.";
                add = false;
            }

            uiHandler.post(() -> {

                //it is safe to Toast here (and do anything else UI related here)
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

            });

    });
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
        // set all editText to blank
            etEventId.setText("");
            etCategoryId.setText("");
            etEventName.setText("");
            etTicketsAvailable.setText("");
            isActive.setChecked(false);

            String message = "";
            message = "Event form is cleared.";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }


}