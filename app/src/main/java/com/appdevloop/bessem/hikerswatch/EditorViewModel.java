package com.appdevloop.bessem.hikerswatch;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.appdevloop.bessem.hikerswatch.databases.AppRepository;
import com.appdevloop.bessem.hikerswatch.databases.LocEntity;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by AppDevloop on 11/12/2018.
 */
public class EditorViewModel extends AndroidViewModel {

    private AppRepository mRepository;
    public MutableLiveData<LocEntity> mLiveNote = new MutableLiveData<>();
    private Executor mExecutor = Executors.newSingleThreadExecutor();

    public EditorViewModel(@NonNull Application application) {
        super(application);
        mRepository = AppRepository.getInstance(application.getApplicationContext());
    }

    public void loadData(final int noteId) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                LocEntity note = mRepository.getNoteById(noteId);
                mLiveNote.postValue(note);
            }
        });
    }

    public void saveNote(String noteText, double lat, double lng, int icon) {
        LocEntity liveNote = mLiveNote.getValue();
        if (liveNote != null) {
            liveNote.setText(noteText.trim());
        } else {
            if (TextUtils.isEmpty(noteText.trim())) {
                return;
            }
            liveNote = new LocEntity(new Date(), noteText.trim(), lat, lng, icon);
        }
        mRepository.insertNote(liveNote);
    }

    public void deleteNote() {
        mRepository.deleteNote(mLiveNote.getValue());
    }
}
