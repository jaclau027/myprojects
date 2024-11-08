package com.fit2081.fit2081assignment1;

public class EventCategory {
    public void setCategory(String category) {
        this.category = category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    private String category;

    private String name;

    private int count;

    private boolean isActive;

    public EventCategory(String categoryIdString, String categoryNameString, int countInteger, boolean activeBool){
        category = categoryIdString;
        name = categoryNameString;
        count = countInteger;
        isActive = activeBool;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public boolean isActive() {
        return isActive;
    }


}
