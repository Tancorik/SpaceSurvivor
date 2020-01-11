package com.tancorikworld.spacesurvivor.core.graphicscorelib.presentation;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.tancorikworld.spacesurvivor.core.graphicscorelib.models.DemoFigure;
import com.tancorikworld.spacesurvivor.core.graphicscorelib.models.IFigure;
import com.tancorikworld.spacesurvivor.core.graphicscorelib.models.notexture.BrokenLineFigure;
import com.tancorikworld.spacesurvivor.core.graphicscorelib.models.notexture.ClosedPolyLineFigure;
import com.tancorikworld.spacesurvivor.core.graphicscorelib.models.notexture.FilledFigure;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class DemoCoreRenderer implements GLSurfaceView.Renderer {

    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];

    IFigure mLine;
    IFigure mTriangle;
    IFigure mBrockenLine;
    IFigure mFilledFig;


    DemoFigure mDemoFigure;
    int mInt = 0;


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.14f, 1.0f);

        // установки для прозрачности текстуры где необходимо
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glEnable(GLES20.GL_BLEND);

        // mLine = new LineFigure(new float[]{0f, 0f, 50f, 50f}, new float[]{1f, 0f, 0f, 1f}, 40f);
        mLine = new BrokenLineFigure(new float[]{0, 0, 50, 0}, new float[]{1f, 0f, 0.5f, 1f}, 5f);
        mTriangle = new ClosedPolyLineFigure(new float[]{0f, 0f, 50f, 0f, 50f, 50f}, new float[]{0f, 1f, 0f, 1f}, 20f);
        mBrockenLine = new ClosedPolyLineFigure(new float[]{0f, 0f, 50f, 0f, 50f, 50f, 100f, 50f}, new float[]{0f, 1f, 1f, 1f}, 10f);

        mFilledFig = new FilledFigure(new float[]{0f, 0f, 50f, 0f, 50f, 50f, 0, 50, -50, 25}, new float[]{0f, 1f, 0f, 1f});

        mDemoFigure = new DemoFigure();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float f = height/(float)width;

        Matrix.frustumM(projectionMatrix, 0, 0, 640, 0, 640 * f, 3, 7);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        Matrix.setLookAtM(viewMatrix, 0, 0, 0, 3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        mLine.draw(viewMatrix, projectionMatrix, 150, 150, 0);
        mTriangle.draw(viewMatrix, projectionMatrix, 200, 200, 0);
        mDemoFigure.draw(viewMatrix, projectionMatrix, 150,150, mInt++, 1);
        mBrockenLine.draw(viewMatrix, projectionMatrix, 0, 0, 0);

        mFilledFig.draw(viewMatrix, projectionMatrix, 400,150, 0);
    }
}
