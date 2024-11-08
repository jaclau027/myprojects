package com.fit2081.fit2081assignment1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;


import com.fit2081.fit2081assignment1.provider.ManagementViewModel;

import java.util.ArrayList;

public class ListCategoryActivity extends AppCompatActivity {

    ArrayList<EventCategory> data = new ArrayList<>();

    private ManagementViewModel managementViewModel;

    private CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.back_bar_layout);

//        managementViewModel = new ViewModelProvider(this).get(ManagementViewModel.class);
//
//        managementViewModel.getAllEventCategories().observe(this, newData -> {
//            categoryAdapter.setData(new ArrayList<EventCategory>(newData));
//        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);

        // back button functionality
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Event Category Page");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.fragmentContainerViewListCategory, new FragmentListCategory())
//                .commit();


    }
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}