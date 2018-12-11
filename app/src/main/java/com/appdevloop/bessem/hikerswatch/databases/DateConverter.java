package com.appdevloop.bessem.hikerswatch.databases;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by AppDevloop on 11/12/2018.
 */
public class DateConverter {
    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }


    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
