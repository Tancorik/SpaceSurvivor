package com.tancorikworld.spacesurvivor.presentation.renderers;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.tancorikworld.spacesurvivor.application.SpaceApplication;
import com.tancorikworld.spacesurvivor.models.demo.gameobjects.DemoStaticSpace;
import com.tancorikworld.spacesurvivor.models.demo.gameobjects.abstractions.GameObject;
import com.tancorikworld.spacesurvivor.models.demo.helpers.SimpleColor;
import com.tancorikworld.spacesurvivor.models.demo.primitive.FilledRectangleFigure;
import com.tancorikworld.spacesurvivor.models.demo.primitive.FilledSomeAngleFigure;
import com.tancorikworld.spacesurvivor.models.demo.primitive.FilledStarFigure;
import com.tancorikworld.spacesurvivor.models.demo.primitive.abstraction.ISimpleFigure2D;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Рендерер для главного экрана для игры с цветными премитивами (без текстур)
 *
 * @author tancorik
 */
public class DemoMainGameColoredRenderer implements GLSurfaceView.Renderer {

    // пытаюсь привести размер к единому, для однаковой скорости игровых обьектов на разных размерах экрана
    private static final int SCREEN_WIDTH = 640;

    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    private int mProgram;

    private GameObject mSpace;

    private float mScreenHeight;
    private float mScreenRatio;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.1f, 1.0f);
        mProgram = SpaceApplication.getAppComponent().getProgramCreator().createProgram(false);

        mSpace = new DemoStaticSpace(mProgram);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        mScreenRatio = (float) SCREEN_WIDTH / width;
        mScreenHeight = mScreenRatio * height;

        Matrix.frustumM(mProjectionMatrix, 0, 0, SCREEN_WIDTH, 0, mScreenHeight, 3, 7);

        mSpace.setScreenSize(SCREEN_WIDTH, mScreenHeight);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        mSpace.redraw(mViewMatrix, mProjectionMatrix);
    }
}
