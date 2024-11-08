package com.fit2081.fit2081assignment1;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.BarringInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fit2081.fit2081assignment1.provider.ManagementViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentListCategory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentListCategory extends Fragment {

    private ArrayList<EventCategory> data;
    Gson gson = new Gson();
    CategoryAdapter adapter;

    private ManagementViewModel managementViewModel;

    private LiveData<List<EventCategory>> allCategoriesLiveData;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentListCategory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentListCategory.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentListCategory newInstance(String param1, String param2) {
        FragmentListCategory fragment = new FragmentListCategory();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
//        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("sharedPreferences",MODE_PRIVATE);
//
//        String arrayListStringRestored = sharedPreferences.getString("category_key", "[]");
//        Type type = new TypeToken<ArrayList<EventCategory>>() {}.getType();
//        data = gson.fromJson(arrayListStringRestored,type);

//        managementViewModel = new ViewModelProvider(this).get(ManagementViewModel.class);
//        managementViewModel.getAllEventCategories().observe(this, newData -> {
//            data = new ArrayList<EventCategory>(newData);
//        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_category, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.cat_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ManagementViewModel managementViewModel = new ViewModelProvider(this).get(ManagementViewModel.class);
        allCategoriesLiveData = managementViewModel.getAllEventCategories();
        allCategoriesLiveData.observe(getViewLifecycleOwner(), new Observer<List<EventCategory>>() {
            @Override
            public void onChanged(List<EventCategory> eventCategories) {
                ArrayList<EventCategory> categories = new ArrayList<>(eventCategories);
                adapter = new CategoryAdapter(categories);
                recyclerView.setAdapter(adapter);
            }
        });

        return view;
    }

}