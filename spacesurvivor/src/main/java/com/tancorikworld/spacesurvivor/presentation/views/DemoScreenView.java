package com.tancorikworld.spacesurvivor.presentation.views;

import android.content.Context;
import android.graphics.Canvas;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.tancorikworld.spacesurvivor.presentation.renderers.DemoMainGameColoredRenderer;
import com.tancorikworld.spacesurvivor.presentation.renderers.DemoRenderer;

public class DemoScreenView extends GLSurfaceView {

    private final DemoRenderer mRenderer;
    // private final DemoMainGameColoredRenderer mRenderer;


    public DemoScreenView(Context context) {
        this(context, null);
    }

    public DemoScreenView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        mRenderer = new DemoRenderer();
        // mRenderer = new DemoMainGameColoredRenderer();

        setRenderer(mRenderer);

        // установить режим отрисовки по запросу
        // setRenderMode(RENDERMODE_WHEN_DIRTY);
        // requestRender(); // запрос перерисовки
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();
        // if (event.getAction() == MotionEvent.ACTION_DOWN)
        //     mRenderer.setCoord(event.getX(), event.getY());
        queueEvent(() ->  mRenderer.setCoord(x, y));
        return true; //super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
