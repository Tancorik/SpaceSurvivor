package com.tancorikworld.spacesurvivor.presentation.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.tancorikworld.spacesurvivor.core.graphicscorelib.CoreGraph;
import com.tancorikworld.spacesurvivor.presentation.renderers.DemoMainGameColoredRenderer;

/**
 * Вьюшка для игрового экрана
 *
 * @author tancorik
 */
public class DemoGameScreenView extends GLSurfaceView {

    private static final int EGL_CONTEXT_CLIENT_VERSION = 2;

    private final Renderer mRenderer;

    private CoreGraph mCoreGraph;

    /**
     * Конструктор для динамического создания
     *
     * @param context контекст
     */
    public DemoGameScreenView(Context context) {
        this(context, null);
    }

    /**
     * Конструктор для создания в разметке
     *
     * @param context контекст
     * @param attrs   атрибуты
     */
    public DemoGameScreenView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRenderer = new DemoMainGameColoredRenderer();

        setEGLContextClientVersion(EGL_CONTEXT_CLIENT_VERSION);
        setRenderer(mRenderer);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
