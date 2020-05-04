package com.kholoud.neamapp.data;

import androidx.room.TypeConverter;

import java.util.Calendar;

public class Converters {
    @TypeConverter
    public static Calendar fromTimestamp(Long value) {
        if(value == null ){
            return null;
        }
        else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(value);
            return calendar;
        }
    }

    @TypeConverter
    public static Long dateToTimestamp(Calendar date) {
        return date == null ? null : date.getTimeInMillis();
    }
}
