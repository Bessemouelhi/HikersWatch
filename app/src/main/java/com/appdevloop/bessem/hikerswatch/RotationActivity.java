package com.appdevloop.bessem.hikerswatch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.appdevloop.bessem.hikerswatch.api.MultiTouchListener;
import com.appdevloop.bessem.hikerswatch.api.RotationGestureDetector;

public class RotationActivity extends AppCompatActivity implements RotationGestureDetector.OnRotationGestureListener {

    ImageView mImageView;
    private RotationGestureDetector mRotationDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotation);

        mImageView = findViewById(R.id.imageView);
        mImageView.setOnTouchListener(new MultiTouchListener());
        mRotationDetector = new RotationGestureDetector(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        mRotationDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean OnRotation(RotationGestureDetector rotationDetector) {
        float angle = rotationDetector.getAngle();
        // mImageView.setRotation(mImageView.getRotation() - angle);
        Log.d("RotationGestureDetector", "Rotation: " + Float.toString(angle));
        return false;
    }
}
