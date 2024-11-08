package com.fit2081.fit2081assignment1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    ArrayList<Event> data = new ArrayList<Event>();
    public EventAdapter(ArrayList<Event> data) {
        this.data = data;
    }

    public void setData(ArrayList<Event> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card_layout, parent, false); //CardView inflated as RecyclerView list item
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.ViewHolder holder, int position) {
        holder.eventIdTv.setText(data.get(position).getEvent());
        holder.categoryIdTv.setText(data.get(position).getCategory());
        holder.eventNameTv.setText(data.get(position).getName());
        holder.ticketsTv.setText(String.valueOf(data.get(position).getTicket()));
        if (data.get(position).isAcitve()) {
            holder.isActiveEvent.setText("Active");
        } else {
            holder.isActiveEvent.setText("Inactive");
        }
        holder.cardView.setOnClickListener(v -> {
            String eventName = data.get(position).getName();

            Context context = holder.cardView.getContext();
            Intent intent = new Intent(context, EventGoogleResult.class);
            intent.putExtra("event_name", eventName);
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView eventIdTv;
        public TextView categoryIdTv;

        public TextView eventNameTv;
        public TextView ticketsTv;
        public TextView isActiveEvent;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventIdTv = itemView.findViewById(R.id.eventId_card);
            categoryIdTv = itemView.findViewById(R.id.categoryId_event_card);
            eventNameTv = itemView.findViewById(R.id.eventname_card);
            ticketsTv = itemView.findViewById(R.id.tickets_card);
            isActiveEvent = itemView.findViewById(R.id.active_event_card);
            cardView = itemView.findViewById(R.id.event_card);
        }
    }
}
