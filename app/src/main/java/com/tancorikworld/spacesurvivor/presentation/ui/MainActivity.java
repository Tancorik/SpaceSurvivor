package com.tancorikworld.spacesurvivor.presentation.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.tancorikworld.spacesurvivor.presentation.views.DemoScreenView;

public class MainActivity extends AppCompatActivity {

    GLSurfaceView mDemoGLSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDemoGLSurfaceView = new DemoScreenView(this);
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
