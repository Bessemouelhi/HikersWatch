package com.appdevloop.bessem.hikerswatch.databases;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by AppDevloop on 11/12/2018.
 */
@Dao
public interface LocDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLoc(LocEntity noteEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<LocEntity> notes);

    @Delete
    void deleteLoc(LocEntity noteEntity);

    @Query("SELECT * FROM locs WHERE id = :id")
    LocEntity getLocById(int id);

    @Query("SELECT * FROM locs ORDER BY date DESC")
    LiveData<List<LocEntity>> getAll();

    @Query("DELETE FROM locs")
    int deleteAll();

    @Query("SELECT COUNT(*) FROM locs")
    int getCount();
}
