package com.fit2081.fit2081assignment1.provider;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.fit2081.fit2081assignment1.Event;
import com.fit2081.fit2081assignment1.EventCategory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities= {Event.class, EventCategory.class}, version = 1)
public abstract class ManagementDatabase extends RoomDatabase {

    public static final String MANAGEMENT_DATABASE = "management_database";

    public abstract  ManagementDAO managementDAO();
    private static volatile ManagementDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static ManagementDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ManagementDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    ManagementDatabase.class, MANAGEMENT_DATABASE)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
