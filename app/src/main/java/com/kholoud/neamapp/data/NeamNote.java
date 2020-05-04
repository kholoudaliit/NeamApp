package com.kholoud.neamapp.data;

import android.text.method.DateTimeKeyListener;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Entity (tableName = "naem_table")
public class NeamNote implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo (name = "id")
    public int ID;

    @ColumnInfo (name = "description")
    public String description;

    @ColumnInfo (name = "created_at")
    public Calendar created_at ;

    @ColumnInfo (name = "neamNote_image")
    public String neamNoteImage;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Calendar getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Calendar created_at) {
        this.created_at = created_at;
    }

    public String getNeamNoteImage() {
        return neamNoteImage;
    }

    public void setNeamNoteImage(String neamNoteImage) {
        this.neamNoteImage = neamNoteImage;
    }
}
