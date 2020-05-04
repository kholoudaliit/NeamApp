package com.kholoud.neamapp.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {NeamNote.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})

public abstract class NeamNoteDB extends RoomDatabase {

    public abstract NeamNoteDao neamNoteDao();

    private static volatile NeamNoteDB INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static NeamNoteDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (NeamNoteDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NeamNoteDB.class, "namenote_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
