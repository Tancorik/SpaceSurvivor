package com.tancorikworld.spacesurvivor.presentation.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;

import com.tancorikworld.spacesurvivor.R;
import com.tancorikworld.spacesurvivor.core.graphicscorelib.presentation.DemoCoreGlSurfaceView;

public class MainActivity extends AppCompatActivity {

    GLSurfaceView mDemoGLSurfaceView;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // mDemoGLSurfaceView = findViewById(R.id.open_gl_view);

        mHandler = new Handler();

        // mDemoGLSurfaceView = new DemoScreenView(this);
        // mDemoGLSurfaceView = new DemoGameScreenView(this);

        Point point = new Point();

        getWindowManager().getDefaultDisplay().getSize(point);
        mDemoGLSurfaceView = new DemoCoreGlSurfaceView(this);
        setContentView(mDemoGLSurfaceView);




    }

    @Override
    protected void onResume() {
        super.onResume();
        mDemoGLSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDemoGLSurfaceView.onPause();

    }
}
