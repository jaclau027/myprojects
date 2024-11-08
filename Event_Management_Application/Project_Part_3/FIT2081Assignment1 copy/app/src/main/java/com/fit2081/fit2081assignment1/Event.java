package com.fit2081.fit2081assignment1;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "events")
public class Event {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "eventId")
    private String event;

    @ColumnInfo(name = "categoryId")
    private String category;

    @ColumnInfo(name = "eventName")
    private String name;

    @ColumnInfo(name = "tickets")
    private int ticket;

    @ColumnInfo(name = "active")
    private boolean isAcitve;

    public Event(String event, String category, String name, int ticket, boolean isAcitve){
      this.event = event;
      this.category = category;
      this.name = name;
      this.ticket = ticket;
      this.isAcitve = isAcitve;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTicket() {
        return ticket;
    }

    public void setTicket(int ticket) {
        this.ticket = ticket;
    }

    public boolean isAcitve() {
        return isAcitve;
    }

    public void setAcitve(boolean acitve) {
        isAcitve = acitve;
    }
}
