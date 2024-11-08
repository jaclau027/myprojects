package com.fit2081.fit2081assignment1;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "categories")
public class EventCategory {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "categoryId")
    private String category;

    @ColumnInfo(name = "categoryName")
    private String name;

    @ColumnInfo(name = "count")
    private int count;

    @ColumnInfo(name = "active")
    private boolean isActive;

    @ColumnInfo(name = "location")
    private String location;

    public EventCategory(String category, String name, int count, boolean isActive, String location){
        this.category = category;
        this.name = name;
        this.count = count;
        this.isActive = isActive;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
