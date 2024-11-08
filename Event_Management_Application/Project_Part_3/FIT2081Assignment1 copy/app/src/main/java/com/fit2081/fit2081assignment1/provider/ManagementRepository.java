package com.fit2081.fit2081assignment1.provider;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fit2081.fit2081assignment1.Event;
import com.fit2081.fit2081assignment1.EventCategory;

import java.util.List;

public class ManagementRepository {

    private ManagementDAO managementDAO;

    private LiveData<List<Event>> allEventsLiveData;

    private LiveData<List<EventCategory>> allEventCategoriesLiveData;

    private volatile boolean mIsUpdating = false;

    private Thread mUpdateThread;

    ManagementRepository(Application application) {
        ManagementDatabase db = ManagementDatabase.getDatabase(application);

        managementDAO = db.managementDAO();

        allEventsLiveData = managementDAO.getAllEvents();

        allEventCategoriesLiveData = managementDAO.getAllCategories();

    }

    LiveData<List<Event>> getAllEvents() {
        return allEventsLiveData;
    }

    void insertEvent(Event event) {
        ManagementDatabase.databaseWriteExecutor.execute(() -> managementDAO.addEvents(event));
    }

    void deleteAllEvents() {
        ManagementDatabase.databaseWriteExecutor.execute(() -> managementDAO.deleteAllEvents());
    }

    LiveData<List<EventCategory>> getAllEventCategories() {
        return allEventCategoriesLiveData;
    }

    void insertCategory(EventCategory category) {
        ManagementDatabase.databaseWriteExecutor.execute(() -> managementDAO.addCategories(category));
    }

    void deleteAllCategories() {
        ManagementDatabase.databaseWriteExecutor.execute(() -> managementDAO.deleteAllCategories());
    }


    List<EventCategory> getCategoryById(String Id){ return managementDAO.getCategoryById(Id);
    }

    void updateCategory(EventCategory eventCategory) {
        ManagementDatabase.databaseWriteExecutor.execute(() -> managementDAO.updateCategory(eventCategory));

    }

    void undoEvent() {
        ManagementDatabase.databaseWriteExecutor.execute(() -> managementDAO.undoEvent());
    }
}
