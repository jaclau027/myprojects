package com.fit2081.fit2081assignment1.provider;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.fit2081.fit2081assignment1.Event;
import com.fit2081.fit2081assignment1.EventCategory;

import java.util.List;

@Dao
public interface ManagementDAO {

    @Query("select * from events")
    LiveData<List<Event>> getAllEvents();

    @Insert
    void addEvents(Event event);

    @Query("delete from events")
    void deleteAllEvents();

    @Query("select * from categories")
    LiveData<List<EventCategory>> getAllCategories();

    @Insert
    void addCategories(EventCategory category);

    @Query("delete from categories")
    void deleteAllCategories();

    @Query("select * from categories where categoryId = :Id Limit 1")
    List<EventCategory> getCategoryById(String Id);

    @Update
    void updateCategory(EventCategory eventCategory);

    @Query("delete from events where id = (select max(id) from events)")
    void undoEvent();

}
