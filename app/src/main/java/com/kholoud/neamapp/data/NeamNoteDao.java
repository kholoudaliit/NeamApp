package com.kholoud.neamapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NeamNoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(NeamNote note);

    @Delete
    int deleteNote(NeamNote note);

    @Update
    int updateNote(NeamNote note);

    @Query("SELECT * from naem_table ORDER BY created_at DESC")
    LiveData<List<NeamNote>> getAllNeamNotes();

}
