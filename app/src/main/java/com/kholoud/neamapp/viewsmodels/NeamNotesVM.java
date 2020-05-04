package com.kholoud.neamapp.viewsmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.kholoud.neamapp.data.NeamNote;
import com.kholoud.neamapp.data.NeamNoteRepoistory;

import java.util.List;

public class NeamNotesVM extends AndroidViewModel {

    private NeamNoteRepoistory mReposistory;
    private LiveData<List<NeamNote>> mAllNeamNotes;

    public NeamNotesVM(Application application){
        super(application);
        mReposistory = new NeamNoteRepoistory(application);
        mAllNeamNotes = mReposistory.getAllNotes();
    }

    public LiveData<List<NeamNote>> getAllNotes() { return mAllNeamNotes; }
    public void insert(NeamNote note) { mReposistory.insert(note); }
    public void update(NeamNote note) { mReposistory.update(note); }
    public void delete(NeamNote note) { mReposistory.delete(note); }

}
