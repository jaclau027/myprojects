package com.fit2081.fit2081assignment1;

public class Event {

    private String event;

    private String category;

    private String name;

    private int ticket;

    private boolean isAcitve;
    public Event(String eventId, String categoryId, String eventName, int tickets, boolean active){
      event = eventId;
      category = categoryId;
      name = eventName;
      ticket = tickets;
      isAcitve = active;
    }

    public String getEvent() {
        return event;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public int getTicket() {
        return ticket;
    }

    public boolean isActive() {
        return isAcitve;
    }

}
