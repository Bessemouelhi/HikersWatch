package com.appdevloop.bessem.hikerswatch.databases;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by AppDevloop on 11/12/2018.
 */
public class AppRepository {
    private static AppRepository ourInstance;

    public LiveData<List<LocEntity>> mLocs;
    private AppDatabase mDb;
    // One Executor for all operation (queued one after another)
    private Executor executor = Executors.newSingleThreadExecutor();

    public static AppRepository getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new AppRepository(context);
        }
        return ourInstance;
    }

    private AppRepository(Context context) {
        mDb = AppDatabase.getInstance(context);
        mLocs = getAllNotes();
    }

    public void addSampleData() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                //mDb.locDao().insertAll(SampleData.getNotes());
            }
        });
    }

    private LiveData<List<LocEntity>> getAllNotes() {
        return mDb.locDao().getAll();
    }

    public void deleteAllNotes() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.locDao().deleteAll();
            }
        });
    }

    public LocEntity getNoteById(final int noteId) {
        return mDb.locDao().getLocById(noteId);
    }

    public void insertNote(final LocEntity locEntity) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.locDao().insertLoc(locEntity);
            }
        });
    }

    public void deleteNote(final LocEntity locEntity) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.locDao().deleteLoc(locEntity);
            }
        });
    }

}
