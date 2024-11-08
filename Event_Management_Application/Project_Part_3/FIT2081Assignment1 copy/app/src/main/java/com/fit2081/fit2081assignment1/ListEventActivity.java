package com.fit2081.fit2081assignment1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.MenuItem;

import com.fit2081.fit2081assignment1.provider.ManagementViewModel;

import java.util.ArrayList;

public class ListEventActivity extends AppCompatActivity {

    private ManagementViewModel managementViewModel;

    private EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.back_bar_event_layout);

        managementViewModel = new ViewModelProvider(this).get(ManagementViewModel.class);

//        managementViewModel.getAllEvents().observe(this, newData -> {
//            eventAdapter.setData(new ArrayList<Event>(newData));
//        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Event page");
        // back button functionality
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerViewEvent, new FragmentListEvent())
                .commit();
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}