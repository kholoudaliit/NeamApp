package com.kholoud.neamapp.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NeamNoteRepoistory {



    private NeamNoteDao mNeamNoteDoa;
    private LiveData<List<NeamNote>> mAllNeamNotes;

    public NeamNoteRepoistory(Application application) {
        NeamNoteDB db = NeamNoteDB.getDatabase(application);
        mNeamNoteDoa = db.neamNoteDao();
        mAllNeamNotes = mNeamNoteDoa.getAllNeamNotes();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<NeamNote>> getAllNotes() {
        return mAllNeamNotes;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(final NeamNote note) {
        NeamNoteDB.databaseWriteExecutor.execute(() -> {
            mNeamNoteDoa.insert(note);
        });
    }

    public void delete (NeamNote note){
        NeamNoteDB.databaseWriteExecutor.execute(() ->{
            mNeamNoteDoa.deleteNote(note);
        });
    }

    public void update (NeamNote note){
        NeamNoteDB.databaseWriteExecutor.execute(() ->{
            mNeamNoteDoa.updateNote(note);
        });
    }




}
