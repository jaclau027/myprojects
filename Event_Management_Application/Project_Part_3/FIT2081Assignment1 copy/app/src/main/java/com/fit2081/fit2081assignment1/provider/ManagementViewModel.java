package com.fit2081.fit2081assignment1.provider;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.fit2081.fit2081assignment1.Event;
import com.fit2081.fit2081assignment1.EventCategory;

import java.util.List;

public class ManagementViewModel extends AndroidViewModel {
    private ManagementRepository repository;

    private LiveData<List<Event>> allEventsLiveData;

    private LiveData<List<EventCategory>> allEventCategoriesLiveData;




    public ManagementViewModel(@NonNull Application application) {
        super(application);

        repository = new ManagementRepository(application);

        allEventsLiveData = repository.getAllEvents();

        allEventCategoriesLiveData = repository.getAllEventCategories();

    }

    public LiveData<List<Event>> getAllEvents() {
        return allEventsLiveData;
    }

    public void insertEvent(Event event) {
        repository.insertEvent(event);
    }

    public void deleteAllEvents() {
        repository.deleteAllEvents();
    }

    public LiveData<List<EventCategory>> getAllEventCategories() {
        return allEventCategoriesLiveData;
    }

    public void insertCategory(EventCategory category) {
        repository.insertCategory(category);
    }

    public void deleteAllCategories() {
        repository.deleteAllCategories();
    }

    public List<EventCategory> getCategoryById(String Id) { return repository.getCategoryById(Id);}


    public void updateCategory(EventCategory eventCategory) {
        repository.updateCategory(eventCategory);
    }

    public void undoEvent() {
        repository.undoEvent();
    }

}
