package com.fit2081.fit2081assignment1;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    ArrayList<EventCategory> data;

    public CategoryAdapter(ArrayList<EventCategory> data) {
        this.data = data;
    }

    public void setData(ArrayList<EventCategory> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card_layout, parent, false); //CardView inflated as RecyclerView list item
        ViewHolder viewHolder = new ViewHolder(v);
//        Log.d("ItemListSize", "Item list size: " + data.size());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        holder.categoryIdTv.setText(data.get(position).getCategory());
        holder.categoryNameTv.setText(data.get(position).getName());
        holder.eventCountTv.setText(String.valueOf(data.get(position).getCount()));
        if (data.get(position).isActive()) {
            holder.isActive.setText("Active");
        } else {
            holder.isActive.setText("Inactive");
        }
//        Log.d("week6App","onBindViewHolder");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView categoryIdTv;
        public TextView categoryNameTv;
        public TextView eventCountTv;
        public TextView isActive;

        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryIdTv = itemView.findViewById(R.id.cat_id_card);
            categoryNameTv = itemView.findViewById(R.id.cat_name_card);
            eventCountTv = itemView.findViewById(R.id.count_card);
            isActive = itemView.findViewById(R.id.cat_isActivecard);
            cardView = itemView.findViewById(R.id.category_card);
        }
    }
}
