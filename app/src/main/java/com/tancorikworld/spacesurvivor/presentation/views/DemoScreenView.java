package com.tancorikworld.spacesurvivor.presentation.views;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.tancorikworld.spacesurvivor.presentation.renderers.DemoRenderer;

public class DemoScreenView extends GLSurfaceView {

    private final DemoRenderer mRenderer;

    public DemoScreenView(Context context) {
        this(context, null);
    }

    public DemoScreenView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        mRenderer = new DemoRenderer();

        setRenderer(mRenderer);
        // setRenderMode(RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        mRenderer.setCoord(event.getX(), event.getY());

        return super.onTouchEvent(event);
    }
}
