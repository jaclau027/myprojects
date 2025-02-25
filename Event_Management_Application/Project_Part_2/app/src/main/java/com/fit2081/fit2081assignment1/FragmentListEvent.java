package com.fit2081.fit2081assignment1;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentListEvent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentListEvent extends Fragment {
    private ArrayList<Event> data;
    private SharedPreferences sharedPreferences;
    Gson gson = new Gson();
    EventAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentListEvent() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentListEvent.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentListEvent newInstance(String param1, String param2) {
        FragmentListEvent fragment = new FragmentListEvent();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("sharedPreferences",MODE_PRIVATE);

        String arrayListStringRestored = sharedPreferences.getString("event_key", "[]");
        Type type = new TypeToken<ArrayList<Event>>() {}.getType();
        data = gson.fromJson(arrayListStringRestored,type);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_event, container, false);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("sharedPreferences",MODE_PRIVATE);
        String arrayListStringRestored = sharedPreferences.getString("event_key", "[]");
        Type type = new TypeToken<ArrayList<Event>>() {}.getType();
        data = gson.fromJson(arrayListStringRestored,type);

        RecyclerView recyclerView = view.findViewById(R.id.event_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        EventAdapter adapter = new EventAdapter(data);
        recyclerView.setAdapter(adapter);
        return view;
    }
}